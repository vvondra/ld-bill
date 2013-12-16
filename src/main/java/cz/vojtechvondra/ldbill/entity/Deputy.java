package cz.vojtechvondra.ldbill.entity;

public class Deputy extends Person {

    private String personId;

    private String website;

    private String address;

    private String city;

    private String[] emails;

    /**
     * Creates an instance from a PSP .unl export file line
     * @param parts from the poslanci.unl file
     */
    public Deputy(String[] parts) {
        if (parts.length < 10 || parts[3].equals("0")) {
            throw new IllegalArgumentException("Incorrect deputy import format");
        }

        personId = parts[1];
        website = parts[5];
        address = parts[6];
        city = parts[7];
        emails = parts[9].split(",");
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

    public String getPersonId() {
        return personId;
    }

}
