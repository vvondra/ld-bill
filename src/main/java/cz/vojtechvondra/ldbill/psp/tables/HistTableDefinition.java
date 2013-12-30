package cz.vojtechvondra.ldbill.psp.tables;

public class HistTableDefinition extends TableDefinition {
    public HistTableDefinition() {
        tableName = "hist";
        colNames = new String[]{
                "id_hist",
                "id_tisk",
                "datum",
                "id_hlas",
                "id_prechod",
                "id_bod",
                "schuze",
                "usnes_ps",
                "orgv_id_posl",
                "ps_id_posl",
                "orgv_p_usn",
                "zaver_pubik",
                "zaver_sb_castka",
                "zaver_sb_cislo",
                "poznamka"
        };
    }
}
