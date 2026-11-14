import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import io.gitlab.arturbosch.detekt.Detekt

// Top-level build file where you can add configuration options common to all sub-projects/modules.

plugins {
    kotlin("jvm") version "2.4.21" apply false
    id("com.github.kt3k.coveralls") version "2.12.2" apply false
    id("com.android.lint") version "9.5.1" apply false
    id("io.github.ben-manes.versions") version "0.67.0"
    id("io.gitlab.arturbosch.detekt") version "1.23.8"
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
    detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.23.8")
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
