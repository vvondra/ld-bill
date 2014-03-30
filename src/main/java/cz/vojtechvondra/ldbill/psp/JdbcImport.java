package cz.vojtechvondra.ldbill.psp;

import com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;


public class JdbcImport {
    private final Connection conn;
    private final TableDefinition def;
    private final PSPExport export;
    static Logger logger = Logger.getLogger(JdbcImport.class);

    public JdbcImport(Connection connection, TableDefinition def, PSPExport export) {
        this.conn = connection;
        this.def = def;
        this.export = export;
    }

    /**
     * Imports all known datasets to H2
     *
     * @param connection JDBC connection to H2
     * @param downloader Downloader for PSP archives
     */
    public static void importAll(Connection connection, PSPDownloader downloader) {
        JdbcImport importer;
        for (String set : PSPDownloader.getKnownDatasetNames()) {
            try {
                logger.debug("Importing dataset to H2: " + set);
                importer = new JdbcImport(connection, TableDefinition.factory(set), new PSPExport(downloader, set));
                importer.importData();
                logger.debug("Imported dataset to H2: " + set);
            } catch (ReflectiveOperationException e) {
                logger.error("Could not import set " + set, e);
            }
        }
    }

    /**
     * Imports a single dataset into the database
     */
    public void importData() {
        int batchCount = 0;
        String[] data;
        String[] colNames = def.getColNames();
        String insertSql = prepareInsertSql();
        PreparedStatement stmt = null;
        try {
            conn.setAutoCommit(false);
            createSchema();
            stmt = conn.prepareStatement(insertSql);
        } catch (SQLException e) {
            logger.error("Could not create prepared statement", e);
            return;
        }
        try {
            while ((data = export.getLine()) != null) {
                if (data.length - 1 != colNames.length) {
                    logger.warn("Column count mismatch in H2 import: " + data.length + " vs. " + colNames.length);
                    continue;
                }
                for (int i = 0; i < colNames.length; i++) {
                    if (def.isNumericCol(colNames[i])) {
                        int n;
                        try {
                            n = Integer.parseInt(data[i]);
                        } catch (NumberFormatException e) {
                            n = 0;
                        }
                        stmt.setInt(i + 1, n);
                    } else {
                        stmt.setString(i + 1, data[i]);
                    }
                }
                stmt.addBatch();
                if ((batchCount + 1) % 100 == 0) {
                    stmt.executeBatch();
                    conn.commit();
                    stmt.close();
                    stmt = conn.prepareStatement(insertSql);
                    batchCount = 0;
                }
                batchCount++;
            }
            stmt.executeBatch();
            conn.commit();
        } catch (SQLException e) {
            logger.error("SQL error in H2 import.", e);
        } finally {
            try {
                stmt.close();
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                logger.error("Could not close prepared statement.", e);
            }
        }
        try {
            export.close();
        } catch (IOException e) {
            logger.error("Could not close export file in H2 import.", e);
        }
    }

    /**
     * Prepare the SQL statement for a single export row
     * @return SQL INSERT string for prepared statement
     */
    private String prepareInsertSql() {
        String[] colNames = def.getColNames();
        StringBuilder insertSql = new StringBuilder();
        insertSql.append("INSERT INTO ").append(def.getTableName()).append("(");
        for (int i = 0; i < colNames.length; i++) {
            insertSql.append(colNames[i].toUpperCase());
            if (i != colNames.length - 1) {
                insertSql.append(", ");
            }
        }
        insertSql.append(") VALUES (");
        for (int i = 0; i < colNames.length; i++) {
            insertSql.append("?");
            if (i != colNames.length - 1) {
                insertSql.append(", ");
            }
        }
        insertSql.append(")");
        return insertSql.toString();
    }

    /**
     * Creates a table in the database for the import
     * @throws SQLException
     */
    private void createSchema() throws SQLException {
        Statement stmt = conn.createStatement();
        logger.debug("Creating table " + def.getTableName());
        String sql = String.format("CREATE TABLE IF NOT EXISTS %s (%s)",
                def.getTableName(), def.getColSqlDefinitions());
        stmt.executeUpdate(sql);

        List<String> indices = def.getIndices();
        for (String index : indices) {
            sql = String.format("CREATE INDEX %s ON %s(%s)",
                    getIndexName(def.getTableName(), index), def.getTableName(), index);
            try {
                stmt.executeUpdate(sql);
            } catch (MySQLSyntaxErrorException ignored) {
                /* ignored */
            }
        }
        stmt.close();
    }


    /**
     * @param tableName Table name in which to create index
     * @param colName Column name which is part of the index
     * @return Generated index name from table and column
     */
    private String getIndexName(String tableName, String colName) {
        return ("IDX" + tableName + colName).toUpperCase();
    }
}
