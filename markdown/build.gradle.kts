import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("java-gradle-plugin")
    alias(libs.plugins.kotlin.jvm)

    id("jacoco")
    alias(libs.plugins.coverallsPlugin)
    alias(libs.plugins.gradlePluginPublishPlugin)
    alias(libs.plugins.jacocoTestKitPlugin)
    alias(libs.plugins.androidLintPlugin)
}

gradlePlugin {
    website = "https://github.com/appmattus/markdown-lint"
    vcsUrl = "https://github.com/appmattus/markdown-lint.git"

    plugins {
        create("markdownlint") {
            id = "com.appmattus.markdown"
            displayName = "markdownlint"
            description = "Linting for markdown files"
            implementationClass = "com.appmattus.markdown.plugin.MarkdownLintPlugin"
            tags = listOf("markdown", "lint", "format", "style")
        }
    }
}

version = System.getenv("GITHUB_REF")?.substring(10) ?: System.getProperty("GITHUB_REF")?.substring(10) ?: "unknown"
group = "com.appmattus"

dependencies {
    compileOnly(gradleApi())

    implementation(libs.kotlin.stdlib)

    api(libs.flexmark.extTables)
    api(libs.flexmark.extGfmStrikethrough)
    api(libs.flexmark.extAutolink)
    api(libs.flexmark.extGfmTasklist)

    testImplementation(libs.kotlin.test)
    testImplementation(libs.kotlin.testJunit)
    testImplementation(gradleTestKit())
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
