import org.gradle.kotlin.dsl.kotlin
import org.gradle.plugin.use.PluginDependenciesSpec

object deps {
    const val kotlinVersion = "1.3.20"
    const val kotlinSdk = "org.jetbrains.kotlin:kotlin-stdlib-jdk8"
    const val kotlinTest = "org.jetbrains.kotlin:kotlin-test"
    const val kotlinTestJunit = "org.jetbrains.kotlin:kotlin-test-junit"

    private const val daggerVersion = "2.21"
    const val dagger = "com.google.dagger:dagger:$daggerVersion"
    const val daggerCompiler = "com.google.dagger:dagger-compiler:$daggerVersion"

    const val rxjava = "io.reactivex.rxjava2:rxjava:2.2.7"
    const val rxkotlin = "io.reactivex.rxjava2:rxkotlin:2.3.0"

    const val slf4j = "org.slf4j:slf4j-api:1.7.26"
    const val slf4jSimple = "org.slf4j:slf4j-simple:1.7.26"

    const val jacksonVersion = "2.9.8"
    const val jacksonAnnotations = "com.fasterxml.jackson.core:jackson-annotations:$jacksonVersion"
    const val jacksonDatabind = "com.fasterxml.jackson.core:jackson-databind:$jacksonVersion"
    const val jacksonDataformatYaml = "com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:$jacksonVersion"
}

val PluginDependenciesSpec.`plugin-kotlin`
    get() = kotlin("jvm")

val PluginDependenciesSpec.`plugin-kapt`
    get() = kotlin("kapt")
