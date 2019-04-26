import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("java-gradle-plugin")
    kotlin("jvm")

    id("jacoco")
    id("com.github.kt3k.coveralls")
    id("com.gradle.plugin-publish") version "0.10.1"
    id("pl.droidsonroids.jacoco.testkit") version "1.0.3"
    id("com.android.lint")
}

gradlePlugin {
    plugins {
        create("markdownlint") {
            id = "com.appmattus.markdown"
            displayName = "markdownlint"
            description = "Linting for markdown files"
            implementationClass = "com.appmattus.markdown.plugin.MarkdownLintPlugin"
        }
    }
}

pluginBundle {
    website = "https://github.com/appmattus/markdown-lint"
    vcsUrl = "https://github.com/appmattus/markdown-lint.git"
    tags = listOf("markdown", "lint", "format", "style")
}

version = System.getenv("CIRCLE_TAG") ?: System.getProperty("CIRCLE_TAG") ?: "unknown"
group = "com.appmattus"

dependencies {
    compileOnly(gradleApi())

    implementation(kotlin("stdlib-jdk8"))

    api("com.vladsch.flexmark:flexmark-ext-tables:0.42.6")
    api("com.vladsch.flexmark:flexmark-ext-gfm-strikethrough:0.42.6")
    api("com.vladsch.flexmark:flexmark-ext-autolink:0.42.6")
    api("com.vladsch.flexmark:flexmark-ext-gfm-tasklist:0.42.6")

    testImplementation(kotlin("test"))
    testImplementation(gradleTestKit())
    testImplementation("junit:junit:4.12")
    testImplementation("org.assertj:assertj-core:3.12.2")
    testImplementation("org.mockito:mockito-core:2.27.0")
    testImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:2.1.0")
    testImplementation("com.flextrade.jfixture:jfixture:2.7.2")
    testImplementation("io.github.classgraph:classgraph:4.8.26")

    testImplementation("org.spekframework.spek2:spek-dsl-jvm:2.0.2")
    testRuntimeOnly("org.spekframework.spek2:spek-runner-junit5:2.0.2")
    // spek requires kotlin-reflect, can be omitted if already in the classpath
    testRuntimeOnly(kotlin("reflect"))
}

tasks.withType<Test> {
    useJUnitPlatform {
        includeEngines = setOf("spek2")
    }

    testLogging {
        events(TestLogEvent.PASSED, TestLogEvent.SKIPPED, TestLogEvent.FAILED)
        exceptionFormat = TestExceptionFormat.SHORT
    }
}

tasks.getByName("test").finalizedBy(tasks.getByName("jacocoTestReport"))

tasks.getByName("check").finalizedBy(rootProject.tasks.getByName("detekt"))

tasks.withType<JacocoReport> {
    reports {
        xml.isEnabled = true
        html.isEnabled = true
    }
}

coveralls {
    sourceDirs = sourceSets.main.get().allSource.srcDirs.map { it.path }
    jacocoReportPath = "$buildDir/reports/jacoco/test/jacocoTestReport.xml"
}

tasks.getByName("jacocoTestReport").finalizedBy(tasks.getByName("coveralls"))

tasks.getByName("coveralls").onlyIf { System.getenv("CI")?.isNotEmpty() == true }

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}
