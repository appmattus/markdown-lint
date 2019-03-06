package com.appmattus.markdown

import com.puppycrawl.tools.checkstyle.XMLLogger
import com.puppycrawl.tools.checkstyle.api.AuditEvent
import com.puppycrawl.tools.checkstyle.api.AutomaticBean
import com.puppycrawl.tools.checkstyle.api.LocalizedMessage
import com.puppycrawl.tools.checkstyle.api.SeverityLevel
import java.io.ByteArrayOutputStream
import java.io.File
import javax.script.ScriptEngineManager
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult
import javax.xml.transform.stream.StreamSource

class RuleProcessor {

    fun process(configFile: File?, rootDir: File, reportsDir: File) {
        val config = configFile?.evaluate() ?: MarkdownLintConfig.Builder().build()
        val fileErrors = rootDir.scanForErrors(config)

        val checkstyleXmlBytes = fileErrors.mapErrorsToCheckstyleXmlBytes()

        generateXmlReport(reportsDir, config, checkstyleXmlBytes)
        generateHtmlReport(reportsDir, config, checkstyleXmlBytes)
    }

    private fun File.evaluate(): MarkdownLintConfig {
        val engine = ScriptEngineManager().getEngineByName("kotlin")
        val result = engine.eval(readText())
        if (result is MarkdownLintConfig) {
            return result
        } else {
            throw IllegalStateException("Expecting a MarkdownLintConfig but got ${result.javaClass.name}")
        }
    }

    private fun File.scanForErrors(config: MarkdownLintConfig): Map<File, List<Error>> {
        val markdownFiles = listMarkdownFiles()

        val rules = AllRules(config).rules

        val parser = ParserFactory.parser
        val fileErrors = markdownFiles.associateWith { file ->
            val document = MarkdownDocument(file.name, parser.parse(file.readText(Charsets.UTF_8)))
            rules.flatMap { rule ->
                rule.processDocument(document)
            }
        }.filterValues { it.isNotEmpty() }
        return fileErrors
    }

    private fun File.listMarkdownFiles(): List<File> {
        return walk()
            .onEnter { it.name != "build" && !it.name.startsWith(".") }
            .filter { it.isFile }
            .filter { it.extension == "md" || it.extension == "markdown" }
            .toList()
    }

    private fun Map<File, List<Error>>.mapErrorsToCheckstyleXmlBytes(): ByteArray {
        return ByteArrayOutputStream().use {
            val logger = XMLLogger(it, AutomaticBean.OutputStreamOptions.NONE)

            logger.auditStarted(null)

            this.forEach { file, errors ->

                val filePath = file.path

                logger.fileStarted(AuditEvent(this, filePath))

                errors.forEach { error ->
                    val message = LocalizedMessage(
                        error.lineNumber, error.columnNumber,
                        "messages.properties", error.errorMessage, null, SeverityLevel.ERROR, null,
                        error.ruleClass, null
                    )

                    logger.addError(AuditEvent(this, filePath, message))
                }

                logger.fileFinished(AuditEvent(this, filePath))
            }

            logger.auditFinished(null)

            it.toByteArray()
        }
    }

    private fun generateXmlReport(reportsDir: File, config: MarkdownLintConfig, checkstyleXmlBytes: ByteArray) {
        if (config.generateXmlReport()) {
            java.io.File(reportsDir, "markdownlint.xml").writeBytes(checkstyleXmlBytes)
        }
    }

    private fun generateHtmlReport(
        reportsDir: File,
        config: MarkdownLintConfig,
        checkstyleXmlBytes: ByteArray
    ) {
        if (config.generateHtmlReport()) {
            val factory = javax.xml.parsers.DocumentBuilderFactory.newInstance()
            val builder = factory.newDocumentBuilder()
            val document = builder.parse(checkstyleXmlBytes.inputStream())

            val stylesheet =
                RuleProcessor::class.java.classLoader.getResourceAsStream("checkstyle-noframes-sorted.xsl")

            val transformer = TransformerFactory.newInstance().newTransformer(StreamSource(stylesheet))

            val outputHtmlFile = File(reportsDir, "markdownlint.html")

            val result = StreamResult(outputHtmlFile.outputStream())
            transformer.transform(DOMSource(document), result)
        }
    }

    private fun MarkdownLintConfig.generateXmlReport() = reports.contains(Report.Checkstyle)
    private fun MarkdownLintConfig.generateHtmlReport() = reports.contains(Report.Html)
}
