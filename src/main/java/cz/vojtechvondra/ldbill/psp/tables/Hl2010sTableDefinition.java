package cz.vojtechvondra.ldbill.psp.tables;

public class Hl2010sTableDefinition extends TableDefinition {
    public Hl2010sTableDefinition() {
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
