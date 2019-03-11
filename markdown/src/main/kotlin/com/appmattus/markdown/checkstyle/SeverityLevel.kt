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

import java.util.Locale

/**
 * Severity level for a check violation.
 *
 * Each violation of an audit check is assigned one of the severity levels
 * defined here.
 *
 */
enum class SeverityLevel {

    /**
     * Severity level ignore.
     */
    IGNORE,
    /**
     * Severity level info.
     */
    INFO,
    /**
     * Severity level warning.
     */
    WARNING,
    /**
     * Severity level error.
     */
    ERROR;

    /**
     * Returns name of severity level.
     *
     * @return the name of this severity level.
     */
    val value: String
        get() = name.toLowerCase(Locale.ENGLISH)

    override fun toString(): String {
        return value
    }
}
