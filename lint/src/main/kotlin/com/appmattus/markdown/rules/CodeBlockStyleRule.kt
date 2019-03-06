package com.appmattus.markdown.rules

import com.appmattus.markdown.ErrorReporter
import com.appmattus.markdown.MarkdownDocument
import com.appmattus.markdown.Rule
import com.appmattus.markdown.RuleSetup
import com.appmattus.markdown.rules.config.CodeBlockStyle
import com.vladsch.flexmark.ast.FencedCodeBlock
import com.vladsch.flexmark.ast.IndentedCodeBlock
import com.vladsch.flexmark.util.ast.Block

class CodeBlockStyleRule(
    private val style: CodeBlockStyle = CodeBlockStyle.Fenced,
    override val config: RuleSetup.Builder.() -> Unit = {}
) : Rule("CodeBlockStyle") {

    override val description = "Code block style"
    override val tags = listOf("code")

    override fun visitDocument(document: MarkdownDocument, errorReporter: ErrorReporter) {
        val codeBlocks = document.codeBlocks

        if (codeBlocks.isEmpty()) {
            return
        }

        val mainStyle = when (style) {
            CodeBlockStyle.Consistent -> when (codeBlocks.first()) {
                is FencedCodeBlock -> CodeBlockStyle.Fenced
                is IndentedCodeBlock -> CodeBlockStyle.Indented
                else -> return
            }
            else -> style
        }

        codeBlocks.forEach {
            if (!it.matchesStyle(mainStyle)) {
                errorReporter.reportError(it.startOffset, it.endOffset, description)
            }
        }
    }

    private fun Block.matchesStyle(style: CodeBlockStyle): Boolean {
        return when (style) {
            CodeBlockStyle.Fenced -> this is FencedCodeBlock
            CodeBlockStyle.Indented -> this is IndentedCodeBlock
            else -> false
        }
    }
}

/*
rule "MD046", "Code block style" do
  tags :code
  aliases 'code-block-style'
  params :style => :fenced
  check do |doc|
    style = @params[:style]
    doc.element_linenumbers(
      doc.find_type_elements(:codeblock).select do |i|
        # for consistent we determine the first one
        if style == :consistent
          if doc.element_line(i).start_with?("    ")
            style = :indented
          else
            style = :fenced
          end
        end
        if @params[:style] == :fenced
          doc.element_line(i).start_with?("    ")
        else
          !doc.element_line(i).start_with?("    ")
        end
      end
    )
  end
end
 */
