import com.vanniktech.maven.publish.JavadocJar
import com.vanniktech.maven.publish.KotlinJvm
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("java-library")
    alias(libs.plugins.kotlin.jvm)

    id("jacoco")
    alias(libs.plugins.coverallsPlugin)
    alias(libs.plugins.androidLintPlugin)
    alias(libs.plugins.gradleMavenPublishPlugin)
}

version = System.getenv("GITHUB_REF")?.substring(10) ?: System.getProperty("GITHUB_REF")?.substring(10) ?: "unknown"
group = "com.appmattus.markdownlint"

// Published to Maven Central so the analyzer can be used without the Gradle plugin, and so the plugin can resolve it
mavenPublishing {
    configure(KotlinJvm(javadocJar = JavadocJar.Empty(), sourcesJar = true))
    coordinates("com.appmattus.markdownlint", "core", version.toString())

    pom {
        name.set("com.appmattus.markdownlint:core")
        description.set("Markdown linting rules and processor used by the com.appmattus.markdown Gradle plugin")
    }
}

// Lets tools such as the Spotless step tell which version of the rules they're running
tasks.jar {
    manifest {
        attributes("Implementation-Version" to project.version)
    }
}

dependencies {
    implementation(libs.kotlin.stdlib)

    api(libs.flexmark.extTables)
    api(libs.flexmark.extGfmStrikethrough)
    api(libs.flexmark.extAutolink)
    api(libs.flexmark.extGfmTasklist)

    testImplementation(libs.kotlin.test)
    testImplementation(libs.kotlin.testJunit)
    testImplementation(libs.junit)
    testImplementation(libs.assertj.core)
    testImplementation(libs.jfixture)
    testImplementation(libs.jimfs)
    testImplementation(libs.classgraph)
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

coveralls {
    sourceDirs = sourceSets.main.get().allSource.srcDirs.map { it.path }
    jacocoReportPath = layout.buildDirectory.file("reports/jacoco/test/jacocoTestReport.xml").get().asFile.path
}

tasks.getByName("jacocoTestReport").finalizedBy(tasks.getByName("coveralls"))

tasks.getByName("coveralls").onlyIf { System.getenv("CI")?.isNotEmpty() == true }

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_1_8)
    }
}
