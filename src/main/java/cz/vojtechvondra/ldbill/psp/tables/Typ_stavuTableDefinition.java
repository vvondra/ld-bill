package cz.vojtechvondra.ldbill.psp.tables;

public class Typ_stavuTableDefinition extends TableDefinition {
    public Typ_stavuTableDefinition() {
        tableName = "typ_stavu";
        colNames = new String[]{
                "id_typ",
                "popis"
        };
    }
}
