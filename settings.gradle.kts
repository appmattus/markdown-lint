pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

rootProject.name = "markdown-lint"

include(
    "core",
    "markdown"
)
