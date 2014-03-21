package cz.vojtechvondra.ldbill.psp.tables;

import cz.vojtechvondra.ldbill.psp.TableDefinition;

public class Typ_akceTableDefinition extends TableDefinition {
    public Typ_akceTableDefinition() {
        tableName = "typ_akce";
        colNames = new String[]{
                "id_akce",
                "popis"
        };
    }
}
