package cz.vojtechvondra.ldbill.vocabulary;

import com.hp.hpl.jena.rdf.model.*;

public class LB {

    protected static final String uri = "http://linked.opendata.cz/resource/legislation/bill#";

    /**
     * @return the URI for this schema
     */
    public static String getURI() {
        return uri;
    }

    private static Model m = ModelFactory.createDefaultModel();

    public static final Property billSponsor = m.createProperty(uri, "billSponsor");

}
