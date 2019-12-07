package lefty.pipeline

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import lefty.pipeline.build.StoredBuild
import lefty.pipeline.build.storage.BuildRepository
import lefty.pipeline.build.storage.BuildStatus
import lefty.pipeline.dagger.pipeline.ForPipeline
import lefty.pipeline.logs.LogEntry
import lefty.pipeline.logs.LogType
import org.slf4j.LoggerFactory
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.RuntimeException
import java.nio.file.Path
import javax.inject.Inject

class Pipeline @Inject constructor(
        private val buildRepository: BuildRepository,
        @ForPipeline private val build: StoredBuild,
        @ForPipeline private val workingDirectory: Path,
        @ForPipeline private val specification: Specification,
        @ForPipeline private val pipelineInfo: PipelineInfo
) {
    companion object {
        private val LOG = LoggerFactory.getLogger(Pipeline::class.java)
    }

    fun run(): Completable {
        LOG.info("Starting pipeline in directory $workingDirectory")
        return Flowable
                .fromIterable(specification.steps)
                .concatMapSingle { step ->
                    val pipelineIndex = pipelineInfo.pipelineIndex
                    val stepIndex = specification.steps.indexOf(step)
                    LOG.info("Setting status of pipeline/step $pipelineIndex/$stepIndex to RUNNING")

                    buildRepository
                            .updateStepStatus(build.buildNumber, pipelineIndex, stepIndex, BuildStatus.RUNNING)
                            .andThen(executeStep(step))
                }.flatMapCompletable { (step, status) ->
                    val pipelineIndex = pipelineInfo.pipelineIndex
                    val stepIndex = specification.steps.indexOf(step)
                    LOG.info("Setting status of pipeline/step $pipelineIndex/$stepIndex to $status")

                    buildRepository.updateStepStatus(build.buildNumber, pipelineIndex, stepIndex, status)
                }
    }

    /**
     * Executes the given step. Should only be invoked inside an rx chain.
     */
    private fun executeStep(step: Step): Single<Pair<Step, BuildStatus>> {
        LOG.info("Running ${step.image} with commands ${step.commands}")
        val process = ProcessBuilder(
                "docker",
                "run",
                "-w", "/workspace",
                "--mount", "type=bind,source=${workingDirectory.toAbsolutePath()},target=/workspace",
                *step.environment.map { entry ->
                    listOf("-e", "CUSTOM_${entry.key}=${entry.value}")
                }.flatten().toTypedArray(),
                step.image,
                "sh", "-c", step.commands.joinToString(";")
        ).start()

        val stdout = Flowable
                .fromIterable(
                        Iterable(BufferedReader(InputStreamReader(process.inputStream)).lines()::iterator))
                .map { LogEntry(LogType.STDOUT, it) }

        val stderr = Flowable
                .fromIterable(
                        Iterable(BufferedReader(InputStreamReader(process.errorStream)).lines()::iterator))
                .map { LogEntry(LogType.STDERR, it) }

        val logs = Flowable
                .merge(stdout, stderr)
                .subscribeOn(Schedulers.io())
                .subscribe {
                    LOG.info("${it.type} ${it.line}")
                }

        return Completable.fromAction {
            LOG.info("Waiting for process to finish")
            val result = process.waitFor()
            if (result != 0) {
                throw RuntimeException("Command failed with status code $result")
            }

            LOG.info("Pipeline completed successfully")
        }.doOnDispose {
            logs.dispose()
            process.destroyForcibly()
        }.toSingleDefault(Pair(step, BuildStatus.COMPLETE)).onErrorReturn { err ->
            LOG.error("Error during build", err)
            Pair(step, BuildStatus.ERROR)
        }
    }
}