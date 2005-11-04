package org.apache.ddlutils.platform;

/*
 * Copyright 1999-2005 The Apache Software Foundation.
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

import org.apache.ddlutils.TestPlatformBase;
import org.apache.ddlutils.platform.FirebirdPlatform;

/**
 * Tests the Firebird platform.
 * 
 * @author Thomas Dudziak
 * @version $Revision: 231110 $
 */
public class TestFirebirdPlatform extends TestPlatformBase
{
    /**
     * {@inheritDoc}
     */
    protected String getDatabaseName()
    {
        return FirebirdPlatform.DATABASENAME;
    }

    /**
     * Tests the column types.
     */
    public void testColumnTypes() throws Exception
    {
        assertEqualsIgnoringWhitespaces(
            "DROP TABLE \"coltype\";\n"+
            "COMMIT;\n"+
            "CREATE TABLE \"coltype\"\n"+
            "(\n"+
            "    \"COL_ARRAY\"           BLOB ,\n"+
            "    \"COL_BIGINT\"          DECIMAL(38,0),\n"+
            "    \"COL_BINARY\"          CHAR CHARACTER SET OCTETS,\n"+
            "    \"COL_BIT\"             DECIMAL(1,0),\n"+
            "    \"COL_BLOB\"            BLOB ,\n"+
            "    \"COL_BOOLEAN\"         DECIMAL(1,0),\n"+
            "    \"COL_CHAR\"            CHAR(15),\n"+
            "    \"COL_CLOB\"            BLOB SUB_TYPE TEXT,\n"+
            "    \"COL_DATALINK\"        BLOB,\n"+
            "    \"COL_DATE\"            DATE,\n"+
            "    \"COL_DECIMAL\"         DECIMAL(15,3),\n"+
            "    \"COL_DECIMAL_NOSCALE\" DECIMAL(15,0),\n"+
            "    \"COL_DISTINCT\"        BLOB,\n"+
            "    \"COL_DOUBLE\"          DOUBLE PRECISION,\n"+
            "    \"COL_FLOAT\"           DOUBLE PRECISION,\n"+
            "    \"COL_INTEGER\"         INTEGER,\n"+
            "    \"COL_JAVA_OBJECT\"     BLOB,\n"+
            "    \"COL_LONGVARBINARY\"   BLOB,\n"+
            "    \"COL_LONGVARCHAR\"     BLOB SUB_TYPE TEXT,\n"+
            "    \"COL_NULL\"            BLOB,\n"+
            "    \"COL_NUMERIC\"         NUMERIC(15,0),\n"+
            "    \"COL_OTHER\"           BLOB,\n"+
            "    \"COL_REAL\"            FLOAT,\n"+
            "    \"COL_REF\"             BLOB,\n"+
            "    \"COL_SMALLINT\"        SMALLINT,\n"+
            "    \"COL_STRUCT\"          BLOB,\n"+
            "    \"COL_TIME\"            TIME,\n"+
            "    \"COL_TIMESTAMP\"       TIMESTAMP,\n"+
            "    \"COL_TINYINT\"         SMALLINT,\n"+
            "    \"COL_VARBINARY\"       VARCHAR(15) CHARACTER SET OCTETS,\n"+
            "    \"COL_VARCHAR\"         VARCHAR(15)\n"+
            ");\n"+
            "COMMIT;\n",
            createTestDatabase(COLUMN_TEST_SCHEMA));
    }

