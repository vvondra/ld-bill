package cz.vojtechvondra.ldbill.vo;


import com.hp.hpl.jena.rdf.model.RDFNode;
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

    private RDFNode billSponsor;

    static Logger logger = Logger.getLogger(Bill.class);

    public Bill(String number, String introductionDate, String title, String description, String billSponsor) throws IllegalArgumentException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        try {
            this.introductionDate = sdf.parse(introductionDate);
        } catch (ParseException e) {
            logger.warn(String.format("Could not parse date of introduction: %s for bill no: %s: %s", introductionDate, number, title));
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

    public RDFNode getBillSponsor() {
        return billSponsor;
    }

    /**
     * Maps possible bill sponsors
     */
    public static RDFNode getSponsorProperty(String sponsorId) {
        switch (sponsorId) {
            case "0": // @todo add description
            case "1":
                return BillSponsors.Government;
            case "2":
                return BillSponsors.Representative;
            case "3":
                return BillSponsors.RepresentativeGroup;
            case "4":
                return BillSponsors.Senate;
            case "5":
            case "6":
            case "7":
            case "8":
            case "9":
            case "10":
            case "11":
            case "12":
            case "13":
            case "14":
            case "15":
            case "16":
            case "17":
            case "18":
            case "19":
                return BillSponsors.RegionalAssembly;
        }

        throw new IllegalArgumentException("Unknown bill sponsor provided: " + sponsorId);
    }

    @Override
    public String toString() {
        return getRdfUri();
    }
}
