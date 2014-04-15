package cz.vojtechvondra.ldbill.vo;

import cz.vojtechvondra.ldbill.exceptions.UnsupportedRowFormatException;

public class Parliament extends Organ {

    public Parliament(String[] line) {
        super(line);

        if (!line[2].equals(ORGAN_PARLIAMENT.toString())) {
            throw new UnsupportedRowFormatException("Supplied data is not a parliament.");
        }
    }

    @Override
    public String getRdfUri() {
        return "http://linked.opendata.cz/resource/psp.cz/group/" + getOrganId();
    }
}
