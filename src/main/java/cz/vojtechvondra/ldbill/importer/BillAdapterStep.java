package cz.vojtechvondra.ldbill.importer;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.DC;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;
import cz.vojtechvondra.ldbill.entity.Bill;
import cz.vojtechvondra.ldbill.entity.BillRevision;
import cz.vojtechvondra.ldbill.entity.Vote;
import cz.vojtechvondra.ldbill.vocabulary.FRBR;
import cz.vojtechvondra.ldbill.vocabulary.LB;
import org.apache.log4j.Logger;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class BillAdapterStep extends H2Import {

    /**
     * Prefix for RDF Act resources from the lex ontology
     */
    public static final String LEX_ONTOLOGY_ACT_URI_PREFIX = "http://linked.opendata.cz/resource/legislation/cz/act/";

    private final SimpleDateFormat dateFormatter;

    private static Logger logger = Logger.getLogger(BillAdapterStep.class);

    /**
     * @inheritDoc
     */
    public BillAdapterStep(Connection connection, Model currentModel) {
        super(connection, currentModel);
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
    }

    /**
     * Adds bills and bill revisions to the current model
     * @return the extended Model
     */
    @Override
    public Model extendModel() {
        Statement stmt;
        try {
            stmt = connection.createStatement();
            ResultSet results = stmt.executeQuery(getBillsSqlSelect());
            while (results.next()) {
                try {
                    Bill bill = new Bill(
                            results.getString("ct"),
                            results.getString("predlozeno"),
                            results.getString("nazev_tisku"),
                            results.getString("uplny_nazev"),
                            results.getString("id_navrh")
                    );
                    addBillToModel(bill);
                    addBillRevisionsToModel(results.getInt("id_tisk"), bill);
                } catch (IllegalArgumentException e) {
                    logger.warn("Invalid bill in import.", e);
                }
            }
        } catch (SQLException e) {
            logger.error("Could not extend RDF model with bills.", e);
        }

        return currentModel;
    }

    /**
     * Creates a RDF resource for a bill and adds it to the model
     * @param bill Bill entity with data
     */
    private void addBillToModel(Bill bill) {
        logger.debug("Importing bill no. " + bill.getIdent());
        Resource b = currentModel.createResource(bill.getRdfUri());
        b.addProperty(RDF.type, LB.Bill);
        b.addProperty(DC.identifier, bill.getIdent());
        b.addProperty(DC.title, bill.getTitle());
        b.addProperty(DC.date, dateFormatter.format(bill.getIntroductionDate()));
        b.addProperty(DC.description, bill.getDescription());
        b.addProperty(RDFS.seeAlso, "http://www.psp.cz/sqw/historie.sqw?o=6&t=" + bill.getNumber());
        b.addProperty(LB.billSponsor, bill.getBillSponsor());
    }

    /**
     * Adds all revisions of a bill to the model
     * @param billDbId ID of the bill in the temporary database
     * @param bill Bill entity with data
     */
    private void addBillRevisionsToModel(int billDbId, Bill bill) {
        PreparedStatement stmt;
        try {
            stmt = connection.prepareStatement(getBillRevisionsSqlSelect());
            stmt.setInt(1, billDbId);
            ResultSet results = stmt.executeQuery();
            BillRevision previousRevision = null;

            // Loop through all revisions
            while (results.next()) {
                BillRevision rev = new BillRevision(
                        bill, results.getRow(),
                        results.getString("popis"),
                        results.getString("datum"),
                        results.getString("id_typ"),
                        results.getString("akce"));
                Resource r = currentModel.createResource(rev.getRdfUri());
                r.addProperty(RDF.type, FRBR.Expression);
                r.addProperty(DC.title, rev.getTitle());
                r.addProperty(DC.date, dateFormatter.format(rev.getDate()));
                r.addProperty(FRBR.realizationOf, rev.getBill().getRdfUri());
                if (previousRevision != null) {
                    r.addProperty(FRBR.revisionOf, currentModel.createResource(previousRevision.getRdfUri()));
                }
                r.addProperty(LB.outcome, rev.getOutcome());
                r.addProperty(LB.legislativeProcessStage, rev.getStage());

                // Check if bill has been enacted
                String collNo = results.getString("zaver_sb_cislo");
                if (collNo.length() > 0) {
                    // Add enaction to bill
                    currentModel
                            .createResource(rev.getBill().getRdfUri())
                            .addProperty(LB.enaction, getAct(rev.getDate(), collNo));
                }

                int voteId = results.getInt("id_hlas");
                if (voteId > 0) {
                    Vote v = new Vote(voteId);
                    r.addProperty(LB.decidedBy, currentModel.createResource(v.getRdfUri()));
                }

                // Set the previous revision to be the current one for the next loop iteration
                previousRevision = rev;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns a RDF resource from the lex ontology with an act
     * @param date Date of bill enaction
     * @param no Collection number of the Act
     * @return RDF resources representing the act from the Lex ontology
     */
    private Resource getAct(java.util.Date date, String no) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);
        return currentModel.createResource(
                String.format("%s%s/%s-%s", LEX_ONTOLOGY_ACT_URI_PREFIX, year, no, year)
        );
    }

    private String getBillsSqlSelect() {
        return "SELECT * FROM tisky WHERE id_druh = 2 OR id_druh = 1 ORDER BY id_tisk DESC LIMIT 100"; // TODO 100
    }

    private String getBillRevisionsSqlSelect() {
        return "SELECT hist.*, tisky_za.*, t1.popis, t1.id_typ, typ_akce.popis as akce, cislo as cislo_hlas, hl2010s.datum as datum_hlasovani, vysledek, nazev_kratky, nazev_dlouhy, id_hlas\n" +
                "FROM hist\n" +
                "JOIN prechody ON prechody.id_prechod = hist.id_prechod\n" +
                "LEFT JOIN tisky_za ON tisky_za.id_hist = hist.id_hist\n" +
                "JOIN stavy AS s1 ON odkud = s1.id_stav\n" +
                "JOIN stavy AS s2 ON kam = s2.id_stav\n" +
                "JOIN typ_stavu AS t1 ON s1.id_typ = t1.id_typ\n" +
                "JOIN typ_stavu AS t2 ON s2.id_typ = t2.id_typ\n" +
                "JOIN typ_akce ON typ_akce.id_akce = prechody.id_akce\n" +
                "LEFT JOIN hl2010s ON hist.id_hlas = id_hlasovani\n" +
                "WHERE hist.id_tisk = ?\n" +
                "ORDER BY hist.id_hist";
    }
}
