package cz.vojtechvondra.ldbill.psp.tables;

import cz.vojtechvondra.ldbill.psp.TableDefinition;

public class Hl1996h1TableDefinition extends TableDefinition {
    public Hl1996h1TableDefinition() {
        tableName = "hl_poslanec";
        colNames = new String[] {
            "id_poslanec",
            "id_hlasovani",
            "vysledek"
        };
    }
}
