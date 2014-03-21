package cz.vojtechvondra.ldbill.psp.tables;

import cz.vojtechvondra.ldbill.psp.TableDefinition;

public class ZarazeniTableDefinition extends TableDefinition {
    public ZarazeniTableDefinition() {
        tableName = "zarazeni";
        colNames = new String[]{
                "id_osoba",
                "id_of",
                "cl_funkce",
                "od_o",
                "do_o",
                "od_f",
                "do_f"
        };
    }
}
