package cz.vojtechvondra.ldbill.psp.tables;


abstract public class TableDefinition {

    protected String tableName;

    protected String[] colNames;

    public String getTableName() {
        return tableName.toUpperCase();
    }

    public String[] getColNames() {
        return colNames;
    }

    public String getColSqlDefinitions() {
        StringBuilder colDef = new StringBuilder();
        for (int i = 0; i < colNames.length; i++) {
            colDef.append(colNames[i]).append(" CLOB");
            if (i != colNames.length - 1) {
                colDef.append(",\n");
            }
        }
        return colDef.toString();
    }
}
