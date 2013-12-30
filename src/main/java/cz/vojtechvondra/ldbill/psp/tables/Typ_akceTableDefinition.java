package cz.vojtechvondra.ldbill.psp.tables;

public class Typ_akceTableDefinition extends TableDefinition {
    public Typ_akceTableDefinition() {
        tableName = "typ_akce";
        colNames = new String[]{
                "id_akce",
                "popis"
        };
    }
}
