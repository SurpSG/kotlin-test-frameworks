plugins {
    `kotlin-subproject`
}

dependencies {
    implementation(project(":code-to-test"))
}

// setup dependencies
dependencies {
    testImplementation("org.spekframework.spek2:spek-dsl-jvm:2.0.19")
    testRuntimeOnly("org.spekframework.spek2:spek-runner-junit5:2.0.19")

//     spek requires kotlin-reflect, can be omitted if already in the classpath
    testRuntimeOnly("org.jetbrains.kotlin:kotlin-reflect")
}

tasks.withType<Test>() {
    useJUnitPlatform {
        includeEngines = setOf("spek2")
    }
}
