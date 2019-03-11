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
 * Raw event for audit.
 *
 * I'm not very satisfied about the design of this event since there are
 * optional methods that will return null in most of the case. This will
 * need some work to clean it up especially if we want to introduce
 * a more sequential reporting action rather than a packet error
 * reporting. This will allow for example to follow the process quickly
 * in an interface or a servlet (yep, that's cool to run a check via
 * a web interface in a source repository ;-)
 *
 * Creates a new `AuditEvent` instance.
 *
 * @param fileName         file associated with the event
 * @param localizedMessage the actual message
 * @see AuditListener
 */
class AuditEvent(
    val fileName: String,
    private val localizedMessage: LocalizedMessage? = null
) {

    /**
     * Return the line number on the source file where the event occurred.
     * This may be 0 if there is no relation to a file content.
     *
     * @return an integer representing the line number in the file source code.
     */
    val line: Int
        get() = localizedMessage!!.lineNo

    /**
     * Return the message associated to the event.
     *
     * @return the event message
     */
    val message: String
        get() = localizedMessage!!.message

    /**
     * Gets the column associated with the message.
     *
     * @return the column associated with the message
     */
    val column: Int
        get() = localizedMessage!!.columnNo

    /**
     * Gets the audit event severity level.
     *
     * @return the audit event severity level
     */
    val severityLevel: SeverityLevel
        get() = localizedMessage?.severityLevel ?: SeverityLevel.INFO

    /**
     * Gets the name of the source for the message.
     *
     * @return the name of the source for the message
     */
    val sourceName: String
        get() = localizedMessage!!.sourceName
}
