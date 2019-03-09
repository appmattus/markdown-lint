import com.appmattus.markdown.dsl.markdownlint
import com.appmattus.markdown.rules.ConsistentHeaderStyleRule
import com.appmattus.markdown.rules.FirstHeaderH1Rule

markdownlint {
    rules {
        +FirstHeaderH1Rule(level = 5) {
            active = false
        }
        +ConsistentHeaderStyleRule {
            active = true
        }
    }

    reports {
        html()
        checkstyle()
    }

    // file extensions .md & .markdown
}

// type safe configuration of rules
// suppression
// generate
//     html
//     checkstyle


/*

markdownlint {
    rules {
        firstHeaderH1(level = 5) {
            suppress {
                file("")
            }
            active = false
        }
        headerStyle {
            active = false
        }
    }

    reports {
        html, checkstyle
    }
}

 */
