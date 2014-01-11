package cz.vojtechvondra.ldbill.importer;

import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.vocabulary.DC;
import com.hp.hpl.jena.vocabulary.RDF;
import cz.vojtechvondra.ldbill.Utils;
import cz.vojtechvondra.ldbill.vo.Person;
import cz.vojtechvondra.ldbill.vo.Vote;
import cz.vojtechvondra.ldbill.vocabulary.Bill;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class VoteStep extends H2Import {

    private static Logger logger = Logger.getLogger(VoteStep.class);
    private final DateFormat dateFormatter;

    public VoteStep(Connection connection, Model currentModel) {
        super(connection, currentModel);
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
    }

    /**
     * For every known vote, extract the individual ballots from the database
     * @return model extended with vote ballots and complete vote resources
     */
    @Override
    public Model extendModel() {
        StmtIterator stmtIterator = currentModel.listStatements(null, Bill.decidedBy, (RDFNode) null);
        while (stmtIterator.hasNext()) {
            Statement stmt = stmtIterator.nextStatement();
            RDFNode voteNode = stmt.getObject();
            // Expand only resources, there should be no literal nodes
            if (voteNode.isResource()) {
                Resource vote = voteNode.asResource();
                int voteDbId = Integer.parseInt(vote.getURI().substring(vote.getURI().lastIndexOf("/") + 1));
                Vote v = loadVoteById(voteDbId);
                if (v == null) {
                    logger.warn("Could not load vote for ID: " + voteDbId);
                    continue;
                }
                vote.addProperty(RDF.type, Bill.VoteInParliament);
                vote.addProperty(DC.date, dateFormatter.format(v.getDate()));
                vote.addProperty(DC.title, v.getTitle());
                vote.addProperty(DC.description, v.getDescription());
                vote.addProperty(Bill.supporterCount, Integer.toString(v.getSupportersCount()));
                vote.addProperty(Bill.opponentCount, Integer.toString(v.getOpponentsCount()));
                vote.addProperty(Bill.abstainedCount, Integer.toString(v.getAbstaineeCount() + v.getDidNotVoteCount()));
                vote.addProperty(Bill.quorum, Integer.toString(v.getQuorum()));
                vote.addProperty(Bill.decision, v.getResultConcept());
               //addBallotsToVote(voteDbId, v);
            }
        }

        return currentModel;
    }

    private void addBallotsToVote(int voteDbId, Vote vote) {
        PreparedStatement stmt;
        try {
            stmt = connection.prepareStatement(getIndividualVotesSqlSelect());
            stmt.setInt(1, voteDbId);
            ResultSet results = stmt.executeQuery();
            while (results.next()) {
                String ballot = results.getString("vysledek");
                Property p = null;
                Person voter = new Person(results.getString("id_osoba"));
                switch (ballot) {
                    case "A":
                        p = Bill.hasSupporter;
                        break;
                    case "B":
                        p = Bill.hasOpponent;
                        break;
                    case "F":
                    case "C":
                        p = Bill.hasAbstainee;
                        break;
                    case "@":
                    case "M":
                        p = Bill.hasAbsentee;
                        break;
                }

                if (p != null) {
                    currentModel.createResource(vote.getRdfUri())
                            .addProperty(p, currentModel.createResource(voter.getRdfUri()));
                } else {
                    logger.debug("Unknown voting result:" + ballot);
                }
            }
        } catch (SQLException e) {
            logger.warn("SQL exception during fetching of ballots", e);
        }
    }

    private Vote loadVoteById(int voteDbId) {
        Vote v = null;
        PreparedStatement stmt;
        try {
            stmt = connection.prepareStatement(getVoteSelect());
            stmt.setInt(1, voteDbId);
            stmt.setMaxRows(1);
            ResultSet results = stmt.executeQuery();
            if (results.next()) {
                v = new Vote(
                        results.getString("id_hlasovani"),
                        results.getString("schuze"),
                        results.getString("cislo"),
                        results.getString("datum"),
                        results.getInt("pro"),
                        results.getInt("proti"),
                        results.getInt("zdrzel"),
                        results.getInt("nehlasoval"),
                        results.getInt("prihlaseno"),
                        results.getInt("kvorum"),
                        results.getString("vysledek"),
                        results.getString("nazev_kratky"),
                        results.getString("nazev_dlouhy")
                );
            }
        } catch (SQLException e) {
            v = null;
            logger.warn("SQL exception during fetching of vote", e);
        }
        return v;
    }

    private String getVoteSelect() {
        return "SELECT *\n" +
                "FROM hlasovani\n" +
                "WHERE id_hlasovani = ?";
    }

    private String getIndividualVotesSqlSelect() {
        return "SELECT *\n" +
                "FROM hl_poslanec\n" +
                "JOIN poslanec ON %1$s.id_poslanec = poslanec.id_poslanec\n" +
                "WHERE id_hlasovani = ?";
    }
}
