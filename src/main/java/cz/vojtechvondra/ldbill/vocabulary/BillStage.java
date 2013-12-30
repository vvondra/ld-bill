package cz.vojtechvondra.ldbill.vocabulary;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;

public final class BillStage {

    protected static final String uri = "http://linked.opendata.cz/resource/legislation/bill-legislative-stages#";

    /**
     * @return the URI for this schema
     */
    public static String getURI() {
        return uri;
    }

    private static final Model m = ModelFactory.createDefaultModel();

    public static final Resource BillStage = m.createProperty(uri, "BillStage");
    public static final Property Introduced = m.createProperty(uri, "Introduced");
    public static final Property GovernmentPosition = m.createProperty(uri, "GovernmentPosition");
    public static final Property FirstReading = m.createProperty(uri, "FirstReading");
    public static final Property CommitteeConsideration = m.createProperty(uri, "CommitteeConsideration");
    public static final Property SecondReading = m.createProperty(uri, "SecondReading");
    public static final Property ThirdReading = m.createProperty(uri, "ThirdReading");
    public static final Property Amendments = m.createProperty(uri, "Amendments");
    public static final Property Senate = m.createProperty(uri, "Senate");
    public static final Property VetoOverride = m.createProperty(uri, "VetoOverride");
    public static final Property SenateVetoOverride = m.createProperty(uri, "SenateVetoOverride");
    public static final Property PresidentialVetoOverride = m.createProperty(uri, "PresidentialVetoOverride");
    public static final Property President = m.createProperty(uri, "President");
    public static final Property Passed = m.createProperty(uri, "Passed");

}
