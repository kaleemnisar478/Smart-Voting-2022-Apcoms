package com.project.smartvotingsystem.helper_classes;

public class Voter {
    private String name;
    private String email;
    private String VoterID;


    public Voter() {
    }

    public Voter(String name, String email, String voterID) {
        this.name = name;
        this.email = email;
        VoterID = voterID;
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

    public String getVoterID() {
        return VoterID;
    }

    public void setVoterID(String voterID) {
        VoterID = voterID;
    }
}
