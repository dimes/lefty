package lefty.pipeline.dagger

import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable
import lefty.pipeline.Specification
import java.nio.file.Path

@Module
class PipelineModule(
        private val workingDirectory: Path,
        private val specification: Specification
) {
    @Provides
    @ForPipeline
    fun providesPipelineDisposable() = CompositeDisposable()

    @Provides
    @ForPipeline
    fun providesSpecification(): Specification = specification

    @Provides
    @ForPipeline
    fun providesWorkingDirectory(): Path = workingDirectory
}