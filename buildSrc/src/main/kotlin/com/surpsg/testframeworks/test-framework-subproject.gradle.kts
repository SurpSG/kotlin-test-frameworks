import org.gradle.kotlin.dsl.dependencies

plugins {
    id("kotlin-subproject")
}

tasks.test {
    useJUnitPlatform()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
}
