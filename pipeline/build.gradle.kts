plugins {
    `plugin-kotlin`
    `plugin-kapt`

    `java-library`
}

repositories {
    mavenCentral()
}

dependencies {
    kapt(deps.daggerCompiler)

    implementation(project(":base"))

    implementation(deps.dagger)
    implementation(deps.rxjava)
    implementation(deps.rxkotlin)

    testImplementation(deps.kotlinTest)
    testImplementation(deps.kotlinTestJunit)
}