package com.appmattus.markdown.processing

import com.appmattus.markdown.checkstyle.AuditEvent
import com.appmattus.markdown.checkstyle.LocalizedMessage
import com.appmattus.markdown.checkstyle.OutputStreamOptions
import com.appmattus.markdown.checkstyle.SeverityLevel
import com.appmattus.markdown.checkstyle.XMLLogger
import com.appmattus.markdown.dsl.Config
import com.appmattus.markdown.dsl.Report
import com.appmattus.markdown.errors.Error
import com.appmattus.markdown.filter.MultiPathFilter
import com.appmattus.markdown.plugin.BuildFailure
import com.appmattus.markdown.rules.AllRules
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.PrintStream
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult
import javax.xml.transform.stream.StreamSource

class RuleProcessor(private val rootDir: File, private val reportsDir: File) {

    fun process(config: Config, summaryStream: PrintStream? = null) {
        val markdownFiles = rootDir.listMarkdownFiles(config)
        val fileErrors = markdownFiles.scanForErrors(config)

        summaryStream?.summariseErrors(markdownFiles, fileErrors)

        val checkstyleXmlBytes = fileErrors.mapErrorsToCheckstyleXmlBytes()

        if (config.generateXmlReport()) {
            try {
                val xmlFile = File(reportsDir, "markdownlint.xml")
                generateXmlReport(xmlFile, checkstyleXmlBytes)
                summaryStream?.summariseXmlGeneration(xmlFile)
            } catch (@Suppress("TooGenericExceptionCaught") e: Exception) {
                // Ignore
            }
        }

        if (config.generateHtmlReport()) {
            try {
                val htmlFile = File(reportsDir, "markdownlint.html")
                generateHtmlReport(htmlFile, checkstyleXmlBytes)
                summaryStream?.summariseHtmlGeneration(htmlFile)
            } catch (@Suppress("TooGenericExceptionCaught") e: Exception) {
                // Ignore
            }
        }

        val errors = fileErrors.values.sumBy { it.size }
        if (errors > config.threshold) {
            throw BuildFailure("Build failure threshold of ${config.threshold} reached with $errors errors!")
        }
    }

    private fun PrintStream.summariseXmlGeneration(xmlFile: File) {
        println("Successfully generated Checkstyle XML report at ${xmlFile.path}")
    }

    private fun PrintStream.summariseHtmlGeneration(htmlFile: File) {
        println("Successfully generated HTML report at ${htmlFile.path}")
    }

    private fun PrintStream.summariseErrors(
        markdownFiles: List<File>,
        fileErrors: Map<File, List<Error>>
    ) {
        println("${markdownFiles.size} markdown files were analysed")
        println()

        if (fileErrors.isNotEmpty()) {
            println("Errors:")
            fileErrors.forEach { (file, errors) ->
                errors.forEach { error ->
                    println(
                        "    ${error.ruleClass.simpleName} at ${file.path}:${error.lineNumber}:${error.columnNumber}"
                    )
                }
            }
        } else {
            println("No errors reported")
        }
        println()
    }

    private fun List<File>.scanForErrors(config: Config): Map<File, List<Error>> {

        val rules = AllRules(config).rules

        val parser = ParserFactory.parser

        return associateWith { file ->
            val document = MarkdownDocument(file, parser.parse(file.readText(Charsets.UTF_8)))

            rules.flatMap { rule ->
                val includes = MultiPathFilter(rule.configuration.includes, rootDir.toPath())
                val excludes = MultiPathFilter(rule.configuration.excludes, rootDir.toPath())

                if (includes.matches(file.toPath()) && !excludes.matches(file.toPath())) {
                    rule.processDocument(document)
                } else {
                    emptyList()
                }
            }
        }.filterValues { it.isNotEmpty() }
    }

    private fun File.listMarkdownFiles(config: Config): List<File> {
        val includes = MultiPathFilter(config.includes, toPath())
        val excludes = MultiPathFilter(config.excludes, toPath())

        return walk()
            .onEnter { it.name != "build" && !it.name.startsWith(".") }
            .filter { it.isFile }
            .filter { it.extension == "md" || it.extension == "markdown" }
            .filter { includes.matches(it.toPath()) && !excludes.matches(it.toPath()) }
            .toList()
    }

    private fun Map<File, List<Error>>.mapErrorsToCheckstyleXmlBytes(): ByteArray {
        return ByteArrayOutputStream().use {
            val logger = XMLLogger(it, OutputStreamOptions.NONE)

            logger.auditStarted()

            forEach { (file, errors) ->

                val filePath = file.path

                logger.fileStarted(AuditEvent(filePath))

                errors.forEach { error ->
                    val message = LocalizedMessage(
                        error.lineNumber, error.columnNumber,
                        error.errorMessage, SeverityLevel.ERROR, error.ruleClass
                    )

                    logger.addError(AuditEvent(filePath, message))
                }

                logger.fileFinished(AuditEvent(filePath))
            }

            logger.auditFinished()

            it.toByteArray()
        }
    }

    private fun generateXmlReport(xmlFile: File, checkstyleXmlBytes: ByteArray) {
        xmlFile.writeBytes(checkstyleXmlBytes)
    }

    private fun generateHtmlReport(
        htmlFile: File,
        checkstyleXmlBytes: ByteArray
    ) {
        val factory = javax.xml.parsers.DocumentBuilderFactory.newInstance()
        val builder = factory.newDocumentBuilder()
        val document = builder.parse(checkstyleXmlBytes.inputStream())

        val stylesheet = RuleProcessor::class.java.classLoader.getResourceAsStream("checkstyle-noframes-sorted.xsl")

        val transformer = TransformerFactory.newInstance().newTransformer(StreamSource(stylesheet))

        val result = StreamResult(htmlFile.outputStream())
        transformer.transform(DOMSource(document), result)
    }

    private fun Config.generateXmlReport() = reports.contains(Report.Checkstyle)
    private fun Config.generateHtmlReport() = reports.contains(Report.Html)
}
