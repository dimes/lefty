package lefty.pipeline

import io.reactivex.Flowable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import lefty.pipeline.dagger.ForPipeline
import org.slf4j.LoggerFactory
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
                    LOG.info("Loading image ${step.image}")
                    step.commands.forEach { command ->
                        LOG.info("Executing command $command")
                    }
                }.subscribe({
                    LOG.info("Pipeline done")
                }, { err ->
                    LOG.error("Error executing pipeline", err)
                }).addTo(disposable)
    }
}