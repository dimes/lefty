package lefty.pipeline.dagger

import dagger.Subcomponent
import lefty.pipeline.Pipeline

@PipelineScope
@Subcomponent(modules = [
    PipelineModule::class
])
interface PipelineComponent {
    fun pipeline(): Pipeline

    @Subcomponent.Builder
    interface Builder {
        fun pipelineModule(module: PipelineModule): Builder

        fun build(): PipelineComponent
    }
}