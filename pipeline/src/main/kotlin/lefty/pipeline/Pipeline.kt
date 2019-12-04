package lefty.pipeline

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import lefty.pipeline.build.storage.BuildRepository
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
        @ForPipeline private val disposable: CompositeDisposable,
        @ForPipeline private val workingDirectory: Path,
        @ForPipeline private val specification: Specification
) {
    companion object {
        private val LOG = LoggerFactory.getLogger(Pipeline::class.java)
    }

    fun run() {
        LOG.info("Starting pipeline in directory $workingDirectory")

        Flowable
                .fromIterable(specification.steps)
                .concatMapCompletable { step ->
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

                    Completable.fromAction {
                        LOG.info("Waiting for process to finish")
                        val result = process.waitFor()
                        if (result != 0) {
                            throw RuntimeException("Command failed with status code $result")
                        }
                    }.doOnDispose {
                        logs.dispose()
                        process.destroyForcibly()
                    }
                }.subscribe({
                    LOG.info("Pipeline done")
                }, { err ->
                    LOG.error("Error executing pipeline", err)
                }).addTo(disposable)
    }
}