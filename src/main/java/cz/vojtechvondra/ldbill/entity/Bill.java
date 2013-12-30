package cz.vojtechvondra.ldbill.entity;


import com.hp.hpl.jena.rdf.model.Property;
import cz.vojtechvondra.ldbill.vocabulary.BillSponsors;
import org.apache.log4j.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Bill implements Entity {

    private String year;

    private String number;

    private Date introductionDate;

    private String title;

    private String description;

    private Property billSponsor;

    static Logger logger = Logger.getLogger(Bill.class);


    public Bill(String number, String introductionDate, String title, String description, String billSponsor) throws IllegalArgumentException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        try {
            this.introductionDate = sdf.parse(introductionDate);
        } catch (ParseException e) {
            logger.warn("Could not parse date of introduction: " + introductionDate);
            throw new IllegalArgumentException(e);
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(this.introductionDate);
        year = (new StringBuilder().append(cal.get(Calendar.YEAR))).toString();

        this.number = number;
        this.billSponsor = getSponsorProperty(billSponsor);
        this.title = title;
        this.description = description;
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

    public Property getBillSponsor() {
        return billSponsor;
    }

    /**
     * Maps possible bill sponsors
     */
    public static Property getSponsorProperty(String sponsorId) {
        switch (sponsorId) {
            case "1":
                return BillSponsors.Government;
            case "2":
                return BillSponsors.Representative;
            case "3":
                return BillSponsors.RepresentativeGroup;
            case "4":
                return BillSponsors.Senate;
            case "5":
                return BillSponsors.RegionalAssembly;
        }

        throw new IllegalArgumentException("Unknown bill sponsor provided: " + sponsorId);
    }
}
