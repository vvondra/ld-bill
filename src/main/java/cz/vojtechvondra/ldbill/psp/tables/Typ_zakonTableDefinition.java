package cz.vojtechvondra.ldbill.psp.tables;

import cz.vojtechvondra.ldbill.psp.TableDefinition;

public class Typ_zakonTableDefinition extends TableDefinition {
    public Typ_zakonTableDefinition() {
        tableName = "typ_zakon";
        colNames = new String[]{
                "id_navrh",
                "druh_navrhovatele"
        };
    }
}
