package com.bezkoder.spring.security.postgresql.payload.response;

import java.sql.Date;

public class DoctorPatientRecordsResponse {
    private int recordId;
    private int inputter;
    private String record;
    private Date date;

    public DoctorPatientRecordsResponse(int recordId, int inputter, String record, Date date) {
        this.recordId = recordId;
        this.inputter = inputter;
        this.record = record;
        this.date = date;
    }

    public int getRecordId() {
        return recordId;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    public int getInputter() {
        return inputter;
    }

    public void setInputter(int inputter) {
        this.inputter = inputter;
    }

    public String getRecord() {
        return record;
    }

    public void setRecord(String record) {
        this.record = record;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
