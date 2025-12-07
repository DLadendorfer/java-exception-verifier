plugins {
    id("java")
}

group = "com.aero.servicely"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    compileOnly(libs.lombok)
    compileOnly(libs.bundles.junit5)
    compileOnly(libs.jspecify)

    annotationProcessor(libs.lombok)

    testCompileOnly(libs.lombok)
    testAnnotationProcessor(libs.lombok)

    testImplementation(libs.bundles.junit5)
}

tasks.test {
    useJUnitPlatform()
}