package cz.vojtechvondra.ldbill.vo;


import cz.vojtechvondra.ldbill.exceptions.UnsupportedRowFormatException;

public class Party extends Organ {
    public Party(String[] parts) {
        super(parts);
        if (!parts[2].equals(ORGAN_PARTY.toString())) {
            throw new UnsupportedRowFormatException("Supplied data is not a party.");
        }
    }
}
