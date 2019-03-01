package com.appmattus.markdown.rules

import com.appmattus.markdown.MarkdownDocument
import com.appmattus.markdown.Rule
import com.appmattus.markdown.RuleSetup
import com.appmattus.markdown.rules.extentions.indent
import com.appmattus.markdown.rules.extentions.level

class ListIndentRule(override val config: RuleSetup.Builder.() -> Unit = {}) : Rule("ListIndent") {

    override val description = "Inconsistent indentation for list items at the same level"
    override val tags = listOf("bullet", "ul", "indentation")

    override fun visitDocument(document: MarkdownDocument) {

        val bullets = document.listItems

        if (bullets.isEmpty()) {
            return
        }

        val indentLevels = mutableMapOf<Int, Int>()

        bullets.forEach { b ->

            val indentLevel = b.indent()

            if (!indentLevels.containsKey(b.level())) {
                indentLevels[b.level()] = indentLevel
            } else if (indentLevel != indentLevels[b.level()]) {
                reportError(b.startOffset, b.endOffset, description)
            }
        }
    }
}

/*
rule "MD005", "Inconsistent indentation for list items at the same level" do
  tags :bullet, :ul, :indentation
  aliases 'list-indent'
  check do |doc|
    bullets = doc.find_type(:li)
    errors = []
    indent_levels = []
    bullets.each do |b|
      indent_level = doc.indent_for(doc.element_line(b))
      if indent_levels[b[:element_level]].nil?
        indent_levels[b[:element_level]] = indent_level
      end
      if indent_level != indent_levels[b[:element_level]]
        errors << doc.element_linenumber(b)
      end
    end
    errors
  end
end
 */
