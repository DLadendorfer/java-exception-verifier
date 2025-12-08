plugins {
    id("java")
}

group = "com.aero.testing"
version = "0.1.0"

repositories {
    mavenCentral()
}

dependencies {
    compileOnly(libs.lombok)
    compileOnly(libs.jspecify)

    annotationProcessor(libs.lombok)

    testCompileOnly(libs.lombok)
    testAnnotationProcessor(libs.lombok)

    testImplementation(libs.bundles.junit5)
}

tasks.test {
    useJUnitPlatform()
}