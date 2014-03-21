package cz.vojtechvondra.ldbill.psp.tables;

import cz.vojtechvondra.ldbill.psp.TableDefinition;

public class OsobyTableDefinition extends TableDefinition {
    public OsobyTableDefinition() {
        tableName = "osoby";
        colNames = new String[]{
                "id_osoba",
                "pred",
                "jmeno",
                "prijmeni",
                "za",
                "narozeni",
                "pohlavi",
                "zmena",
                "umrti"
        };
    }
}
