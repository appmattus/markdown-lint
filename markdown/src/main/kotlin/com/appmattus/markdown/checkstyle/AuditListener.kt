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
 * Listener in charge of receiving events from the Checker.
 * Typical events sequence is:
 *
 * ```
 * auditStarted
 *     (fileStarted
 *         (addError)
 *     fileFinished)
 * auditFinished
 * ```
 */
interface AuditListener {

    /**
     * Notify that the audit is about to start.
     */
    fun auditStarted()

    /**
     * Notify that the audit is finished.
     */
    fun auditFinished()

    /**
     * Notify that audit is about to start on a specific file.
     *
     * @param event the event details
     */
    fun fileStarted(event: AuditEvent)

    /**
     * Notify that audit is finished on a specific file.
     *
     * @param event the event details
     */
    fun fileFinished(event: AuditEvent)

    /**
     * Notify that an audit error was discovered on a specific file.
     *
     * @param event the event details
     */
    fun addError(event: AuditEvent)
}
