package cz.vojtechvondra.ldbill.entity;

public class Parliament extends Organ {

    public Parliament(String[] line) {
        super(line);

        if (!line[2].equals(ORGAN_PARLIAMENT.toString())) {
            throw new IllegalArgumentException("Supplied data is not a parliament.");
        }
    }

    @Override
    public String getRdfUri() {
        return "http://linked.opendata.cz/resource/psp.cz/group/" + getOrganId();
    }
}
