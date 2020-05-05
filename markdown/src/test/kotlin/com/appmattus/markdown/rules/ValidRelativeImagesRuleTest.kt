package com.appmattus.markdown.rules

import org.junit.jupiter.api.TestFactory

class ValidRelativeImagesRuleTest {

    @TestFactory
    fun validRelativeImagesRule() = FileTestFactory { ValidRelativeImagesRule() }
}
