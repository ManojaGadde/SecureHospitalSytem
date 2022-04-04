package com.bezkoder.spring.security.postgresql.payload.response;

import java.sql.Date;

public class FetchBillsResponse {
    private int patientID;
    private int transactionID;
    private int amount;
    private String service;
    private Date date;
    public FetchBillsResponse(int patientID, int transactionID, int amount, String service, Date date) {
        this.patientID = patientID;
        this.transactionID = transactionID;
        this.amount = amount;
        this.service = service;
        this.date = date;
    }
    public int getPatientID() {
        return patientID;
    }
    public void setPatientID(int patientID) {
        this.patientID = patientID;
    }
    public int getTransactionID() {
        return transactionID;
    }
    public void setTransactionID(int transactionID) {
        this.transactionID = transactionID;
    }
    public int getAmount() {
        return amount;
    }
    public void setAmount(int amount) {
        this.amount = amount;
    }
    public String getService() {
        return service;
    }
    public void setService(String service) {
        this.service = service;
    }
    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }
    
}
