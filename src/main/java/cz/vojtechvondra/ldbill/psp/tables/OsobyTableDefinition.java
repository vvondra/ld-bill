package cz.vojtechvondra.ldbill.psp.tables;

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
