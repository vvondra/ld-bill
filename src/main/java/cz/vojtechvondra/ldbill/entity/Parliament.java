package cz.vojtechvondra.ldbill.entity;

public class Parliament extends Organ {

    public Parliament(String[] line) {
        super(line);
    }

    @Override
    public String getRdfUri() {
        return "http://linked.opendata.cz/resource/psp.cz/group/" + getOrganId();
    }
}
