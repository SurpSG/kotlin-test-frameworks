plugins {
    `test-framework-subproject`
}

dependencies {
    implementation(project(":code-to-test"))

    testImplementation("org.amshove.kluent:kluent:1.72")
}
