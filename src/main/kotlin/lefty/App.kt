/*
 * This Kotlin source file was generated by the Gradle 'init' task.
 */
package lefty

import com.fasterxml.jackson.databind.ObjectMapper
import lefty.dagger.DaggerApplicationComponent
import lefty.pipeline.dagger.build.BuildComponent
import lefty.pipeline.serialization.SerializedSpecification
import lefty.serialization.dagger.ForYaml
import javax.inject.Inject
import javax.inject.Provider

class App @Inject constructor(
        @ForYaml private val yamlObjectMapper: ObjectMapper,
        private val buildBuilder: Provider<BuildComponent.Builder>
) {
    companion object {
        private const val TEST_YAML = """
steps:
  - type: builtin
    name: git
    environment:
      GIT_REPO: https://github.com/dimes/lefty.git
      GIT_BRANCH: master
        """
    }

    fun run() {
        val yamlSpecification = yamlObjectMapper.readValue(TEST_YAML, SerializedSpecification::class.java)
        val build = buildBuilder
                .get()
                .bindsSpecifications(listOf(yamlSpecification.toSpecification()))
                .build()
                .build()
        build.run()
    }
}

fun main() {
    val applicationComponent = DaggerApplicationComponent
            .builder()
            .build()

    val app = applicationComponent.app()
    app.run()
}
