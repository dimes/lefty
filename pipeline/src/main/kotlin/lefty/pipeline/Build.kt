package lefty.pipeline

import io.reactivex.Completable
import lefty.pipeline.build.storage.BuildRepository
import lefty.pipeline.dagger.build.BuildScope
import lefty.pipeline.dagger.build.ForBuild
import lefty.pipeline.dagger.pipeline.PipelineComponent
import java.nio.file.Path
import javax.inject.Inject
import javax.inject.Provider

@BuildScope
class Build @Inject constructor(
        private val buildRepository: BuildRepository,
        @ForBuild private val workingDirectory: Path,
        @ForBuild private val specifications: List<Specification>,
        private val pipelineBuilder: Provider<PipelineComponent.Builder>
) {
    fun run(): Completable {
        return buildRepository
                .newStoredBuild(specifications)
                .flatMapCompletable { build ->
                    Completable.merge(specifications.mapIndexed { index, specification ->
                        pipelineBuilder
                                .get()
                                .bindsBuild(build)
                                .bindsWorkingDirectory(workingDirectory)
                                .bindsSpecification(specification)
                                .bindsPipelineInfo(PipelineInfo(index))
                                .build()
                                .pipeline()
                                .run()
                    })
                }
    }
}