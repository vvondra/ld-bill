package cz.vojtechvondra.ldbill.psp.tables;

public class StavyTableDefinition extends TableDefinition {
    public StavyTableDefinition() {
        tableName = "stavy";
        colNames = new String[]{
                "id_stav",
                "id_typ",
                "id_druh",
                "popis",
                "lhuta",
                "lhuta_where"
        };
    }
}
