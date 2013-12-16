package cz.vojtechvondra.ldbill;

/**
 * Class EnumMapper
 *
 * Maps PSP export enumerated values to RDF predicates
 */
class EnumMapper {

    /**
     * Maps possible types of casting a ballot
     */
    public static String vote(String vote) {
        switch (vote) {
            case "@":
            case "M":
                return "hasAbsentee";
            case "F":
            case "C":
                return "hasAbstainee";
            case "A":
                return "hasOpponent";
            case "B":
                return "hasSupporter";
        }

        throw new IllegalArgumentException("Unknown vote type: " + vote);
    }

    /**
     * Maps legislative process stages
     */
    public static String stage(String stage) {
        switch (stage) {
            case "0":
                return "Introduced";
            case "1":
            case "17":
            case "18":
                return "FirstReading";
            case "2":
            case "3":
            case "4":
                return "SecondReading";
            case "5":
                return "ThirdReading";
            case "6":
                return "Passed";
            case "7":
                return "CommitteeConsideration";
            case "8":
                return "President";
            case "12":
                return "Senate";
            case "11":
                return "Passed";
            case "16":
            case "15":
                return "GovernmentPosition";
            case "14":
                return "SenateVetoOverride";
            case "21":
                return "Amendments";
        }

        throw new IllegalArgumentException("Unknown stage provided: " + stage);
    }

    /**
     * Maps possible bill sponsors
     */
    public static String sponsor(String sponsorId) {
        switch (sponsorId) {
            case "1":
                return "Government";
            case "2":
                return "Representative";
            case "3":
                return "RepresentativeGroup";
            case "4":
                return "Senate";
            case "5":
                return "RegionalAssembly";
        }

        throw new IllegalArgumentException("Unknown bill sponsor provided: " + sponsorId);
    }
} 