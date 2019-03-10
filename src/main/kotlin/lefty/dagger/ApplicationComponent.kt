package lefty.dagger

import dagger.Component
import lefty.App
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class])
interface ApplicationComponent {
    fun app(): App

    @Component.Builder
    interface Builder {
        fun build(): ApplicationComponent
    }
}