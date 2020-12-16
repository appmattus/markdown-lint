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
import java.io.PrintStream
import java.nio.file.FileVisitResult
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.SimpleFileVisitor
import java.nio.file.attribute.BasicFileAttributes
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult
import javax.xml.transform.stream.StreamSource

@Suppress("TooManyFunctions")
class RuleProcessor(private val rootDir: Path, private val reportsDir: Path) {

    fun process(config: Config, summaryStream: PrintStream? = null) {
        val markdownFiles = rootDir.listMarkdownFiles(config)
        val fileErrors = markdownFiles.scanForErrors(config)

        summaryStream?.summariseErrors(markdownFiles, fileErrors)

        val checkstyleXmlBytes = fileErrors.mapErrorsToCheckstyleXmlBytes()

        if (config.generateXmlReport()) {
            try {
                val xmlFile = reportsDir.resolve("markdownlint.xml")
                generateXmlReport(xmlFile, checkstyleXmlBytes)
                summaryStream?.summariseXmlGeneration(xmlFile)
            } catch (@Suppress("TooGenericExceptionCaught") e: Exception) {
                // Ignore
            }
        }

        if (config.generateHtmlReport()) {
            try {
                val htmlFile = reportsDir.resolve("markdownlint.html")
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

    private fun PrintStream.summariseXmlGeneration(xmlFile: Path) {
        println("Successfully generated Checkstyle XML report at $xmlFile")
    }

    private fun PrintStream.summariseHtmlGeneration(htmlFile: Path) {
        println("Successfully generated HTML report at $htmlFile")
    }

    private fun PrintStream.summariseErrors(
        markdownFiles: List<Path>,
        fileErrors: Map<Path, List<Error>>
    ) {
        println("${markdownFiles.size} markdown files were analysed")
        println()

        if (fileErrors.isNotEmpty()) {
            println("Errors:")
            fileErrors.forEach { (file, errors) ->
                errors.forEach { error ->
                    println(
                        "    ${error.ruleClass.simpleName} at $file:${error.lineNumber}:${error.columnNumber}"
                    )
                }
            }
        } else {
            println("No errors reported")
        }
        println()
    }

    private fun List<Path>.scanForErrors(config: Config): Map<Path, List<Error>> {

        val rules = AllRules(config).rules

        val parser = ParserFactory.parser

        return associateWith { file ->
            val document = MarkdownDocument(file, parser.parse(String(Files.readAllBytes(file), Charsets.UTF_8)))

            rules.flatMap { rule ->
                val includes = MultiPathFilter(rule.configuration.includes, rootDir)
                val excludes = MultiPathFilter(rule.configuration.excludes, rootDir)

                if (includes.matches(file) && !excludes.matches(file)) {
                    rule.processDocument(document)
                } else {
                    emptyList()
                }
            }
        }.filterValues { it.isNotEmpty() }
    }

    private fun Path.listMarkdownFiles(config: Config): List<Path> {
        val includes = MultiPathFilter(config.includes, this)
        val excludes = MultiPathFilter(config.excludes, this)

        val markdownFiles = mutableListOf<Path>()

        Files.walkFileTree(this, object : SimpleFileVisitor<Path>() {
            override fun preVisitDirectory(dir: Path?, attrs: BasicFileAttributes?): FileVisitResult {
                return if (dir!!.fileName.toString() == "build" || dir.fileName.toString().startsWith(".")) {
                    FileVisitResult.SKIP_SUBTREE
                } else {
                    FileVisitResult.CONTINUE
                }
            }

            override fun visitFile(file: Path?, attrs: BasicFileAttributes?): FileVisitResult {
                if (Files.isRegularFile(file!!) &&
                        hasMarkdownExtension(file) &&
                        matchesFilters(file)
                ) {
                    markdownFiles.add(file)
                }
                return FileVisitResult.CONTINUE
            }

            private fun hasMarkdownExtension(file: Path) =
                    file.fileName.toString().run {
                                endsWith(".md") || endsWith(".markdown")
                            }

            private fun matchesFilters(file: Path) =
                includes.matches(file) && !excludes.matches(file)
        })
        return markdownFiles
    }

    private fun Map<Path, List<Error>>.mapErrorsToCheckstyleXmlBytes(): ByteArray {
        return ByteArrayOutputStream().use {
            val logger = XMLLogger(it, OutputStreamOptions.NONE)

            logger.auditStarted()

            forEach { (file, errors) ->

                val filePath = file.toString()

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

    private fun generateXmlReport(xmlFile: Path, checkstyleXmlBytes: ByteArray) {
        Files.write(xmlFile, checkstyleXmlBytes)
    }

    private fun generateHtmlReport(
        htmlFile: Path,
        checkstyleXmlBytes: ByteArray
    ) {
        val factory = DocumentBuilderFactory.newInstance()
        val builder = factory.newDocumentBuilder()
        val document = builder.parse(checkstyleXmlBytes.inputStream())

        val stylesheet = RuleProcessor::class.java.classLoader.getResourceAsStream("checkstyle-noframes-sorted.xsl")

        val transformer = TransformerFactory.newInstance().newTransformer(StreamSource(stylesheet))

        val result = StreamResult(Files.newOutputStream(htmlFile))
        transformer.transform(DOMSource(document), result)
    }

    private fun Config.generateXmlReport() = reports.contains(Report.Checkstyle)
    private fun Config.generateHtmlReport() = reports.contains(Report.Html)
}
