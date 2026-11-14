import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import io.gitlab.arturbosch.detekt.Detekt

// Top-level build file where you can add configuration options common to all sub-projects/modules.

plugins {
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.coverallsPlugin) apply false
    alias(libs.plugins.androidLintPlugin) apply false
    alias(libs.plugins.gradlePluginPublishPlugin) apply false
    alias(libs.plugins.jacocoTestKitPlugin) apply false
    alias(libs.plugins.gradleVersionsPlugin)
    alias(libs.plugins.detektPlugin)
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

tasks.register<Delete>("clean") {
    delete(rootProject.layout.buildDirectory)
}

tasks.withType<DependencyUpdatesTask>().configureEach {
    rejectVersionIf {
        listOf("alpha", "beta", "rc", "cr", "m", "preview")
            .map { qualifier -> Regex("(?i).*[.-]$qualifier[.\\d-]*") }
            .any { it.matches(candidate.version) }
    }
}

dependencies {
    detektPlugins(libs.detekt.formatting)
}

detekt {
    source.setFrom(files(projectDir))

    buildUponDefaultConfig = true

    // To override MaxLineLength:excludeCommentStatements
    config.setFrom(files("detekt-config.yml"))

    autoCorrect = true
}

tasks.withType<Detekt>().configureEach {
    exclude("**/build/**", "**/.gradle/**")
}
