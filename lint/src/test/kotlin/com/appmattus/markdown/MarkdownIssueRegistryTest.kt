package com.appmattus.markdown

import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

class MarkdownIssueRegistryTest {

    private lateinit var markdownIssueRegistry: MarkdownIssueRegistry

    /**
     * Setup for the other test methods
     */
    @Before
    fun setUp() {
        markdownIssueRegistry = MarkdownIssueRegistry()
    }

    /**
     * Test that the Issue Registry contains the correct number of Issues
     */
    @Test
    fun testNumberOfIssues() {
        assertThat(markdownIssueRegistry.issues.size).isEqualTo(1)
    }

    /**
     * Test that the Issue Registry contains the correct Issues
     */
    @Test
    fun testGetIssues() {
        assertThat(markdownIssueRegistry.issues).contains(MarkdownDetector.ISSUE)
    }
}
