package com.bezkoder.spring.security.postgresql.payload.response;

import java.sql.Date;
import java.sql.Time;

public class FetchAllDoctorsResponse {
    private int patientID;
    private int doctorID;
    private Time time;
    public FetchAllDoctorsResponse(int patientID, int doctorID, Time time, Date date, int approver, String status,
            int amount) {
        this.patientID = patientID;
        this.doctorID = doctorID;
        this.time = time;
        this.date = date;
        this.approver = approver;
        this.status = status;
        this.amount = amount;
    }
    private Date date;
    private int approver;
    public int getPatientID() {
        return patientID;
    }
    public void setPatientID(int patientID) {
        this.patientID = patientID;
    }
    public int getDoctorID() {
        return doctorID;
    }
    public void setDoctorID(int doctorID) {
        this.doctorID = doctorID;
    }
    public Time getTime() {
        return time;
    }
    public void setTime(Time time) {
        this.time = time;
    }
    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }
    public int getApprover() {
        return approver;
    }
    public void setApprover(int approver) {
        this.approver = approver;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public int getAmount() {
        return amount;
    }
    public void setAmount(int amount) {
        this.amount = amount;
    }
    private String status;
    private int amount;
}
