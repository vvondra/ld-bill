package cz.vojtechvondra.ldbill.psp.tables;

import cz.vojtechvondra.ldbill.psp.TableDefinition;

public class Hl2002h2TableDefinition extends TableDefinition {
    public Hl2002h2TableDefinition() {
        tableName = "hl_poslanec";
        colNames = new String[]{
                "id_poslanec",
                "id_hlasovani",
                "vysledek"
        };
    }
}
