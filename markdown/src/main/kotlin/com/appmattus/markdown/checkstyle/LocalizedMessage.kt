////////////////////////////////////////////////////////////////////////////////
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
////////////////////////////////////////////////////////////////////////////////

package com.appmattus.markdown.checkstyle

/**
 * Represents a message that can be localised. The translations come from
 * message.properties files. The underlying implementation uses
 * java.text.MessageFormat.
 *
 * @param lineNo        line number associated with the message
 * @param columnNo      column number associated with the message
 * @param message       the error message
 * @param severityLevel severity level for the message
 * @param sourceClass   the Class that is the source of the message
 */
data class LocalizedMessage(
    val lineNo: Int,
    val columnNo: Int,
    val message: String,
    val severityLevel: SeverityLevel,
    private val sourceClass: Class<*>
) : Comparable<LocalizedMessage> {

    /**
     * Gets the name of the source for this LocalizedMessage.
     *
     * @return the name of the source for this LocalizedMessage
     */
    val sourceName: String = sourceClass.name

    ////////////////////////////////////////////////////////////////////////////
    // Interface Comparable methods
    ////////////////////////////////////////////////////////////////////////////

    @Suppress("ComplexMethod")
    override fun compareTo(other: LocalizedMessage): Int {
        return if (lineNo == other.lineNo) {
            if (columnNo == other.columnNo) {
                message.compareTo(other.message)
            } else {
                columnNo.compareTo(other.columnNo)
            }
        } else {
            lineNo.compareTo(other.lineNo)
        }
    }
}
