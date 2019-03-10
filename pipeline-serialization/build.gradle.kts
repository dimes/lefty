plugins {
    `plugin-kotlin`
    `plugin-kapt`

    `java-library`
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":base"))
    implementation(project(":builtin"))
    implementation(project(":pipeline"))
    implementation(project(":serialization"))

    testImplementation(deps.kotlinTest)
    testImplementation(deps.kotlinTestJunit)
}