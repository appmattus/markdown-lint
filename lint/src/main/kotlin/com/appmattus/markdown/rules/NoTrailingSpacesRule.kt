package com.appmattus.markdown.rules

import com.appmattus.markdown.MarkdownDocument
import com.appmattus.markdown.Rule
import com.appmattus.markdown.RuleSetup

class NoTrailingSpacesRule(
    private val brSpaces: Int = 2,
    override val config: RuleSetup.Builder.() -> Unit = {}
) : Rule("NoTrailingSpaces") {

    override val description = "Trailing spaces"
    override val tags = listOf("whitespace")

    override fun visitDocument(document: MarkdownDocument) {
        document.lines.forEach {
            if (it.endsWith(" ")) {
                val actual = it.length - it.trimEnd().length

                if (actual != 0 && actual != brSpaces) {
                    reportError(it.startOffset, it.endOffset, description)
                }
            }
        }
    }
}

/*
rule "MD009", "Trailing spaces" do
  tags :whitespace
  aliases 'no-trailing-spaces'
  params :br_spaces => 0
  check do |doc|
    errors = doc.matching_lines(/\s$/)
    if params[:br_spaces] > 1
      errors -= doc.matching_lines(/\S\s{#{params[:br_spaces]}}$/)
    end
    errors
  end
end
 */
