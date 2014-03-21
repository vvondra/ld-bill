package cz.vojtechvondra.ldbill.psp.tables;

import cz.vojtechvondra.ldbill.psp.TableDefinition;

public class TiskyTableDefinition extends TableDefinition {
    public TiskyTableDefinition() {
        tableName = "tisky";
        colNames = new String[]{
                "id_tisk",
                "id_druh",
                "id_stav",
                "ct",
                "cislo_za",
                "id_navrh",
                "id_org",
                "od_org_obdobi",
                "id_osoba",
                "navrhovatel",
                "nazev_tisku",
                "predlozeno",
                "rozeslano",
                "dalsi_projednani",
                "nepouzivano",
                "uplny_nazev",
                "zm_lhuty",
                "lhuta",
                "rj",
                "text_url",
                "is_eu",
                "rozeslano_minuty",
                "duvera_vlade",
                "status"
        };
    }
}
