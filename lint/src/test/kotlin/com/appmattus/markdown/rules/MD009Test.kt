package com.appmattus.markdown.rules

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

object MD009Test : Spek({
    Feature("MD009") {

        FileRuleScenario(listOf("trailing_spaces_br.md")) { MD009(brSpaces = 2) }

        /*FileRuleScenario(listOf("spaces_after_list_marker.md")) { MD007(indent = 4) }*/

        FileRuleScenario(exclude = listOf("trailing_spaces_br.md")) { MD009() }
    }
})
