package cz.vojtechvondra.ldbill.vo;

import com.hp.hpl.jena.rdf.model.Property;
import cz.vojtechvondra.ldbill.vocabulary.Decisions;
import org.apache.log4j.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Represents a vote
 * without detailed ballot information, only vote counts
 */
public class Vote implements Entity {

    private String voteId;
    private final String sessionNumber;
    private final String voteNumber;
    private final Date date;
    private final int supportersCount;
    private final int opponentsCount;
    private final int abstaineeCount;
    private final int didNotVoteCount;
    private final int presentCount;
    private final int quorum;
    private final String result;
    private final String title;
    private final String description;

    static Logger logger = Logger.getLogger(Vote.class);

    public Vote(int voteId) {
        this(Integer.toString(voteId));
    }

    /**
     * Default constructor without additional voting data
     * @param voteId ID of vote in psp.cz db
     */
    public Vote(String voteId) {
        this.voteId = voteId;
        sessionNumber = "";
        voteNumber = "";
        date = new Date();
        supportersCount = 0;
        opponentsCount = 0;
        abstaineeCount = 0;
        didNotVoteCount = 0;
        presentCount = 0;
        quorum = 0;
        result = "";
        title = "";
        description = "";
    }

    /**
     * @param voteId ID of vote in psp.cz db
     * @param sessionNumber Parliamentary session number {@link cz.vojtechvondra.ldbill.vo.Parliament}
     * @param voteNumber Ordinal number of vote
     * @param date Date of vote (dd.MM.yyyy)
     * @param supportersCount How many people voted for the proposal
     * @param opponentsCount How many people voted against the proposal
     * @param abstaineeCount How many people chose not to vote
     * @param didNotVoteCount How many people chose not to vote or were not present at voting
     * @param presentCount How many people were present
     * @param quorum How many votes is necessary to pass the vote
     * @param result Vote result (Accepted/Refused/Not Valid)
     * @param title Name of the vote
     * @param description Description of the issue which the vote decided
     */
    public Vote(String voteId, String sessionNumber, String voteNumber, String date, int supportersCount, int opponentsCount, int abstaineeCount, int didNotVoteCount, int presentCount, int quorum, String result, String title, String description) {
        this.sessionNumber = sessionNumber;
        this.voteNumber = voteNumber;
        this.supportersCount = supportersCount;
        this.opponentsCount = opponentsCount;
        this.abstaineeCount = abstaineeCount;
        this.didNotVoteCount = didNotVoteCount;
        this.presentCount = presentCount;
        this.quorum = quorum;
        this.result = result;
        this.title = title;
        this.description = description;
        this.voteId = voteId;

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

        try {
            this.date = sdf.parse(date);
        } catch (ParseException e) {
            logger.warn("Could not parse date of vote: " + date);
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public String getRdfUri() {
        return RESOURCE_URI_PREFIX +"psp.cz/poll/" + voteId;
    }

    public String getSessionNumber() {
        return sessionNumber;
    }

    public String getVoteNumber() {
        return voteNumber;
    }

    public Date getDate() {
        return date;
    }

    public int getSupportersCount() {
        return supportersCount;
    }

    public int getOpponentsCount() {
        return opponentsCount;
    }

    public int getAbstaineeCount() {
        return abstaineeCount;
    }

    public int getDidNotVoteCount() {
        return didNotVoteCount;
    }

    public int getPresentCount() {
        return presentCount;
    }

    public int getQuorum() {
        return quorum;
    }

    public String getResult() {
        return result;
    }

    public Property getResultConcept() {
        switch (result) {
            case "A":
                return Decisions.Accepts;
            case "R":
                return Decisions.Refuses;
            default:
                return Decisions.Spoilt;
        }
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Maps possible types of casting a ballot
     */
    public static String vote(String vote) {
        switch (vote) {
            case "@":
            case "M":
                return "hasAbsentee";
            case "F":
            case "C":
                return "hasAbstainee";
            case "A":
                return "hasOpponent";
            case "B":
                return "hasSupporter";
        }

        throw new IllegalArgumentException("Unknown vote type: " + vote);
    }

}
