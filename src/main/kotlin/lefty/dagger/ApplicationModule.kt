package lefty.dagger

import dagger.Module
import lefty.pipeline.dagger.build.BuildComponent
import lefty.pipeline.dagger.build.BuildModules
import lefty.serialization.dagger.SerializationModules

@Module(
        includes = [
            SerializationModules.SingletonModule::class,
            BuildModules.SingletonModule::class
        ],
        subcomponents = [
            BuildComponent::class
        ])
class ApplicationModule