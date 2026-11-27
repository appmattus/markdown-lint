import com.vanniktech.maven.publish.JavadocJar
import com.vanniktech.maven.publish.KotlinJvm
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("java-library")
    alias(libs.plugins.kotlin.jvm)

    id("jacoco")
    alias(libs.plugins.androidLintPlugin)
    alias(libs.plugins.gradleMavenPublishPlugin)
}

version = System.getenv("GITHUB_REF")?.substring(10) ?: System.getProperty("GITHUB_REF")?.substring(10) ?: "unknown"
group = "com.appmattus.markdownlint"

// Published to Maven Central alongside core, as a Spotless step for markdown-lint
mavenPublishing {
    configure(KotlinJvm(javadocJar = JavadocJar.Empty(), sourcesJar = true))
    coordinates("com.appmattus.markdownlint", "spotless", version.toString())

    pom {
        name.set("com.appmattus.markdownlint:spotless")
        description.set("A Spotless step that reports markdown-lint errors as Spotless lints")
    }
}

dependencies {
    implementation(libs.kotlin.stdlib)

    api(project(":core"))

    // Provided by the Spotless plugin that applies the step
    compileOnly(libs.spotless.lib)

    testImplementation(libs.spotless.lib)
    testImplementation(libs.kotlin.test)
    testImplementation(libs.kotlin.testJunit)
    testImplementation(libs.junit)
    testImplementation(libs.assertj.core)
}

tasks.withType<Test> {
    useJUnit()

    testLogging {
        events(TestLogEvent.SKIPPED, TestLogEvent.FAILED)
        exceptionFormat = TestExceptionFormat.SHORT
    }
}

tasks.getByName("test").finalizedBy(tasks.getByName("jacocoTestReport"))

tasks.getByName("check").finalizedBy(rootProject.tasks.getByName("detekt"))

jacoco {
    toolVersion = libs.versions.jacoco.get()
}

tasks.withType<JacocoReport> {
    reports {
        xml.required.set(true)
        html.required.set(true)
    }
}

// Spotless itself requires Java 17
java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
    }
}