    /**
     * Tests the column constraints.
     */
    public void testColumnConstraints() throws Exception
    {
        assertEqualsIgnoringWhitespaces(
            "DELETE FROM RDB$GENERATOR WHERE RDB$GENERATOR_NAME = \"gen_constraints_OL_PK_AUTO_INCR\";\n" +
            "COMMIT;\n"+
            "DELETE FROM RDB$GENERATOR WHERE RDB$GENERATOR_NAME = \"gen_constraints_COL_AUTO_INCR\";\n" +
            "COMMIT;\n"+
            "DROP TABLE \"constraints\";\n"+
            "COMMIT;\n"+
            "CREATE TABLE \"constraints\"\n"+
            "(\n"+
            "    \"COL_PK\"               VARCHAR(32),\n"+
            "    \"COL_PK_AUTO_INCR\"     INTEGER,\n"+
            "    \"COL_NOT_NULL\"         CHAR(100) CHARACTER SET OCTETS NOT NULL,\n"+
            "    \"COL_NOT_NULL_DEFAULT\" DOUBLE PRECISION DEFAULT '-2.0' NOT NULL,\n"+
            "    \"COL_DEFAULT\"          CHAR(4) DEFAULT 'test',\n"+
            "    \"COL_AUTO_INCR\"        DECIMAL(38,0),\n"+
            "    PRIMARY KEY (\"COL_PK\", \"COL_PK_AUTO_INCR\")\n"+
            ");\n"+
            "COMMIT;\n"+
            "CREATE GENERATOR \"gen_constraints_OL_PK_AUTO_INCR\";\n" +
            "COMMIT;\n"+
            "SET TERM !! ;\n"+
            "CREATE TRIGGER \"trg_constraints_OL_PK_AUTO_INCR\" FOR \"constraints\"\n"+
            "ACTIVE BEFORE INSERT POSITION 0 AS\n"+
            "BEGIN\n"+
            "  IF (NEW.\"COL_PK_AUTO_INCR\" IS NULL) THEN\n"+
            "    NEW.\"COL_PK_AUTO_INCR\" = GEN_ID(\"gen_constraints_OL_PK_AUTO_INCR\", 1);\n"+
            "END !!\n"+
            "SET TERM ; !!\n"+
            "COMMIT;\n"+
            "CREATE GENERATOR \"gen_constraints_COL_AUTO_INCR\";\n" +
            "COMMIT;\n"+
            "SET TERM !! ;\n"+
            "CREATE TRIGGER \"trg_constraints_COL_AUTO_INCR\" FOR \"constraints\"\n"+
            "ACTIVE BEFORE INSERT POSITION 0 AS\n"+
            "BEGIN\n"+
            "  IF (NEW.\"COL_AUTO_INCR\" IS NULL) THEN\n"+
            "    NEW.\"COL_AUTO_INCR\" = GEN_ID(\"gen_constraints_COL_AUTO_INCR\", 1);\n"+
            "END !!\n"+
            "SET TERM ; !!\n"+
            "COMMIT;\n",
            createTestDatabase(COLUMN_CONSTRAINT_TEST_SCHEMA));
    }

    /**
     * Tests the table constraints.
     */
    public void testTableConstraints() throws Exception
    {
        assertEqualsIgnoringWhitespaces(
            "ALTER TABLE \"table3\" DROP CONSTRAINT \"testfk\";\n"+
            "COMMIT;\n"+
            "ALTER TABLE \"table2\" DROP CONSTRAINT \"table2_FK_COL_F_COL_FK_2_table1\";\n"+
            "COMMIT;\n"+
            "DROP TABLE \"table3\";\n"+
            "COMMIT;\n"+
            "DROP TABLE \"table2\";\n"+
            "COMMIT;\n"+
            "DROP TABLE \"table1\";\n"+
            "COMMIT;\n"+
            "CREATE TABLE \"table1\"\n"+
            "(\n"+
            "    \"COL_PK_1\"    VARCHAR(32) NOT NULL,\n"+
            "    \"COL_PK_2\"    INTEGER,\n"+
            "    \"COL_INDEX_1\" CHAR(100) CHARACTER SET OCTETS NOT NULL,\n"+
            "    \"COL_INDEX_2\" DOUBLE PRECISION NOT NULL,\n"+
            "    \"COL_INDEX_3\" CHAR(4),\n"+
            "    PRIMARY KEY (\"COL_PK_1\", \"COL_PK_2\")\n"+
            ");\n"+
            "CREATE INDEX \"testindex1\" ON \"table1\" (\"COL_INDEX_2\");\n"+
            "CREATE UNIQUE INDEX \"testindex2\" ON \"table1\" (\"COL_INDEX_3\", \"COL_INDEX_1\");\n"+
            "COMMIT;\n"+
            "CREATE TABLE \"table2\"\n"+
            "(\n"+
            "    \"COL_PK\"   INTEGER,\n"+
            "    \"COL_FK_1\" INTEGER,\n"+
            "    \"COL_FK_2\" VARCHAR(32) NOT NULL,\n"+
            "    PRIMARY KEY (\"COL_PK\")\n"+
            ");\n"+
            "COMMIT;\n"+
            "CREATE TABLE \"table3\"\n"+
            "(\n"+
            "    \"COL_PK\" VARCHAR(16),\n"+
            "    \"COL_FK\" INTEGER NOT NULL,\n"+
            "    PRIMARY KEY (\"COL_PK\")\n"+
            ");\n"+
            "COMMIT;\n"+
            "ALTER TABLE \"table2\" ADD CONSTRAINT \"table2_FK_COL_F_COL_FK_2_table1\" FOREIGN KEY (\"COL_FK_1\", \"COL_FK_2\") REFERENCES \"table1\" (\"COL_PK_2\", \"COL_PK_1\");\n"+
            "COMMIT;\n"+
            "ALTER TABLE \"table3\" ADD CONSTRAINT \"testfk\" FOREIGN KEY (\"COL_FK\") REFERENCES \"table2\" (\"COL_PK\");\n"+
            "COMMIT;\n",
            createTestDatabase(TABLE_CONSTRAINT_TEST_SCHEMA));
    }
}