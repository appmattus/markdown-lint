/*
 * Copyright 2020 Appmattus Limited
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("java-gradle-plugin")
    kotlin("jvm")

    jacoco
    id("com.github.kt3k.coveralls")
    id("com.gradle.plugin-publish") version "0.12.0"
    id("pl.droidsonroids.jacoco.testkit") version "1.0.7"
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

    api("com.vladsch.flexmark:flexmark-ext-tables:0.50.50")
    api("com.vladsch.flexmark:flexmark-ext-gfm-strikethrough:0.50.50")
    api("com.vladsch.flexmark:flexmark-ext-autolink:0.50.50")
    api("com.vladsch.flexmark:flexmark-ext-gfm-tasklist:0.50.50")

    testImplementation(kotlin("test"))
    testImplementation(gradleTestKit())
    testImplementation("junit:junit:4.13")
    testImplementation("org.assertj:assertj-core:3.16.1")
    testImplementation("org.mockito:mockito-core:3.4.4")
    testImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:2.2.0")
    testImplementation("io.github.classgraph:classgraph:4.8.87")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.2")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.6.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.6.2")

    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.3.8")
}

tasks.withType<Test> {
    useJUnitPlatform {
        includeEngines = setOf("junit-jupiter")
    }

    testLogging {
        events(TestLogEvent.SKIPPED, TestLogEvent.FAILED)
        exceptionFormat = TestExceptionFormat.SHORT
    }
}

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

val jacocoTask = tasks.named("jacocoTestReport") {
    finalizedBy(tasks.named("coveralls"))
}

tasks.named("check") {
    finalizedBy(jacocoTask)
    finalizedBy(rootProject.tasks.named("detekt"))
}

tasks.named("coveralls") { onlyIf { System.getenv("CI")?.isNotEmpty() == true } }

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
    kotlinOptions.freeCompilerArgs += "-Xopt-in=kotlin.RequiresOptIn"
    kotlinOptions.freeCompilerArgs += "-Xopt-in=kotlin.contracts.ExperimentalContracts"
}

lintOptions {
    disable("GradleDependency")
}
