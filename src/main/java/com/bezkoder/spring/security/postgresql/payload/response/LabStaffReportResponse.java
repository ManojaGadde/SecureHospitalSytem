package com.bezkoder.spring.security.postgresql.payload.response;

import java.sql.Date;

public class LabStaffReportResponse {
    private String testName;
    private String record;
    private int inputter;
    public int getPatientID() {
        return patientID;
    }
    public void setPatientID(int patientID) {
        this.patientID = patientID;
    }
    private int patientID;
    private String status;
    private Date dateRecommended;
    private int recommender;
    private Date dateFilled;
    

    public LabStaffReportResponse(int patientID, String testName, String record, int inputter, String status, Date dateRecommended,
            int recommender, Date dateFilled) {
        this.patientID = patientID;
        this.testName = testName;
        this.record = record;
        this.inputter = inputter;
        this.status = status;
        this.dateRecommended = dateRecommended;
        this.recommender = recommender;
        this.dateFilled = dateFilled;
    }
    public LabStaffReportResponse(String testName2, int inputter2, String status2, Date dateRecommended2,
            int recommender2, Date dateFilled2) {
    }
    public String getTestName() {
        return testName;
    }
    public void setTestName(String testName) {
        this.testName = testName;
    }
    public String getRecord() {
        return record;
    }
    public void setRecord(String record) {
        this.record = record;
    }
    public int getInputter() {
        return inputter;
    }
    public void setInputter(int inputter) {
        this.inputter = inputter;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public Date getDateRecommended() {
        return dateRecommended;
    }
    public void setDateRecommended(Date dateRecommended) {
        this.dateRecommended = dateRecommended;
    }
    public int getRecommender() {
        return recommender;
    }
    public void setRecommender(int recommender) {
        this.recommender = recommender;
    }
    public Date getDateFilled() {
        return dateFilled;
    }
    public void setDateFilled(Date dateFilled) {
        this.dateFilled = dateFilled;
    }

    
}
