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
    api(deps.jacksonDatabind)
    api(deps.jacksonAnnotations)
    implementation(deps.jacksonDataformatYaml)

    testImplementation(deps.kotlinTest)
    testImplementation(deps.kotlinTestJunit)
}