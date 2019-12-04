package lefty.pipeline.dagger.pipeline

import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable

class PipelineModules {
    @Module
    class PipelineModule {
        @Provides
        @ForPipeline
        @PipelineScope
        fun providesPipelineDisposable() = CompositeDisposable()
    }
}
