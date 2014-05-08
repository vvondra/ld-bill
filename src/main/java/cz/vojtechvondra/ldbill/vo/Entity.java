package cz.vojtechvondra.ldbill.vo;


public interface Entity {

    public static final String RESOURCE_URI_PREFIX = "http://linked.opendata.cz/resource/";

    /**
     * @return Fully qualified URI of this entity in RDF
     */
    public String getRdfUri();
}
