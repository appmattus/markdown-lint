package com.appmattus.markdown.rules

import kotlin.test.Test

class ValidRelativeImagesRuleTest {
    @Test
    fun `all files`() = fileRuleScenario { ValidRelativeImagesRule() }
}
