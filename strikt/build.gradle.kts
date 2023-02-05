plugins {
    `test-framework-subproject`
}

dependencies {
    implementation(project(":code-to-test"))

    testImplementation(platform("io.strikt:strikt-bom:0.34.1"))
    testImplementation("io.strikt:strikt-jackson")
    testImplementation("io.strikt:strikt-jvm")
    testImplementation("io.strikt:strikt-spring")
}
