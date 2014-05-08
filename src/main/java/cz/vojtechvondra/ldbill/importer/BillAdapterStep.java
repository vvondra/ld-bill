package cz.vojtechvondra.ldbill.importer;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.DCTerms;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;
import cz.vojtechvondra.ldbill.vo.BillRevision;
import cz.vojtechvondra.ldbill.vo.Vote;
import cz.vojtechvondra.ldbill.vocabulary.Bill;
import cz.vojtechvondra.ldbill.vocabulary.FRBR;
import org.apache.log4j.Logger;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class BillAdapterStep extends JdbcImportStep {

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
                    int termId = results.getInt("od_org_obdobi");
                    if (!isContemporaryParliament(termId)) {
                        logger.debug("Historical bill, skipping");
                        continue;
                    }
                    cz.vojtechvondra.ldbill.vo.Bill bill = new cz.vojtechvondra.ldbill.vo.Bill(
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
     * @param termId Parliamentary term ID
     * @return True if the parliament is from the modern history of the Czech Republic
     */
    private boolean isContemporaryParliament(int termId) {
        return contemporaryParliamentIds.contains(termId);
    }

    /**
     * Creates a RDF resource for a bill and adds it to the model
     * @param bill Bill entity with data
     */
    private void addBillToModel(cz.vojtechvondra.ldbill.vo.Bill bill) {
        logger.debug("Importing bill no. " + bill.getIdent());
        Resource b = currentModel.createResource(bill.getRdfUri());
        b.addProperty(RDF.type, Bill.Bill);
        b.addProperty(DCTerms.identifier, bill.getIdent());
        b.addProperty(DCTerms.title, bill.getTitle());
        b.addProperty(DCTerms.date, currentModel.createTypedLiteral(
                        dateFormatter.format(bill.getIntroductionDate()),
                        XSDDatatype.XSDdate
                )
        );
        b.addProperty(DCTerms.description, bill.getDescription());
        b.addProperty(RDFS.seeAlso, "http://www.psp.cz/sqw/historie.sqw?o=6&t=" + bill.getNumber());
        b.addProperty(Bill.billSponsor, bill.getBillSponsor());
    }

    /**
     * Adds all revisions of a bill to the model
     * @param billDbId ID of the bill in the temporary database
     * @param bill Bill entity with data
     */
    private void addBillRevisionsToModel(int billDbId, cz.vojtechvondra.ldbill.vo.Bill bill) {
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
                r.addProperty(DCTerms.title, rev.getTitle());
                r.addProperty(DCTerms.date, currentModel.createTypedLiteral(dateFormatter.format(rev.getDate()), XSDDatatype.XSDdate));
                r.addProperty(FRBR.realizationOf, currentModel.createResource(rev.getBill().getRdfUri()));
                if (previousRevision != null) {
                    r.addProperty(FRBR.revisionOf, currentModel.createResource(previousRevision.getRdfUri()));
                }
                r.addProperty(Bill.outcome, rev.getOutcome());
                r.addProperty(Bill.legislativeProcessStage, rev.getStage());

                // Check if bill has been enacted
                String collNo = results.getString("zaver_sb_cislo");
                if (collNo.length() > 0) {
                    logger.debug("Bill revisions enacted as: " + collNo);
                    // Add enaction to bill
                    currentModel
                            .createResource(rev.getBill().getRdfUri())
                            .addProperty(Bill.enaction, getAct(rev.getDate(), collNo));
                }

                String voteId = results.getString("id_hlas");
                if (voteId.length() > 0 && !voteId.equals("0")) {
                    logger.debug("Bill revisions decided by vote: " + voteId);
                    Vote v = new Vote(voteId);
                    Resource voteRes = currentModel.createResource(v.getRdfUri());
                    r.addProperty(Bill.decidedBy, voteRes);
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
        return "SELECT * FROM TISKY WHERE id_druh = 2 OR id_druh = 1 ORDER BY id_tisk DESC";
    }

    private String getBillRevisionsSqlSelect() {
        return "SELECT HIST.*, TISKY_ZA.*, t1.popis, t1.id_typ, TYP_AKCE.popis as akce, cislo as cislo_hlas, HL_HLASOVANI.datum as datum_hlasovani, vysledek, nazev_kratky, nazev_dlouhy, id_hlas\n" +
                "FROM HIST\n" +
                "JOIN PRECHODY ON PRECHODY.id_prechod = HIST.id_prechod\n" +
                "LEFT JOIN TISKY_ZA ON TISKY_ZA.id_hist = HIST.id_hist\n" +
                "JOIN STAVY AS s1 ON odkud = s1.id_stav\n" +
                "JOIN STAVY AS s2 ON kam = s2.id_stav\n" +
                "JOIN TYP_STAVU AS t1 ON s1.id_typ = t1.id_typ\n" +
                "JOIN TYP_STAVU AS t2 ON s2.id_typ = t2.id_typ\n" +
                "JOIN TYP_AKCE ON TYP_AKCE.id_akce = PRECHODY.id_akce\n" +
                "LEFT JOIN HL_HLASOVANI ON HIST.id_hlas = id_hlasovani\n" +
                "WHERE HIST.id_tisk = ?\n" +
                "ORDER BY HIST.id_hist";
    }

    /**
     * A list of IDs of modern-time Parliamentary terms for the czech Parliament
     */
    private static final List<Integer> contemporaryParliamentIds;

    static {
        contemporaryParliamentIds = new ArrayList<>();
        contemporaryParliamentIds.add(165);
        contemporaryParliamentIds.add(166);
        contemporaryParliamentIds.add(167);
        contemporaryParliamentIds.add(168);
        contemporaryParliamentIds.add(169);
        contemporaryParliamentIds.add(170);
        contemporaryParliamentIds.add(171);
    }

}
