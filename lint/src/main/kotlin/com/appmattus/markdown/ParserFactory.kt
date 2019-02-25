package com.appmattus.markdown

import com.vladsch.flexmark.ext.autolink.AutolinkExtension
import com.vladsch.flexmark.ext.gfm.strikethrough.StrikethroughExtension
import com.vladsch.flexmark.ext.tables.TablesExtension
import com.vladsch.flexmark.parser.Parser
import com.vladsch.flexmark.util.options.MutableDataSet

object ParserFactory {
    private val options by lazy {
        MutableDataSet().apply {
            set(Parser.HEADING_NO_ATX_SPACE, true)
            //set(Parser.HEADING_NO_EMPTY_HEADING_WITHOUT_SPACE, true)
            //set(Parser.HEADING_NO_LEAD_SPACE, true)
        }
    }

    val parser by lazy {
        Parser.builder(options).extensions(
            listOf(
                TablesExtension.create(),
                StrikethroughExtension.create(),
                AutolinkExtension.create()
                //GfmIssuesExtension.create()
            )
        ).build()
    }
}
