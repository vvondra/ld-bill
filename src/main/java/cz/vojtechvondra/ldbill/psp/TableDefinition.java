package cz.vojtechvondra.ldbill.psp;

import java.util.ArrayList;
import java.util.List;

/**
 * Base class for table metadata definitions
 */
abstract public class TableDefinition {

    /**
     * Name of the table for which the definition is provided
     */
    protected String tableName;

    /**
     * Names of columns in the table
     */
    protected String[] colNames;

    /**
     * @return Normalized form of the table name
     */
    public String getTableName() {
        return tableName.toUpperCase();
    }

    /**
     * Factory method for returning a table definition class instance base on table name
     *
     * @param name DB Table name
     * @return DB Table definition
     * @throws ReflectiveOperationException when table definition for the given name does not exist
     */
    public static TableDefinition factory(String name) throws ReflectiveOperationException {
        char first = Character.toUpperCase(name.charAt(0));
        name = first + name.substring(1);
        return (TableDefinition)
                Class.forName(TableDefinition.class.getPackage().getName() + ".tables." + name + "TableDefinition")
                        .newInstance();
    }

    /**
     * @return An array of names of the columns in the table
     */
    public String[] getColNames() {
        return colNames;
    }

    /**
     * @return The column definition part of the CREATE TABLE DDL statement
     */
    public String getColSqlDefinitions() {
        StringBuilder colDef = new StringBuilder();
        for (int i = 0; i < colNames.length; i++) {
            colDef.append(colNames[i]);
            if (isNumericCol(colNames[i])) {
                colDef.append(" INT");
            } else {
                colDef.append(" TEXT");
            }
            if (i != colNames.length - 1) {
                colDef.append(",\n");
            }
        }
        return colDef.toString();
    }

    /**
     * Returns the columns on which an index should be created
     *
     * @return a list of index columns
     */
    public List<String> getIndices() {
        List<String> indices = new ArrayList<>();
        for (String colName : colNames) {
            if (isNumericCol(colName)) {
                indices.add(colName);
            }
        }
        return indices;
    }

    /**
     * Determines if the current column should be numeric
     * @param name Column name
     * @return true if the column is numeric
     */
    public boolean isNumericCol(String name) {
        return name.length() > 3 && name.substring(0, 3).equals("id_");
    }
}
