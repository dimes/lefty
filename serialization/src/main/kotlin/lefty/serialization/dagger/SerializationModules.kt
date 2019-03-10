package lefty.serialization.dagger

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

class SerializationModules {
    @Module
    class ApplicationModule {
        @ForYaml
        @Provides
        @Singleton
        fun providesObjectMapper(): ObjectMapper {
            return ObjectMapper(YAMLFactory())
        }
    }
}