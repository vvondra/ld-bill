package cz.vojtechvondra.ldbill.psp;

/**
 * Creates SQL queries for creating schemas and inserting data
 * Each instance works on one table
 */
public class SQLQueryBuilder {

    /**
     * Definition of the table which queries are prepared for
     */
    private final TableDefinition tableDefinition;

    public SQLQueryBuilder(TableDefinition definition) {
        this.tableDefinition = definition;
    }

    /**
     * Prepare the SQL statement for a single export row
     * @return SQL INSERT string for prepared statement
     */
    String prepareInsertSql() {
        String[] colNames = tableDefinition.getColNames();
        StringBuilder insertSql = new StringBuilder();
        insertSql.append("INSERT INTO ").append(tableDefinition.getTableName()).append("(");
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
     * @return SQL query for creating the table
     */
    String prepareCreateTableSql() {
        return String.format("CREATE TABLE IF NOT EXISTS %s (%s)",
                tableDefinition.getTableName(), getColSqlDefinitions());
    }

    /**
     * @return The column definition part of the CREATE TABLE DDL statement
     */
    String getColSqlDefinitions() {
        StringBuilder colDef = new StringBuilder();
        for (int i = 0; i < tableDefinition.colNames.length; i++) {
            colDef.append(tableDefinition.colNames[i]);
            if (tableDefinition.isNumericCol(tableDefinition.colNames[i])) {
                colDef.append(" INT");
            } else {
                colDef.append(" TEXT");
            }
            if (i != tableDefinition.colNames.length - 1) {
                colDef.append(",\n");
            }
        }
        return colDef.toString();
    }

    /**
     * @param index column name
     * @return SQL query to create an index on a column
     */
    String prepareCreateIndexSql(String index) {
        return String.format("CREATE INDEX %s ON %s(%s)",
                getIndexName(tableDefinition.getTableName(), index), tableDefinition.getTableName(), index);
    }

    /**
     * @param tableName Table name in which to create index
     * @param colName   Column name which is part of the index
     * @return Generated index name from table and column
     */
    String getIndexName(String tableName, String colName) {
        return ("IDX" + tableName + colName).toUpperCase();
    }
}