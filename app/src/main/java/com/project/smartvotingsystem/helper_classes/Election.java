package com.project.smartvotingsystem.helper_classes;

public class Election {

    private String detailId;
    private String title;
    private String startDate;
    private String endDate;
    private String startTime;
    private String endTime;
    private String details;

    private Participant[] participants;
    private Voter[] voter;


    public Election() {
    }

    public Election(String detailId, String title, String startDate, String endDate, String startTime, String endTime, String details) {
        this.detailId = detailId;
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.details = details;
    }

    public Election(String detailId, String title, String startDate, String endDate, String startTime, String endTime, String details, Participant[] participants, Voter[] voter) {
        this.detailId = detailId;
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.details = details;
        this.participants = participants;
        this.voter = voter;
    }

    public Participant[] getParticipants() {
        return participants;
    }

    public void setParticipants(Participant[] participants) {
        this.participants = participants;
    }

    public Voter[] getVoter() {
        return voter;
    }

    public void setVoter(Voter[] voter) {
        this.voter = voter;
    }

    public String getDetailId() {
        return detailId;
    }

    public void setDetailId(String detailId) {
        this.detailId = detailId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
