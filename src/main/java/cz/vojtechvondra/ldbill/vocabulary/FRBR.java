package cz.vojtechvondra.ldbill.vocabulary;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;

/**
 * Used properties from the FRBR vocabulary
 * May not be complete, added because Jena does not contain the FRBR vocabulary
 */
public final class FRBR {

    protected static final String uri = "http://purl.org/vocab/frbr/core#";

    /**
     * @return the URI for this schema
     */
    public static String getURI() {
        return uri;
    }

    private static Model m = ModelFactory.createDefaultModel();

    public static final Resource Work = m.createProperty(uri, "Work");
    public static final Resource Expression = m.createProperty(uri, "Expression");
    public static final Property realizationOf = m.createProperty(uri, "realizationOf");
    public static final Property revisionOf = m.createProperty(uri, "revisionOf");

}
