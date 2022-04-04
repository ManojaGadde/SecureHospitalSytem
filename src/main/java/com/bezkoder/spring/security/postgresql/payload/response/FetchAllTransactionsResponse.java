package com.bezkoder.spring.security.postgresql.payload.response;

import java.sql.Date;

public class FetchAllTransactionsResponse {
    private int transactionAmount;
    private int transactionID;
    private Date date;
    private String status;
    public FetchAllTransactionsResponse(int transactionAmount, int transactionID, Date date, String status, int payer) {
        this.transactionAmount = transactionAmount;
        this.transactionID = transactionID;
        this.date = date;
        this.status = status;
        this.payer = payer;
    }
    private int payer;
    public int getTransactionAmount() {
        return transactionAmount;
    }
    public void setTransactionAmount(int transactionAmount) {
        this.transactionAmount = transactionAmount;
    }
    public int getTransactionID() {
        return transactionID;
    }
    public void setTransactionID(int transactionID) {
        this.transactionID = transactionID;
    }
    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public int getPayer() {
        return payer;
    }
    public void setPayer(int payer) {
        this.payer = payer;
    }
    
}
