package com.bezkoder.spring.security.postgresql.payload.response;

public class DoctorNamesResponse {
    private int doctorID;
    private String name;
    public int getDoctorID() {
        return doctorID;
    }
    public DoctorNamesResponse(int doctorID, String name) {
        this.doctorID = doctorID;
        this.name = name;
    }
    public void setDoctorID(int doctorID) {
        this.doctorID = doctorID;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
