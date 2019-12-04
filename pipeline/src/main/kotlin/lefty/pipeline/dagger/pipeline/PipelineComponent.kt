package lefty.pipeline.dagger.pipeline

import dagger.BindsInstance
import dagger.Subcomponent
import lefty.pipeline.Pipeline
import lefty.pipeline.Specification
import java.nio.file.Path

@PipelineScope
@Subcomponent(modules = [
    PipelineModules.PipelineModule::class
])
interface PipelineComponent {
    fun pipeline(): Pipeline

    @Subcomponent.Builder
    interface Builder {

        @BindsInstance fun bindsSpecification(@ForPipeline specification: Specification): Builder
        @BindsInstance fun bindsWorkingDirectory(@ForPipeline workingDirectory: Path): Builder

        fun build(): PipelineComponent
    }
}