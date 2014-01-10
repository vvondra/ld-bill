package cz.vojtechvondra.ldbill.entity;

public class Vote implements Entity {

    private String voteId;

    public Vote(int voteId) {
        this.voteId = Integer.toString(voteId);
    }

    public Vote(String voteId) {
        this.voteId = voteId;
    }

    @Override
    public String getRdfUri() {
        return "http://linked.opendata.cz/resource/psp.cz/poll/" + voteId;
    }
}
