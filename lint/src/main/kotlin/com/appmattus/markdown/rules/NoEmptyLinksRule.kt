package com.appmattus.markdown.rules

import com.appmattus.markdown.MarkdownDocument
import com.appmattus.markdown.Rule
import com.appmattus.markdown.RuleSetup
import com.appmattus.markdown.rules.extentions.referenceUrl
import com.vladsch.flexmark.ast.LinkRef
import com.vladsch.flexmark.ast.Reference

class NoEmptyLinksRule(override val config: RuleSetup.Builder.() -> Unit = {}) : Rule("NoEmptyLinks") {

    override val description = "No empty links"
    override val tags = listOf("links")

    private val emptyLinkRegex = Regex("#?|(?:<>)")

    override fun visitDocument(document: MarkdownDocument) {
        document.allLinks.forEach { link ->

            when (link) {
                is LinkRef -> link.referenceUrl()?.toString() ?: ""
                is Reference -> null
                else -> link.url.toString()
            }?.let { url ->
                if (url.matches(emptyLinkRegex)) {
                    reportError(link.startOffset, link.endOffset, description)
                }
            }
        }
    }
}

/*
module.exports = {
  "names": [ "MD042", "no-empty-links" ],
  "description": "No empty links",
  "tags": [ "links" ],
  "function": function MD042(params, onError) {
    shared.filterTokens(params, "inline", function forToken(token) {
      let inLink = false;
      let linkText = "";
      let emptyLink = false;
      token.children.forEach(function forChild(child) {
        if (child.type === "link_open") {
          inLink = true;
          linkText = "";
          child.attrs.forEach(function forAttr(attr) {
            if (attr[0] === "href" && (!attr[1] || (attr[1] === "#"))) {
              emptyLink = true;
            }
          });
        } else if (child.type === "link_close") {
          inLink = false;
          if (emptyLink) {
            shared.addErrorContext(onError, child.lineNumber,
              "[" + linkText + "]()", null, null,
              shared.rangeFromRegExp(child.line, emptyLinkRe));
          }
        } else if (inLink) {
          linkText += child.content;
        }
      });
    });
  }
};
 */
