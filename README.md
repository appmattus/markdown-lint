# markdown-lint for Gradle projects

[![Build](https://github.com/appmattus/markdown-lint/actions/workflows/android.yml/badge.svg)](https://github.com/appmattus/markdown-lint/actions/workflows/android.yml)
[![Coverage Status](https://coveralls.io/repos/github/appmattus/markdown-lint/badge.svg?branch=master)](https://coveralls.io/github/appmattus/markdown-lint?branch=master)
[![Plugin Portal](https://img.shields.io/maven-metadata/v/https/plugins.gradle.org/m2/com/appmattus/markdown/com.appmattus.markdown.gradle.plugin/maven-metadata.xml.svg?label=plugin%20portal)](https://plugins.gradle.org/plugin/com.appmattus.markdown)

Linting for markdown files

## Getting started

Apply the plugin in your `build.gradle.kts` script. Further instructions on the
[plugin page](https://plugins.gradle.org/plugin/com.appmattus.markdown).

```kotlin
plugins {
  id("com.appmattus.markdown") version "<latestVersion>"
}
```

Run the plugin with:

```bash
./gradlew markdownlint
```

or by adding a dependency to the task:

```kotlin
tasks.named("check") {
    dependsOn(tasks.named("markdownlint"))
}
```

To customise the rules and report generation specify the configuration in
your `build.gradle.kts` script:

```kotlin
import com.appmattus.markdown.rules.config.HeaderStyle
import com.appmattus.markdown.rules.ConsistentHeaderStyleRule
import com.appmattus.markdown.rules.SingleH1Rule

markdownlint {
    // Optional - Use to override settings for individual rules or add in custom rules
    rules {
        // Change the default settings of a rule
        +ConsistentHeaderStyleRule(HeaderStyle.Atx)

        +NoDuplicateHeaderRule {
            // Include files using RegEx for this rule, default=".*" (i.e. all files)
            includes = listOf(".*/directory_to_include/.*")

            // Exclude files using RegEx for this rule, default=empty (i.e. exclude nothing)
            excludes = listOf(".*/a_file_to_exclude.md")
        }

        // Disable a rule by setting active to false
        +SingleH1Rule {
            active = false
        }
    }
    
    // Optional - Include files using RegEx for ALL rules, default=".*" (i.e. all files in root project directory)
    includes = listOf(".*/directory_to_include/.*")

    // Optional - Exclude files using RegEx for ALL rules, default=empty (i.e. exclude nothing)
    excludes = listOf(".*/a_file_to_exclude.md")

    // Optional - Specify the reports to generate, default=html,checkstyle
    // If you specify the reports block the plugin will output only the types
    // specified i.e. to output no reports implement an empty block
    reports {
        // enable html report
        html()

        // enable checkstyle xml report
        checkstyle()
    }

    // Optional - Specify the error count threshold that triggers a failed build, default=0
    threshold = 10
}
```

### Writing a custom rule

You can keep custom rules in a local `markdownRules` module and add them to the
build-script classpath without publishing a JAR or changing the plugin. Use a
[Gradle included build](https://docs.gradle.org/current/userguide/composite_builds.html)
so Gradle builds the rules before compiling the consuming build script.

Create this directory structure:

```text
.
├── settings.gradle.kts
├── build.gradle.kts
└── markdownRules/
    ├── settings.gradle.kts
    ├── build.gradle.kts
    └── src/main/kotlin/com/example/markdown/NoTodoRule.kt
```

Add the included build to your root `settings.gradle.kts`:

```kotlin
includeBuild("markdownRules")
```

Use `includeBuild("markdownRules")`, rather than `include(":markdownRules")`:
the rules are build logic needed to compile your build script, so they must be
built independently of the consuming project's configuration.

In `markdownRules/settings.gradle.kts`, give the build its dependency name:

```kotlin
rootProject.name = "markdownRules"
```

In `markdownRules/build.gradle.kts`, compile against the analyzer API. Replace
`<kotlinVersion>` with a compatible Kotlin version, and `<latestVersion>` with
the same markdown-lint version used by your consuming build:

```kotlin
plugins {
    kotlin("jvm") version "<kotlinVersion>"
}

group = "com.example"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("com.appmattus.markdownlint:core:<latestVersion>")
}
```

Use `compileOnly` because the Markdown plugin supplies the analyzer at runtime.
The included build has its own settings and repositories; it does not inherit
those from your root build.

Create `markdownRules/src/main/kotlin/com/example/markdown/NoTodoRule.kt`:

```kotlin
package com.example.markdown

import com.appmattus.markdown.dsl.RuleSetup
import com.appmattus.markdown.errors.ErrorReporter
import com.appmattus.markdown.processing.MarkdownDocument
import com.appmattus.markdown.rules.Rule

class NoTodoRule(config: RuleSetup.Builder.() -> Unit = {}) : Rule(config) {
    override fun visitDocument(document: MarkdownDocument, errorReporter: ErrorReporter) {
        Regex("\\bTODO\\b").findAll(document.chars.toString()).forEach { match ->
            errorReporter.reportError(
                match.range.first,
                match.range.last + 1,
                "Resolve TODO markers before publishing."
            )
        }
    }
}
```

This example checks the raw Markdown text, including code blocks. Extend `Rule`,
implement `visitDocument` and report problems with the `ErrorReporter`. Passing
`config` to `Rule` lets users set `active`, `includes` and `excludes` as they would
for a built-in rule; the base class handles configuration-cache compatibility.

Finally, add the dependency and register the rule in your root `build.gradle.kts`:

```kotlin
import com.example.markdown.NoTodoRule

buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("com.example:markdownRules:1.0")
    }
}

plugins {
    id("com.appmattus.markdown") version "<latestVersion>"
}

markdownlint {
    rules {
        +NoTodoRule {
            excludes = listOf(".*/drafts/.*")
        }
    }
}
```

Gradle substitutes `com.example:markdownRules` with the included build using its
`group` and `rootProject.name`, and builds the rules automatically. Maven Central
resolves external dependencies such as the Kotlin standard library; the rules
themselves are loaded locally. No separate build or publish step is required.
Use these coordinates instead of `project(":markdownRules")`, since included
builds are separate from the root project's subprojects.

Adding the classpath dependency makes the rule available to import; adding
`+NoTodoRule { ... }` enables it alongside the built-in rules. No service provider
file is needed. Run the checks as usual:

```bash
./gradlew markdownlint
```

### Groovy build scripts

In a Groovy `build.gradle` script, add rules with `rule(...)` instead of `+`,
and pass any rule settings as a closure after it:

```groovy
import com.appmattus.markdown.rules.config.HeaderStyle
import com.appmattus.markdown.rules.ConsistentHeaderStyleRule
import com.appmattus.markdown.rules.NoDuplicateHeaderRule
import com.appmattus.markdown.rules.SingleH1Rule

markdownlint {
    // Optional - Use to override settings for individual rules or add in custom rules
    rules {
        // Change the default settings of a rule
        rule(new ConsistentHeaderStyleRule(HeaderStyle.Atx))

        rule(new NoDuplicateHeaderRule()) {
            // Include files using RegEx for this rule, default=".*" (i.e. all files)
            includes = ['.*/directory_to_include/.*']

            // Exclude files using RegEx for this rule, default=empty (i.e. exclude nothing)
            excludes = ['.*/a_file_to_exclude.md']
        }

        // Disable a rule by setting active to false
        rule(new SingleH1Rule()) {
            active = false
        }
    }

    // Optional - Include files using RegEx for ALL rules, default=".*" (i.e. all files in root project directory)
    includes = ['.*/directory_to_include/.*']

    // Optional - Exclude files using RegEx for ALL rules, default=empty (i.e. exclude nothing)
    excludes = ['.*/a_file_to_exclude.md']

    // Optional - Specify the reports to generate, default=html,checkstyle
    reports {
        html()
        checkstyle()
    }

    // Optional - Specify the error count threshold that triggers a failed build, default=0
    threshold = 10
}
```

Pass rule settings in the closure after `rule(...)` rather than to the rule's
constructor, so that `includes`, `excludes` and `active` apply to that rule.

## Spotless

To report markdown-lint errors from [Spotless](https://github.com/diffplug/spotless),
add the markdown-lint step to the build script classpath and to a Spotless
format:

```kotlin
import com.appmattus.markdown.spotless.MarkdownLintStep

buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("com.appmattus.markdownlint:spotless:<latestVersion>")
    }
}

plugins {
    id("com.diffplug.spotless") version "<spotlessVersion>"
}

spotless {
    format("markdown") {
        target("**/*.md", "**/*.markdown")
        // Add it after any steps that change markdown files, so it checks their final content
        addStep(MarkdownLintStep.create())

        // Optional - Suppress a rule, using its class name
        suppressLintsFor {
            step = MarkdownLintStep.NAME
            shortCode = "SingleH1Rule"
        }
    }
}
```

`MarkdownLintStep.create()` uses the built-in rules with their default
settings. To configure rules, pass the project directory and a `rules` block,
as in the plugin's configuration. Each rule's `includes` and `excludes` are
matched against the project directory:

```kotlin
addStep(MarkdownLintStep.create(projectDir) {
    // Change the default settings of a rule
    +LineLengthRule(lineLength = 120)

    // Limit or disable a rule
    +SingleH1Rule {
        excludes = listOf(".*/docs/.*")
    }
    +NoBareUrlsRule {
        active = false
    }

    // Add a custom rule
    +NoTodoRule()
})
```

In a Groovy build script, pass the rules as a list:

```groovy
addStep(MarkdownLintStep.create(projectDir, [
    new LineLengthRule(120),
    new SingleH1Rule(1, { it.excludes = ['.*/docs/.*'] })
]))
```

Spotless stores the rules, so custom rules used with the step must take their
setup through the `Rule` constructor, as shown in
[Writing a custom rule](#writing-a-custom-rule), and any fields they add must be
serializable.

The step never changes files. `spotlessCheck` and `spotlessApply` fail while
there are errors, which are reported with the file, line, rule and column:

```text
README.md:L3 markdownlint(SingleH1Rule) Multiple top level 1 headers in the same document, … (column 1)
```

Errors can be suppressed by rule, path or for the whole step with
`suppressLintsFor`. There's no equivalent of the plugin's `threshold`.

## Using the analyzer without Gradle

The rules and processor are published separately to Maven Central, so they can
be used from other tools or to write custom rules:

```kotlin
dependencies {
    implementation("com.appmattus.markdownlint:core:<latestVersion>")
}
```

```kotlin
import com.appmattus.markdown.dsl.Config
import com.appmattus.markdown.dsl.RulesBuilder
import com.appmattus.markdown.processing.RuleProcessor
import com.appmattus.markdown.rules.SingleH1Rule
import java.nio.file.Paths

val config = Config(
    // Optional - Override settings for individual rules, as in the plugin's rules block
    rules = RulesBuilder().apply {
        +SingleH1Rule {
            active = false
        }
    }.build()
)

// Analyses markdown files under the root directory, writes the reports and prints a summary.
// Throws BuildFailure when the error count reaches the configured threshold.
RuleProcessor(rootDir = Paths.get("."), reportsDir = Paths.get("build/reports/markdownlint"))
    .process(config, System.out)
```

To lint content you already have, without finding files or writing reports,
use `MarkdownLinter`. The file path is used for filename rules and to match each
rule's `includes` and `excludes` against the root directory, but isn't read:

```kotlin
import com.appmattus.markdown.processing.MarkdownLinter

val linter = MarkdownLinter(rootDir = Paths.get("."), config = config)

linter.lint(Paths.get("docs/guide.md"), content).forEach { error ->
    println("${error.ruleClass.simpleName} at ${error.lineNumber}:${error.columnNumber}: ${error.errorMessage}")
}
```

## Contributing

Please fork this repository and contribute back using [pull requests](https://github.com/appmattus/markdown-lint/pulls).

All contributions, large or small, major features, bug fixes, additional
language translations, unit/integration tests are welcomed.

## License

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](LICENSE)

Copyright 2019 Appmattus Limited

Licensed under the Apache License, Version 2.0 (the "License"); you may not use
this file except in compliance with the License. You may obtain a copy of the
License at [http://www.apache.org/licenses/LICENSE-2.0](http://www.apache.org/licenses/LICENSE-2.0)

Unless required by applicable law or agreed to in writing, software distributed
under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
CONDITIONS OF ANY KIND, either express or implied. See the License for the
specific language governing permissions and limitations under the License.
