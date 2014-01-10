package cz.vojtechvondra.ldbill.vo;


public class Party extends Organ {
    public Party(String[] parts) {
        super(parts);
        if (!parts[2].equals(ORGAN_PARTY.toString())) {
            throw new IllegalArgumentException("Supplied data is not a party.");
        }
    }
}
