package cz.vojtechvondra.ldbill.psp.tables;

import cz.vojtechvondra.ldbill.psp.TableDefinition;

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
