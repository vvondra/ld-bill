package cz.vojtechvondra.ldbill.entity;

public class Organ implements Entity {

    private String organId;

    private String shortCode;

    private String fullTitle;

    /**
     * Creates an instance from a PSP .unl export file line
     * @param parts row from the organy export file
     */
    public Organ(String[] parts) {
        if (parts.length < 10 || !parts[2].equals("6")) {
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
        return "http://linked.opendata.cz/resource/psp.cz/group/" + getOrganId();
    }
}
