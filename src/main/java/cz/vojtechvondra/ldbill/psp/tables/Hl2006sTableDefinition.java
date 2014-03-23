package cz.vojtechvondra.ldbill.psp.tables;

import cz.vojtechvondra.ldbill.psp.TableDefinition;

public class Hl2006sTableDefinition extends TableDefinition {
    public Hl2006sTableDefinition() {
        tableName = "hl_hlasovani";
        colNames = new String[]{
                "id_hlasovani",
                "id_organ",
                "schuze",
                "cislo",
                "bod",
                "datum",
                "cas",
                "pro",
                "proti",
                "zdrzel",
                "nehlasoval",
                "prihlaseno",
                "kvorum",
                "druh_hlasovani",
                "vysledek",
                "nazev_dlouhy",
                "nazev_kratky"
        };
    }
}