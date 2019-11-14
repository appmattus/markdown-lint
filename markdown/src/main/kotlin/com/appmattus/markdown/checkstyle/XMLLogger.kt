// //////////////////////////////////////////////////////////////////////////////
// checkstyle: Checks Java source code for adherence to a set of rules.
// Copyright (C) 2001-2019 the original author or authors.
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public
// License along with this library; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
// //////////////////////////////////////////////////////////////////////////////

package com.appmattus.markdown.checkstyle

import java.io.OutputStream
import java.io.PrintWriter
import java.io.StringWriter
import java.util.Collections
import java.util.concurrent.ConcurrentHashMap

/**
 * Simple XML logger.
 * It outputs everything in UTF-8 (default XML encoding is UTF-8) in case
 * we want to localize error messages or simply that file names are
 * localized and takes care about escaping as well.
 */
// -@cs[AbbreviationAsWordInName] We can not change it as,
// check's name is part of API (used in configurations).
@Suppress("TooManyFunctions")
class XMLLogger : AuditListener {

    /**
     * Close output stream in auditFinished.
     */
    private val closeStream: Boolean

    /**
     * The writer lock object.
     */
    private val writerLock = Any()

    /**
     * Holds all messages for the given file.
     */
    private val fileMessages = ConcurrentHashMap<String, FileMessages>()

    /**
     * Helper writer that allows easy encoding and printing.
     */
    private val writer: PrintWriter

    /**
     * Creates a new `XMLLogger` instance.
     * Sets the output to a defined stream.
     *
     * @param outputStream the stream to write logs to.
     * @param closeStream close oS in auditFinished
     * @noinspection BooleanParameter
     */
    @Deprecated("in order to fulfill demands of BooleanParameter IDEA check.")
    constructor(outputStream: OutputStream, closeStream: Boolean) {
        writer = PrintWriter(outputStream.writer())
        this.closeStream = closeStream
    }

    /**
     * Creates a new `XMLLogger` instance.
     * Sets the output to a defined stream.
     *
     * @param outputStream the stream to write logs to.
     * @param outputStreamOptions if `CLOSE` stream should be closed in auditFinished()
     */
    constructor(outputStream: OutputStream, outputStreamOptions: OutputStreamOptions?) {
        requireNotNull(outputStreamOptions) { "Parameter outputStreamOptions can not be null" }

        writer = PrintWriter(outputStream.writer())
        closeStream = outputStreamOptions == OutputStreamOptions.CLOSE
    }

    override fun auditStarted() {
        writer.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")

        val version = "8.18"

        writer.println("<checkstyle version=\"$version\">")
    }

    override fun auditFinished() {
        writer.println("</checkstyle>")
        if (closeStream) {
            writer.close()
        } else {
            writer.flush()
        }
    }

    override fun fileStarted(event: AuditEvent) {
        fileMessages[event.fileName] = FileMessages()
    }

    override fun fileFinished(event: AuditEvent) {
        val fileName = event.fileName
        val messages = fileMessages[fileName]

        synchronized(writerLock) {
            writeFileMessages(fileName, messages)
        }

        fileMessages.remove(fileName)
    }

    /**
     * Prints the file section with all file errors and exceptions.
     *
     * @param fileName The file name, as should be printed in the opening file tag.
     * @param messages The file messages.
     */
    private fun writeFileMessages(fileName: String, messages: FileMessages?) {
        writeFileOpeningTag(fileName)
        if (messages != null) {
            for (errorEvent in messages.getErrors()) {
                writeFileError(errorEvent)
            }
            for (exception in messages.getExceptions()) {
                writeException(exception)
            }
        }
        writeFileClosingTag()
    }

    /**
     * Prints the "file" opening tag with the given filename.
     *
     * @param fileName The filename to output.
     */
    private fun writeFileOpeningTag(fileName: String) {
        writer.println("<file name=\"" + encode(fileName) + "\">")
    }

    /**
     * Prints the "file" closing tag.
     */
    private fun writeFileClosingTag() {
        writer.println("</file>")
    }

    override fun addError(event: AuditEvent) {
        if (event.severityLevel != SeverityLevel.IGNORE) {
            val fileName = event.fileName
            if (!fileMessages.containsKey(fileName)) {
                synchronized(writerLock) {
                    writeFileError(event)
                }
            } else {
                fileMessages.getValue(fileName).addError(event)
            }
        }
    }

    /**
     * Outputs the given event to the writer.
     *
     * @param event An event to print.
     */
    private fun writeFileError(event: AuditEvent) {
        writer.print("<error" + " line=\"" + event.line + "\"")
        if (event.column > 0) {
            writer.print(" column=\"" + event.column + "\"")
        }
        writer.print(
            " severity=\"" +
                    event.severityLevel.value +
                    "\""
        )
        writer.print(
            " message=\"" +
                    encode(event.message) +
                    "\""
        )
        writer.print(" source=\"")
        writer.print(encode(event.sourceName))
        writer.println("\"/>")
    }

    /**
     * Writes the exception event to the print writer.
     *
     * @param throwable The
     */
    private fun writeException(throwable: Throwable) {
        writer.println("<exception>")
        writer.println("<![CDATA[")

        val stringWriter = StringWriter()
        val printer = PrintWriter(stringWriter)
        throwable.printStackTrace(printer)
        writer.println(encode(stringWriter.toString()))

        writer.println("]]>")
        writer.println("</exception>")
    }

    /**
     * Escape &lt;, &gt; &amp; &#39; and &quot; as their entities.
     *
     * @param value the value to escape.
     * @return the escaped value if necessary.
     */
    @Suppress("ComplexMethod")
    private fun encode(value: String): String {
        val sb = StringBuilder()
        for (chr in value) {
            when (chr) {
                '<' -> sb.append("&lt;")
                '>' -> sb.append("&gt;")
                '\'' -> sb.append("&apos;")
                '\"' -> sb.append("&quot;")
                '&' -> sb.append("&amp;")
                '\r' -> {
                }
                '\n' -> sb.append("&#10;")
                else -> if (Character.isISOControl(chr)) {
                    // true escape characters need '&' before but it also requires XML 1.1
                    // until https://github.com/checkstyle/checkstyle/issues/5168
                    sb.append("#x")
                    sb.append(Integer.toHexString(chr.toInt()))
                    sb.append(';')
                } else {
                    sb.append(chr)
                }
            }
        }
        return sb.toString()
    }

    /**
     * The registered file messages.
     */
    private class FileMessages {

        /**
         * The file error events.
         */
        private val errors = Collections.synchronizedList(mutableListOf<AuditEvent>())

        /**
         * The file exceptions.
         */
        private val exceptions = Collections.synchronizedList(mutableListOf<Throwable>())

        /**
         * Returns the file error events.
         *
         * @return the file error events.
         */
        fun getErrors(): List<AuditEvent> {
            return Collections.unmodifiableList(errors)
        }

        /**
         * Adds the given error event to the messages.
         *
         * @param event the error event.
         */
        fun addError(event: AuditEvent) {
            errors.add(event)
        }

        /**
         * Returns the file exceptions.
         *
         * @return the file exceptions.
         */
        fun getExceptions(): List<Throwable> {
            return Collections.unmodifiableList(exceptions)
        }
    }
}
