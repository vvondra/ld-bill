package cz.vojtechvondra.ldbill.vocabulary;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;

public final class LB {

    protected static final String uri = "http://linked.opendata.cz/resource/legislation/bill#";

    /**
     * @return the URI for this schema
     */
    public static String getURI() {
        return uri;
    }

    private static final Model m = ModelFactory.createDefaultModel();

    public static final Resource Bill = m.createProperty(uri, "Bill");

    public static final Property billSponsor = m.createProperty(uri, "billSponsor");
    public static final Property legislativeProcessStage = m.createProperty(uri, "legislativeProcessStage");
    public static final Property outcome = m.createProperty(uri, "outcome");
    public static final Property decidedBy = m.createProperty(uri, "decidedBy");
    public static final Property enaction = m.createProperty(uri, "enaction");

}
