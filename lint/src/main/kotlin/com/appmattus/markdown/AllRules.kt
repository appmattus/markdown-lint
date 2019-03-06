package com.appmattus.markdown

import com.appmattus.markdown.rules.BlanksAroundFencesRule
import com.appmattus.markdown.rules.BlanksAroundHeadersRule
import com.appmattus.markdown.rules.BlanksAroundListsRule
import com.appmattus.markdown.rules.CodeBlockStyleRule
import com.appmattus.markdown.rules.CommandsShowOutputRule
import com.appmattus.markdown.rules.ConsistentHeaderStyleRule
import com.appmattus.markdown.rules.ConsistentUlStyleRule
import com.appmattus.markdown.rules.FencedCodeLanguageRule
import com.appmattus.markdown.rules.FirstHeaderH1Rule
import com.appmattus.markdown.rules.FirstLineH1Rule
import com.appmattus.markdown.rules.HeaderIncrementRule
import com.appmattus.markdown.rules.HeaderStartLeftRule
import com.appmattus.markdown.rules.HrStyleRule
import com.appmattus.markdown.rules.LineLengthRule
import com.appmattus.markdown.rules.ListIndentRule
import com.appmattus.markdown.rules.ListMarkerSpaceRule
import com.appmattus.markdown.rules.LowerCaseFilenameRule
import com.appmattus.markdown.rules.NoBareUrlsRule
import com.appmattus.markdown.rules.NoBlanksBlockquoteRule
import com.appmattus.markdown.rules.NoConsecutiveHyphensFilenameRule
import com.appmattus.markdown.rules.NoDuplicateHeaderRule
import com.appmattus.markdown.rules.NoEmphasisAsHeaderRule
import com.appmattus.markdown.rules.NoEmptyLinksRule
import com.appmattus.markdown.rules.NoHardTabsRule
import com.appmattus.markdown.rules.NoInlineHtmlRule
import com.appmattus.markdown.rules.NoMissingSpaceAtxRule
import com.appmattus.markdown.rules.NoMissingSpaceClosedAtxRule
import com.appmattus.markdown.rules.NoMultipleBlanksRule
import com.appmattus.markdown.rules.NoMultipleSpaceAtxRule
import com.appmattus.markdown.rules.NoMultipleSpaceBlockquoteRule
import com.appmattus.markdown.rules.NoPunctuationFilenameRule
import com.appmattus.markdown.rules.NoReversedLinksRule
import com.appmattus.markdown.rules.NoSpaceInCodeRule
import com.appmattus.markdown.rules.NoSpaceInEmphasisRule
import com.appmattus.markdown.rules.NoSpaceInLinksRule
import com.appmattus.markdown.rules.NoSurroundingHyphensFilenameRule
import com.appmattus.markdown.rules.NoTrailingPunctuationRule
import com.appmattus.markdown.rules.NoTrailingSpacesRule
import com.appmattus.markdown.rules.NoWhitespaceFilenameRule
import com.appmattus.markdown.rules.OlPrefixRule
import com.appmattus.markdown.rules.ProperNamesRule
import com.appmattus.markdown.rules.SingleH1Rule
import com.appmattus.markdown.rules.UlIndentRule
import com.appmattus.markdown.rules.UlStartLeftRule

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
