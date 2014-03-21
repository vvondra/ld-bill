package cz.vojtechvondra.ldbill.psp.tables;

import cz.vojtechvondra.ldbill.psp.TableDefinition;

public class PrechodyTableDefinition extends TableDefinition {
    public PrechodyTableDefinition() {
        tableName = "prechody";
        colNames = new String[]{
                "id_prechod",
                "odkud",
                "kam",
                "id_akce",
                "typ_prechodu"
        };
    }
}
