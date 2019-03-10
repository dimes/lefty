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
    implementation(project(":pipeline"))
}