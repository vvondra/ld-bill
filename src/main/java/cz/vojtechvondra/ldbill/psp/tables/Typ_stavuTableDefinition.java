package cz.vojtechvondra.ldbill.psp.tables;

import cz.vojtechvondra.ldbill.psp.TableDefinition;

public class Typ_stavuTableDefinition extends TableDefinition {
    public Typ_stavuTableDefinition() {
        tableName = "typ_stavu";
        colNames = new String[]{
                "id_typ",
                "popis"
        };
    }
}
