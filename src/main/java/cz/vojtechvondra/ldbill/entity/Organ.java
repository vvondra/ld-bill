package cz.vojtechvondra.ldbill.entity;

public class Organ implements Entity {

    public static final Integer ORGAN_PARTY = 6;

    public static final Integer ORGAN_PARLIAMENT = 11;

    protected String organId;

    protected String shortCode;

    protected String fullTitle;

    public Organ(String organId) {
        this.organId = organId;
    }

    /**
     * Creates an instance from a PSP .unl export file line
     * @param parts row from the organy export file
     */
    public Organ(String[] parts) {
        if (parts.length < 10) {
            throw new IllegalArgumentException("Incorrect organ import format");
        }

        organId = parts[0];
        shortCode = parts[3];
        fullTitle = parts[4];

    }

    public String getOrganId() {
        return organId;
    }

    public String getShortCode() {
        return shortCode;
    }

    public String getFullTitle() {
        return fullTitle;
    }

    @Override
    public String getRdfUri() {
        return getRdfPrefix() + getOrganId();
    }

    static public String getRdfPrefix() {
        return "http://linked.opendata.cz/resource/psp.cz/group/";
    }
}
