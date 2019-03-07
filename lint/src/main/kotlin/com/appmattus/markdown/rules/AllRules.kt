package com.appmattus.markdown.rules

import com.appmattus.markdown.dsl.MarkdownLintConfig

class AllRules(private val config: MarkdownLintConfig) {
    private val allRules = listOf(
        BlanksAroundFencesRule(),
        BlanksAroundHeadersRule(),
        BlanksAroundListsRule(),
        CodeBlockStyleRule(),
        CommandsShowOutputRule(),
        ConsistentHeaderStyleRule(),
        ConsistentUlStyleRule(),
        FencedCodeLanguageRule(),
        FirstHeaderH1Rule(),
        FirstLineH1Rule(),
        HeaderIncrementRule(),
        HeaderStartLeftRule(),
        HrStyleRule(),
        LineLengthRule(),
        ListIndentRule(),
        ListMarkerSpaceRule(),
        LowerCaseFilenameRule(),
        NoBareUrlsRule(),
        NoBlanksBlockquoteRule(),
        NoConsecutiveHyphensFilenameRule(),
        NoDuplicateHeaderRule(),
        NoEmphasisAsHeaderRule(),
        NoEmptyLinksRule(),
        NoHardTabsRule(),
        NoInlineHtmlRule(),
        NoMissingSpaceAtxRule(),
        NoMissingSpaceClosedAtxRule(),
        NoMultipleBlanksRule(),
        NoMultipleSpaceAtxRule(),
        NoMultipleSpaceBlockquoteRule(),
        NoMissingSpaceClosedAtxRule(),
        NoPunctuationFilenameRule(),
        NoReversedLinksRule(),
        NoSpaceInCodeRule(),
        NoSpaceInEmphasisRule(),
        NoSpaceInLinksRule(),
        NoSurroundingHyphensFilenameRule(),
        NoTrailingPunctuationRule(),
        NoTrailingSpacesRule(),
        NoWhitespaceFilenameRule(),
        OlPrefixRule(),
        ProperNamesRule(),
        SingleH1Rule(),
        UlIndentRule(),
        UlStartLeftRule()
    )

    val rules: List<Rule> by lazy {
        allRules.filterNot { rule ->
            config.rules.any { it::class == rule::class }
        } + config.rules.filter {
            it.configuration.active
        }
    }
}
