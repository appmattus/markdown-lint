import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask

// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.50")
        classpath("org.kt3k.gradle.plugin:coveralls-gradle-plugin:2.8.3")
        classpath("com.android.tools.build:gradle:3.5.0")
    }
}

plugins {
    id("com.github.ben-manes.versions") version "0.25.0"
    id("io.gitlab.arturbosch.detekt") version "1.0.1"
}

allprojects {
    repositories {
        google()
        jcenter()
        mavenCentral()
    }
}

task("clean", type = Delete::class) {
    delete(rootProject.buildDir)
}

tasks.withType(DependencyUpdatesTask::class.java).all {
    resolutionStrategy {
        componentSelection {
            all {
                val rejected = listOf("alpha", "beta", "rc", "cr", "m", "preview")
                    .map { qualifier -> Regex("(?i).*[.-]$qualifier[.\\d-]*") }
                    .any { it.matches(candidate.version) }

                if (rejected) {
                    reject("Release candidate")
                }
            }
        }
    }
}

detekt {
    input = files("$projectDir")

    buildUponDefaultConfig = true

    // To override MaxLineLength:excludeCommentStatements
    config = files("detekt-config.yml")
}
