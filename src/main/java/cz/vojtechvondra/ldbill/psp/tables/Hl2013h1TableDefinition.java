package cz.vojtechvondra.ldbill.psp.tables;

import cz.vojtechvondra.ldbill.psp.TableDefinition;

public class Hl2013h1TableDefinition extends TableDefinition {
    public Hl2013h1TableDefinition() {
        tableName = "hl_poslanec";
        colNames = new String[]{
                "id_poslanec",
                "id_hlasovani",
                "vysledek"
        };
    }
}
