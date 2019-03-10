package lefty.dagger

import dagger.Module
import lefty.pipeline.dagger.PipelineComponent

@Module(subcomponents = [
    PipelineComponent::class
])
class ApplicationModule