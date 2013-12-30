package cz.vojtechvondra.ldbill.vocabulary;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;

public class BillSponsors {

    protected static final String uri = "http://linked.opendata.cz/resource/legislation/bill-sponsors#";

    /**
     * @return the URI for this schema
     */
    public static String getURI() {
        return uri;
    }

    private static Model m = ModelFactory.createDefaultModel();

    public static final Property Sponsor = m.createProperty(uri, "Sponsor");
    public static final Property Representative = m.createProperty(uri, "Representative");
    public static final Property RepresentativeGroup = m.createProperty(uri, "RepresentativeGroup");
    public static final Property Senate = m.createProperty(uri, "Senate");
    public static final Property Government = m.createProperty(uri, "Government");
    public static final Property RegionalAssembly = m.createProperty(uri, "RegionalAssembly");

}