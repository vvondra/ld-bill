package cz.vojtechvondra.ldbill.importer;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.DC;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;
import cz.vojtechvondra.ldbill.entity.Bill;
import cz.vojtechvondra.ldbill.vocabulary.LB;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class BillAdapterStep implements Step {
    private final Connection connection;
    private final Model currentModel;
    static Logger logger = Logger.getLogger(BillAdapterStep.class);


    public BillAdapterStep(Connection connection, Model currentModel) {
        this.connection = connection;
        this.currentModel = currentModel;
    }

    @Override
    public Model extendModel() {
        Statement stmt;
        try {
            stmt = connection.createStatement();
            ResultSet results = stmt.executeQuery(getBillsSqlSelect());
            while (results.next()) {
                try {
                    addBillToModel(new Bill(
                            results.getString("ct"),
                            results.getString("predlozeno"),
                            results.getString("nazev_tisku"),
                            results.getString("uplny_nazev"),
                            results.getString("id_navrh")
                    ));
                } catch (IllegalArgumentException e) {
                    logger.warn("Invalid bill in import.", e);
                }
            }
        } catch (SQLException e) {
            logger.error("Could not extend RDF model with bills.", e);
        }

        return currentModel;
    }

    private void addBillToModel(Bill bill) {
        logger.debug("Importing bill no. " + bill.getIdent());
        Resource b = currentModel.createResource(bill.getRdfUri());
        b.addProperty(RDF.type, LB.Bill);
        b.addProperty(DC.identifier, bill.getIdent());
        b.addProperty(DC.title, bill.getTitle());
        b.addProperty(DC.description, bill.getDescription());
        b.addProperty(RDFS.seeAlso, "http://www.psp.cz/sqw/historie.sqw?o=6&t=" + bill.getNumber());
        b.addProperty(LB.billSponsor, bill.getBillSponsor());
    }

    private String getBillsSqlSelect() {
        return "SELECT * FROM tisky WHERE id_druh = 2 OR id_druh = 1 ORDER BY id_tisk DESC LIMIT 100"; // TODO 100
    }

    private String getBillRevisionsSqlSelect() {
        return "SELECT hist.*, tisky_za.*, t1.popis, t1.id_typ, typ_akce.popis as akce, cislo as cislo_hlas, hl2010s.datum as datum_hlasovani, vysledek, nazev_kratky, nazev_dlouhy, id_hlas\n" +
                "FROM hist\n" +
                "JOIN prechody USING(id_prechod)\n" +
                "LEFT JOIN tisky_za ON tisky_za.id_hist = hist.id_hist\n" +
                "JOIN stavy AS s1 ON odkud = s1.id_stav\n" +
                "JOIN stavy AS s2 ON kam = s2.id_stav\n" +
                "JOIN typ_stavu AS t1 ON s1.id_typ = t1.id_typ\n" +
                "JOIN typ_stavu AS t2 ON s2.id_typ = t2.id_typ\n" +
                "JOIN typ_akce USING(id_akce)\n" +
                "LEFT JOIN hl2010s ON hist.id_hlas = id_hlasovani\n" +
                "WHERE hist.id_tisk = \"%d\"\n" +
                "ORDER BY hist.id_hist";
    }
}
