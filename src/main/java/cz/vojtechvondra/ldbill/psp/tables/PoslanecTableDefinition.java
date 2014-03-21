package cz.vojtechvondra.ldbill.psp.tables;

import cz.vojtechvondra.ldbill.psp.TableDefinition;

public class PoslanecTableDefinition extends TableDefinition {
    public PoslanecTableDefinition() {
        tableName = "poslanec";
        colNames = new String[]{
                "id_poslanec",
                "id_osoba",
                "id_kraj",
                "id_kandidatka",
                "id_obdobi",
                "web",
                "ulice",
                "obec",
                "psc",
                "email",
                "telefon",
                "fax",
                "psp_tel",
                "facebook",
                "foto"
        };
    }
}
