package lefty.pipeline

import lefty.pipeline.dagger.build.BuildScope
import lefty.pipeline.dagger.build.ForBuild
import lefty.pipeline.dagger.pipeline.PipelineComponent
import java.nio.file.Paths
import javax.inject.Inject
import javax.inject.Provider

@BuildScope
class Build @Inject constructor(
        @ForBuild private val specifications: List<Specification>,
        private val pipelineBuilder: Provider<PipelineComponent.Builder>
) {
    fun run() {
        specifications.forEach { specification ->
            pipelineBuilder
                    .get()
                    .bindsWorkingDirectory(Paths.get("./test"))
                    .bindsSpecification(specification)
                    .build()
                    .pipeline()
                    .run()
        }
    }
}