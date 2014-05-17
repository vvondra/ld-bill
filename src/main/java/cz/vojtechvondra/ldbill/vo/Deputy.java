package cz.vojtechvondra.ldbill.vo;

import java.util.ArrayList;

/**
 * Represents a deputy in the lower chamber of the Parliament
 */
public class Deputy extends Person {

    private String website;

    private String address;

    private String city;

    private String[] emails;

    private String partyId;

    /**
     * Creates an instance from a PSP .unl export file line
     * @param parts from the poslanci.unl file
     */
    public Deputy(String[] parts) {
        if (parts.length < 10 || parts[3].equals("0")) {
            throw new IllegalArgumentException("Incorrect deputy import format");
        }

        personId = parts[1];
        partyId = parts[3];
        website = parts[5];
        address = parts[6];
        city = parts[7];
        setEmail(parts[9]);
    }

    public void setEmail(String value) {
        String[] emails = value.split(",");
        ArrayList<String> toSet = new ArrayList<>();
        for (String email : emails) {
            email = email.trim().replace("\\", "");
            if (email.length() > 0) {
                toSet.add(email);
            }
        }
        this.emails = toSet.toArray(new String[toSet.size()]);
    }

    public String getWebsite() {
        return website;
    }

    public String[] getEmails() {
        return emails;
    }

    public String getCity() {
        return city;
    }

    public String getAddress() {
        return address;
    }

    public String getPartyId() {
        return partyId;
    }
}
