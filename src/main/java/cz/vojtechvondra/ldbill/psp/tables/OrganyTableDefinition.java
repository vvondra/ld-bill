package cz.vojtechvondra.ldbill.psp.tables;

import cz.vojtechvondra.ldbill.psp.TableDefinition;

public class OrganyTableDefinition extends TableDefinition {
    public OrganyTableDefinition() {
        tableName = "organy";
        colNames = new String[]{
                "id_organ",
                "parent",
                "id_typ_organu",
                "zkratka",
                "nazev_cz",
                "nazev_en",
                "od_organ",
                "do_organ",
                "priorita",
                "cl_organ_base"
        };
    }
}
