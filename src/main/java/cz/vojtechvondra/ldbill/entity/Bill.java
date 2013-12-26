package cz.vojtechvondra.ldbill.entity;


import java.util.Date;

public class Bill implements Entity {

    private String year;

    private String number;

    private Date introductionDate;

    private String title;

    private String description;

    public Bill(String[] parts) {
        // TODO
    }

    @Override
    public String getRdfUri() {
        return "http://linked.opendata.cz/resource/legislation/cz/bill/" + getIdent();
    }

    public String getNumber() {
        return number;
    }

    public Date getIntroductionDate() {
        return introductionDate;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getYear() {
        return year;
    }

    public String getIdent() {
        return getYear() + "/" + getNumber();
    }
}
