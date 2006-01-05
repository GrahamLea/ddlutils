package org.apache.ddlutils.platform.mysql;

/*
 * Copyright 1999-2006 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.sql.Types;

import org.apache.ddlutils.PlatformInfo;
import org.apache.ddlutils.platform.PlatformImplBase;

/**
 * The platform implementation for MySQL.
 * 
 * @author Thomas Dudziak
 * @version $Revision: 231306 $
 */
public class MySqlPlatform extends PlatformImplBase
{
    /** Database name of this platform. */
    public static final String DATABASENAME     = "MySQL";
    /** The standard MySQL jdbc driver. */
    public static final String JDBC_DRIVER      = "com.mysql.jdbc.Driver";
    /** The old MySQL jdbc driver. */
    public static final String JDBC_DRIVER_OLD  = "org.gjt.mm.mysql.Driver";
    /** The subprotocol used by the standard MySQL driver. */
    public static final String JDBC_SUBPROTOCOL = "mysql";

    /**
     * Creates a new platform instance.
     */
    public MySqlPlatform()
    {
        PlatformInfo info = new PlatformInfo();

        info.setMaxIdentifierLength(64);
        info.setRequiringNullAsDefaultValue(true);
        info.setSupportingDefaultValuesForLongTypes(false);
        info.setPrimaryKeyEmbedded(true);
        info.setForeignKeysEmbedded(false);
        info.setIndicesEmbedded(false);
        // see http://dev.mysql.com/doc/refman/4.1/en/example-auto-increment.html
        info.setSupportingNonPKIdentityColumns(false);
        // MySql returns synthetic default values for pk columns
        info.setReturningDefaultValueForRequired(true);
        info.setCommentPrefix("#");
        // Double quotes are only allowed for delimiting identifiers if the server SQL mode includes ANSI_QUOTES 
        info.setDelimiterToken("`");

        info.addNativeTypeMapping(Types.ARRAY,         "LONGBLOB",          Types.LONGVARBINARY);
        info.addNativeTypeMapping(Types.BIT,           "TINYINT(1)");
        info.addNativeTypeMapping(Types.BLOB,          "LONGBLOB",          Types.LONGVARBINARY);
        info.addNativeTypeMapping(Types.CLOB,          "LONGTEXT",          Types.LONGVARCHAR);
        info.addNativeTypeMapping(Types.DISTINCT,      "LONGBLOB",          Types.LONGVARBINARY);
        info.addNativeTypeMapping(Types.FLOAT,         "DOUBLE",            Types.DOUBLE);
        info.addNativeTypeMapping(Types.JAVA_OBJECT,   "LONGBLOB",          Types.LONGVARBINARY);
        info.addNativeTypeMapping(Types.LONGVARBINARY, "MEDIUMBLOB");
        info.addNativeTypeMapping(Types.LONGVARCHAR,   "MEDIUMTEXT");
        info.addNativeTypeMapping(Types.NULL,          "MEDIUMBLOB",        Types.LONGVARBINARY);
        info.addNativeTypeMapping(Types.NUMERIC,       "DECIMAL",           Types.DECIMAL);
        info.addNativeTypeMapping(Types.OTHER,         "LONGBLOB",          Types.LONGVARBINARY);
        info.addNativeTypeMapping(Types.REAL,          "FLOAT");
        info.addNativeTypeMapping(Types.REF,           "MEDIUMBLOB",        Types.LONGVARBINARY);
        info.addNativeTypeMapping(Types.STRUCT,        "LONGBLOB",          Types.LONGVARBINARY);
        // Since TIMESTAMP is not a stable datatype yet, and does not support a higher precision
        // that DATETIME (year to seconds) as of MySQL 5, we map the JDBC type here to DATETIME
        // TODO: Make this configurable
        info.addNativeTypeMapping(Types.TIMESTAMP,     "DATETIME");
        // In MySql, TINYINT has only a range of -128 to 127
        info.addNativeTypeMapping(Types.TINYINT,       "SMALLINT",          Types.SMALLINT);
        info.addNativeTypeMapping("BOOLEAN",  "TINYINT(1)", "BIT");
        info.addNativeTypeMapping("DATALINK", "MEDIUMBLOB");

        info.addDefaultSize(Types.CHAR,      254);
        info.addDefaultSize(Types.VARCHAR,   254);
        info.addDefaultSize(Types.BINARY,    254);
        info.addDefaultSize(Types.VARBINARY, 254);
        
        setSqlBuilder(new MySqlBuilder(info));
        setModelReader(new MySqlModelReader(info));
    }

    /**
     * {@inheritDoc}
     */
    public String getName()
    {
        return DATABASENAME;
    }
}