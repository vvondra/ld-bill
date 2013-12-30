package cz.vojtechvondra.ldbill.psp.tables;

public class Hl2010hTableDefinition extends TableDefinition {
    public Hl2010hTableDefinition() {
        tableName = "hl2010h";
        colNames = new String[]{
                "id_poslanec",
                "id_hlasovani",
                "vysledek"
        };
    }
}
