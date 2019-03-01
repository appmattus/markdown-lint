import com.appmattus.markdown.markdownlint
import com.appmattus.markdown.rules.MD002
import com.appmattus.markdown.rules.MD003

markdownlint {
    rules {
        MD002(level = 5) {
            active = false
        }
        MD003 {
            active = true
        }
    }

    reports {
        html()
        checkstyle()
    }
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
