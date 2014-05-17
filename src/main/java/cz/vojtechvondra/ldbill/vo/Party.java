package cz.vojtechvondra.ldbill.vo;


import cz.vojtechvondra.ldbill.exceptions.UnsupportedRowFormatException;

/**
 * Represents a political party
 */
public class Party extends Organ {

    /**
     * @param parts Columns from the psp.cz data export
     */
    public Party(String[] parts) {
        super(parts);
        if (!parts[2].equals(ORGAN_PARTY.toString())) {
            throw new UnsupportedRowFormatException("Supplied data is not a party.");
        }
    }

    @Override
    public String getRdfUri() {
        return getRdfPrefix() + getShortCode();
    }
}
