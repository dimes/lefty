package lefty.dagger

import dagger.Module
import lefty.pipeline.dagger.build.BuildComponent
import lefty.pipeline.dagger.build.BuildModules
import lefty.pipeline.dagger.pipeline.PipelineComponent
import lefty.pipeline.dagger.pipeline.PipelineModules
import lefty.serialization.dagger.SerializationModules

@Module(
        includes = [
            SerializationModules.ApplicationModule::class,
            BuildModules.ApplicationModule::class
        ],
        subcomponents = [
            BuildComponent::class
        ])
class ApplicationModule