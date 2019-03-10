plugins {
    `plugin-kotlin`
    `plugin-kapt`

    `java-library`
}

repositories {
    mavenCentral()
}

dependencies {
    api(deps.slf4j)
}