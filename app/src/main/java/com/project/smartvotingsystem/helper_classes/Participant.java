package com.project.smartvotingsystem.helper_classes;

public class Participant {

    private String participantId;
    private String name;
    private String email;
    private String party;
    private String pic;
    private String symbol;
    private int votes;
    private Voter[] myVoters;


    public Participant() {
    }

    public Participant(String participantId, String name, String email, String party, String pic, String symbol) {
        this.participantId = participantId;
        this.name = name;
        this.email = email;
        this.party = party;
        this.pic = pic;
        this.symbol = symbol;
        this.votes=0;
    }

    public Participant(String participantId, String name, String email, String party, String pic, String symbol, int votes) {
        this.participantId = participantId;
        this.name = name;
        this.email = email;
        this.party = party;
        this.pic = pic;
        this.symbol = symbol;
        this.votes = votes;
    }



    public String getParticipantId() {
        return participantId;
    }

    public void setParticipantId(String participantId) {
        this.participantId = participantId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getParty() {
        return party;
    }

    public void setParty(String party) {
        this.party = party;
    }

    public String getPic() {
        return pic;
    }

    public int getVotes() {
        return votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }


    public Voter[] getMyVoters() {
        return myVoters;
    }

    public void setMyVoters(Voter[] myVoters) {
        this.myVoters = myVoters;
    }
}
