package cz.vojtechvondra.ldbill.vo;


import com.hp.hpl.jena.rdf.model.Property;
import cz.vojtechvondra.ldbill.vocabulary.BillStage;
import org.apache.log4j.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BillRevision implements Entity {

    private final Bill bill;
    private final int revisionNumber;
    private final String description;
    private final Date date;
    private final Property stage;
    private final String outcome;
    static Logger logger = Logger.getLogger(BillRevision.class);

    public BillRevision(Bill bill, int revisionNumber, String description, String date, String stage, String outcome) {
        Date revDate;
        this.bill = bill;
        this.revisionNumber = revisionNumber;
        this.description = description;
        this.stage = getLegislativeProcessStage(stage);
        this.outcome = outcome;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        try {
            revDate = sdf.parse(date);
        } catch (ParseException e) {
            logger.warn("Could not parse date of : " + date);
            revDate = bill.getIntroductionDate();
        }
        this.date = revDate;
    }

    @Override
    public String getRdfUri() {
        return "http://linked.opendata.cz/resource/legislation/cz/bill/" + bill.getIdent() + "/" + revisionNumber;
    }

    public Bill getBill() {
        return bill;
    }

    public String getTitle() {
        return bill.getTitle() + " - " + description;
    }

    public Date getDate() {
        return date;
    }

    public Property getStage() {
        return stage;
    }

    public String getOutcome() {
        return outcome;
    }

    private static Property getLegislativeProcessStage(String stageId) {
        switch (stageId) {
            case "0":
                return BillStage.Introduced;
            case "1":
            case "17":
            case "18":
                return BillStage.FirstReading;
            case "2":
            case "3":
            case "4":
                return BillStage.SecondReading;
            case "5":
                return BillStage.ThirdReading;
            case "6":
                return BillStage.Passed;
            case "7":
                return BillStage.CommitteeConsideration;
            case "8":
            case "13":
                return BillStage.President;
            case "12":
                return BillStage.Senate;
            case "11":
                return BillStage.Passed;
            case "16":
            case "15":
                return BillStage.GovernmentPosition;
            case "14":
                return BillStage.SenateVetoOverride;
            case "21":
                return BillStage.Amendments;
        }

        throw new IllegalArgumentException("Unknown stage provided: " + stageId);
    }

}
