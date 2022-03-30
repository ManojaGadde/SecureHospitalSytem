package com.bezkoder.spring.security.postgresql.payload.response;

import java.sql.Date;
import java.sql.Time;

public class PatientAppointmentViewResponse {
    private int doctorID;
    private Time time;
    private Date date;
    private int approver;

    public PatientAppointmentViewResponse(int doctorID, Time time, Date date, int approver) {
        this.doctorID = doctorID;
        this.time = time;
        this.date = date;
        this.approver = approver;
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
}