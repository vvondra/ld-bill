package cz.vojtechvondra.ldbill.psp.tables;

import cz.vojtechvondra.ldbill.psp.TableDefinition;

public class Hl1998h3TableDefinition extends TableDefinition {
    public Hl1998h3TableDefinition() {
        tableName = "hl_poslanec";
        colNames = new String[] {
            "id_poslanec",
            "id_hlasovani",
            "vysledek"
        };
    }
}