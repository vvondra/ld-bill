package cz.vojtechvondra.ldbill.vo;

import cz.vojtechvondra.ldbill.exceptions.UnsupportedRowFormatException;

/**
 * Represents a Parliamentary session
 * All sessions have a lifetime between elections
 */
public class Parliament extends Organ {

    public Parliament(String[] line) {
        super(line);

        if (!line[2].equals(ORGAN_PARLIAMENT.toString())) {
            throw new UnsupportedRowFormatException("Supplied data is not a parliament.");
        }
    }

    @Override
    public String getRdfUri() {
        return RESOURCE_URI_PREFIX + "psp.cz/group/" + getOrganId();
    }
}
