package lefty.pipeline.dagger.pipeline

import dagger.BindsInstance
import dagger.Subcomponent
import lefty.pipeline.Pipeline
import lefty.pipeline.PipelineInfo
import lefty.pipeline.Specification
import lefty.pipeline.build.StoredBuild
import java.nio.file.Path

@PipelineScope
@Subcomponent(modules = [
    PipelineModules.PipelineModule::class
])
interface PipelineComponent {
    fun pipeline(): Pipeline

    @Subcomponent.Builder
    interface Builder {

        @BindsInstance fun bindsBuild(@ForPipeline build: StoredBuild): Builder
        @BindsInstance fun bindsSpecification(@ForPipeline specification: Specification): Builder
        @BindsInstance fun bindsWorkingDirectory(@ForPipeline workingDirectory: Path): Builder
        @BindsInstance fun bindsPipelineInfo(@ForPipeline pipelineInfo: PipelineInfo): Builder

        fun build(): PipelineComponent
    }
}