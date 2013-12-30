package cz.vojtechvondra.ldbill.psp.tables;

public class Tisky_zaTableDefinition extends TableDefinition {
    public Tisky_zaTableDefinition() {
        tableName = "tisky_za";
        colNames = new String[]{
                "id_tisk",
                "cislo_za",
                "id_hist",
                "id_druh",
                "nazev_za",
                "uplny_nazev_za",
                "rozeslano",
                "id_org",
                "usn_vybort",
                "id_posl",
                "t_url",
                "id_vysledek",
                "cisla_post",
                "sort_it",
                "rozeslano_minuty",
                "status"
        };
    }
}
