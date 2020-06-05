# markdown-lint for Gradle projects

[![CircleCI](https://circleci.com/gh/appmattus/markdown-lint/tree/master.svg?style=svg)](https://circleci.com/gh/appmattus/markdown-lint/tree/master)
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
