import org.gradle.kotlin.dsl.kotlin
import org.gradle.plugin.use.PluginDependenciesSpec

object deps {
    val kotlinVersion = "1.3.20"
    val kotlinSdk = "org.jetbrains.kotlin:kotlin-stdlib-jdk8"
    val kotlinTest = "org.jetbrains.kotlin:kotlin-test"
    val kotlinTestJunit = "org.jetbrains.kotlin:kotlin-test-junit"

    val daggerVersion = "2.21"
    val dagger = "com.google.dagger:dagger:$daggerVersion"
    val daggerCompiler = "com.google.dagger:dagger-compiler:$daggerVersion"

    val rxjava = "io.reactivex.rxjava2:rxjava:2.2.7"
    val rxkotlin = "io.reactivex.rxjava2:rxkotlin:2.3.0"

    val slf4j = "org.slf4j:slf4j-api:1.7.26"
    val slf4jSimple = "org.slf4j:slf4j-simple:1.7.26"
}

val PluginDependenciesSpec.`plugin-kotlin`
    get() = kotlin("jvm")

val PluginDependenciesSpec.`plugin-kapt`
    get() = kotlin("kapt")
