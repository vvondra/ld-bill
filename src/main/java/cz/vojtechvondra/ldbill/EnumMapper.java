package cz.vojtechvondra.ldbill;

/**
 * Class EnumMapper
 * <p/>
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


} 