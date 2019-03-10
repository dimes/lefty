import org.jetbrains.kotlin.kapt3.base.Kapt.kapt

plugins {
    `plugin-kotlin`.version(deps.kotlinVersion)
    `plugin-kapt`.version(deps.kotlinVersion)

    application
}

repositories {
    mavenCentral()
}

configurations.all {
    resolutionStrategy {
        failOnVersionConflict()

        force("com.google.guava:guava:25.0-jre")
        force("org.checkerframework:checker-compat-qual:2.5.3")
        force("org.jetbrains.kotlin:kotlin-stdlib:${deps.kotlinVersion}")
        force("com.fasterxml.jackson.core:jackson-annotations:${deps.jacksonVersion}")
        force(deps.rxjava)
    }
}

dependencies {
    kapt(deps.daggerCompiler)

    implementation(deps.dagger)
    implementation(deps.rxjava)

    implementation(project(":base"))
    implementation(project(":builtin"))
    implementation(project(":pipeline"))
    implementation(project(":pipeline-serialization"))
    implementation(project(":serialization"))

    runtime(deps.slf4jSimple)

    testImplementation(deps.kotlinTest)
    testImplementation(deps.kotlinTestJunit)
}

application {
    // Define the main class for the application.
    mainClassName = "lefty.AppKt"
}
