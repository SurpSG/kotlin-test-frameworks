rootProject.name = "kotlin-test-frameworks"
include("kotest")
include("kluent")
include("strikt")
include("spek")
include("code-to-test")
include("github")

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        maven("https://plugins.gradle.org/m2/")
        maven("https://jitpack.io")
    }
}
