package cz.vojtechvondra.ldbill.vocabulary;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;

public final class Decisions {

    protected static final String uri = "http://linked.opendata.cz/resource/legislation/vote-decisions#";

    /**
     * @return the URI for this schema
     */
    public static String getURI() {
        return uri;
    }

    private static final Model m = ModelFactory.createDefaultModel();

    public static final Resource Decision = m.createProperty(uri, "Decision");
    public static final Property Accepts = m.createProperty(uri, "Accepts");
    public static final Property Refuses = m.createProperty(uri, "Refuses");
    public static final Property Spoilt = m.createProperty(uri, "Spoilt");
    public static final Property PassesToCommittee = m.createProperty(uri, "PassesToCommittee");

}
