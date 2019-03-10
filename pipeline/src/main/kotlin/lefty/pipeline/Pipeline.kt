package lefty.pipeline

import io.reactivex.Flowable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import lefty.pipeline.dagger.ForPipeline
import org.slf4j.LoggerFactory
import java.lang.RuntimeException
import java.nio.file.Path
import javax.inject.Inject

class Pipeline @Inject constructor(
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
                .map { step ->
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
                    )
                            .redirectOutput(ProcessBuilder.Redirect.INHERIT)
                            .redirectError(ProcessBuilder.Redirect.INHERIT)
                            .start()
                    val result = process.waitFor()
                    if (result != 0) {
                        throw RuntimeException("Command failed")
                    }
                }.subscribe({
                    LOG.info("Pipeline done")
                }, { err ->
                    LOG.error("Error executing pipeline", err)
                }).addTo(disposable)
    }
}