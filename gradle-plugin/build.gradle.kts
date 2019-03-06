import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.gradle.jvm.tasks.Jar

plugins {
    `kotlin-dsl`
}

gradlePlugin {
    plugins {
        register("markdownlint") {
            id = "markdownlint"
            implementationClass = "com.appmattus.markdown.plugin.MarkdownLintPlugin"
        }
    }
}

dependencies {
    compileOnly(gradleApi())
    implementation(kotlin("stdlib-jdk8"))
    implementation(project(":lint"))

    //implementation("com.puppycrawl.tools:checkstyle:8.18")

    //implementation(kotlin("compiler"))
    //implementation(kotlin("script-util"))
    //implementation(kotlin("script-runtime"))

    //implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.1.1")
    //implementation("com.vladsch.flexmark:flexmark-all:0.40.20")

    testImplementation(kotlin("test"))
    testImplementation(gradleTestKit())
    testImplementation("junit:junit:4.12")
    testImplementation("org.assertj:assertj-core:3.12.1")
    testImplementation("org.mockito:mockito-core:2.24.5")
    testImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:2.1.0")
    testImplementation("com.flextrade.jfixture:jfixture:2.7.2")

    //testImplementation("org.junit.platform:junit-platform-engine:1.4.0")
    testImplementation("org.spekframework.spek2:spek-dsl-jvm:2.0.1") /*{
        exclude(group = "org.jetbrains.kotlin")
    }*/
    testRuntimeOnly("org.spekframework.spek2:spek-runner-junit5:2.0.1") /*{
        exclude(group = "org.junit.platform")
        exclude(group = "org.jetbrains.kotlin")
    }*/

    // spek requires kotlin-reflect, can be omitted if already in the classpath
    testRuntimeOnly(kotlin("reflect"))
}

tasks.withType<Jar> {
    manifest {
        attributes(
            "Manifest-Version" to 1.0,
            "Lint-Registry-v2" to "com.appmattus.markdown.MarkdownIssueRegistry"
        )
    }
}

//sourceCompatibility = "1.7"
//targetCompatibility = "1.7"


tasks.withType<Test> {
    useJUnitPlatform {
        includeEngines = setOf("spek2")
    }

    testLogging {
        events(TestLogEvent.PASSED, TestLogEvent.SKIPPED, TestLogEvent.FAILED)
        exceptionFormat = TestExceptionFormat.SHORT
    }
}
