package cz.vojtechvondra.ldbill.psp;

import com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * Loads a PSP Export into a database system
 */
public class ExportDatabaseLoader {
    private final Connection conn;
    private final TableDefinition def;
    private final PSPExport export;
    private final SQLQueryBuilder queryBuilder;

    static Logger logger = Logger.getLogger(ExportDatabaseLoader.class);

    /**
     *
     * @param connection JDBC connection to the database
     * @param def Table DDL provider
     * @param export data export to be loaded
     */
    public ExportDatabaseLoader(Connection connection, TableDefinition def, PSPExport export) {
        this.conn = connection;
        this.def = def;
        this.export = export;
        this.queryBuilder = new SQLQueryBuilder(def);
    }

    /**
     * Imports all known datasets to the database
     * @param connection JDBC connection to the database
     * @param downloader Downloader for PSP archives
     */
    public static void importAll(Connection connection, PSPDownloader downloader) {
        ExportDatabaseLoader importer;
        for (String set : PSPDownloader.getKnownDatasetNames()) {
            try {
                logger.debug("Importing dataset to DB: " + set);
                importer = new ExportDatabaseLoader(connection, TableDefinition.factory(set), new PSPExport(downloader, set));
                importer.importData();
                logger.debug("Imported dataset to DB: " + set);
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
        String insertSql = queryBuilder.prepareInsertSql();
        PreparedStatement stmt;
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
                    logger.warn("Column count mismatch in DB import: " + data.length + " vs. " + colNames.length);
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
            logger.error("SQL error in data import.", e);
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
            logger.error("Could not close export file in data import.", e);
        }
    }


    /**
     * Creates a table in the database for the import
     * @throws SQLException
     */
    private void createSchema() throws SQLException {
        Statement stmt = conn.createStatement();
        logger.debug("Creating table " + def.getTableName());
        String sql = queryBuilder.prepareCreateTableSql();
        stmt.executeUpdate(sql);

        List<String> indices = def.getIndices();
        for (String index : indices) {
            sql = queryBuilder.prepareCreateIndexSql(index);
            try {
                stmt.executeUpdate(sql);
            } catch (MySQLSyntaxErrorException ignored) {
                /* ignored */
            }
        }
        stmt.close();
    }

}
