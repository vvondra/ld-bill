package cz.vojtechvondra.ldbill.vocabulary;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;

public final class Bill {

    protected static final String uri = "http://linked.opendata.cz/resource/legislation/bill#";

    /**
     * @return the URI for this schema
     */
    public static String getURI() {
        return uri;
    }

    private static final Model m = ModelFactory.createDefaultModel();

    public static final Resource Bill = m.createProperty(uri, "Bill");
    public static final Resource VoteInParliament = m.createProperty(uri, "VoteInParliament");
    public static final Resource PoliticalParty = m.createProperty(uri, "PoliticalParty");

    public static final Property billSponsor = m.createProperty(uri, "billSponsor");
    public static final Property legislativeProcessStage = m.createProperty(uri, "legislativeProcessStage");
    public static final Property outcome = m.createProperty(uri, "outcome");
    public static final Property decidedBy = m.createProperty(uri, "decidedBy");
    public static final Property enaction = m.createProperty(uri, "enaction");

    // VoteInParliament properties
    public static final Property supporterCount = m.createProperty(uri, "supporterCount");
    public static final Property opponentCount = m.createProperty(uri, "opponentCount");
    public static final Property abstainedCount = m.createProperty(uri, "abstainedCount");
    public static final Property quorum = m.createProperty(uri, "quorum");
    public static final Property decision = m.createProperty(uri, "decision");

    public static final Property hasAbstainee = m.createProperty(uri, "hasAbstainee");
    public static final Property hasAbsentee = m.createProperty(uri, "hasAbsentee");
    public static final Property hasOpponent = m.createProperty(uri, "hasOpponent");
    public static final Property hasSupporter = m.createProperty(uri, "hasSupporter");


}
