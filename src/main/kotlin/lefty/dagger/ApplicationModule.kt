package lefty.dagger

import dagger.Module
import lefty.pipeline.dagger.PipelineComponent
import lefty.serialization.dagger.SerializationModules

@Module(
        includes = [
            SerializationModules.ApplicationModule::class
        ],
        subcomponents = [
            PipelineComponent::class
        ])
class ApplicationModule