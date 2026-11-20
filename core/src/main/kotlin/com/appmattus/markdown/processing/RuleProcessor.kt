package com.appmattus.markdown.processing

import com.appmattus.markdown.dsl.Config
import com.appmattus.markdown.dsl.Report
import com.appmattus.markdown.errors.Error
import com.appmattus.markdown.filter.MultiPathFilter
import com.appmattus.markdown.rules.AllRules
import java.io.PrintStream
import java.nio.file.FileVisitResult
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.SimpleFileVisitor
import java.nio.file.attribute.BasicFileAttributes

@Suppress("TooManyFunctions")
class RuleProcessor(private val rootDir: Path, private val reportsDir: Path) {

    fun process(config: Config, summaryStream: PrintStream? = null) {
        val markdownFiles = rootDir.listMarkdownFiles(config)
        val fileErrors = markdownFiles.scanForErrors(config)

        summaryStream?.summariseErrors(markdownFiles, fileErrors)

        if (config.generateXmlReport()) {
            try {
                val xmlFile = reportsDir.resolve("markdownlint.xml")
                Files.write(xmlFile, ReportGenerator.checkstyleXml(fileErrors).toByteArray(Charsets.UTF_8))
                summaryStream?.summariseXmlGeneration(xmlFile)
            } catch (@Suppress("TooGenericExceptionCaught", "SwallowedException") e: Exception) {
                // Ignore
            }
        }

        if (config.generateHtmlReport()) {
            try {
                val htmlFile = reportsDir.resolve("markdownlint.html")
                Files.write(htmlFile, ReportGenerator.html(fileErrors).toByteArray(Charsets.UTF_8))
                summaryStream?.summariseHtmlGeneration(htmlFile)
            } catch (@Suppress("TooGenericExceptionCaught", "SwallowedException") e: Exception) {
                // Ignore
            }
        }

        val errors = fileErrors.values.sumOf { it.size }
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

        Files.walkFileTree(
            this,
            object : SimpleFileVisitor<Path>() {
                override fun preVisitDirectory(dir: Path, attrs: BasicFileAttributes): FileVisitResult {
                    // fileName is null when dir is the root of the file system
                    val name = dir.fileName?.toString()
                    return if (name != null && (name == "build" || name.startsWith("."))) {
                        FileVisitResult.SKIP_SUBTREE
                    } else {
                        FileVisitResult.CONTINUE
                    }
                }

                override fun visitFile(file: Path, attrs: BasicFileAttributes): FileVisitResult {
                    if (Files.isRegularFile(file) && hasMarkdownExtension(file) && matchesFilters(file)) {
                        markdownFiles.add(file)
                    }
                    return FileVisitResult.CONTINUE
                }

                private fun hasMarkdownExtension(file: Path) = file.fileName.toString().run {
                    endsWith(".md") || endsWith(".markdown")
                }

                private fun matchesFilters(file: Path) = includes.matches(file) && !excludes.matches(file)
            }
        )
        return markdownFiles
    }

    private fun Config.generateXmlReport() = reports.contains(Report.Checkstyle)
    private fun Config.generateHtmlReport() = reports.contains(Report.Html)
}
