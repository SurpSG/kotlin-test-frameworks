plugins {
    `test-framework-subproject`
}

dependencies {
    implementation(project(":code-to-test"))

    testImplementation("io.kotest:kotest-runner-junit5:5.5.4")
}
