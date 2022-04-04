package com.bezkoder.spring.security.postgresql.payload.response;

import java.sql.Date;
import java.sql.Time;

public class FetchAllDoctorAppointmentsResponse {
    private int doctorID;
    private Date date;
    private String name;
    public FetchAllDoctorAppointmentsResponse(int doctorID, Date date, String name, Time time) {
        this.doctorID = doctorID;
        this.date = date;
        this.name = name;
        this.time = time;
    }
    public int getDoctorID() {
        return doctorID;
    }
    public void setDoctorID(int doctorID) {
        this.doctorID = doctorID;
    }
    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Time getTime() {
        return time;
    }
    public void setTime(Time time) {
        this.time = time;
    }
    private Time time;
}
