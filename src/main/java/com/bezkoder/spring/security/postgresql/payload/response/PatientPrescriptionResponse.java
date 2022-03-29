package com.bezkoder.spring.security.postgresql.payload.response;

import java.sql.Date;

public class PatientPrescriptionResponse {
    private int doctorID;
    private Date gender;
    private int prescriptionID;
    private String prescription;
    
    public PatientPrescriptionResponse(int doctorID, Date gender, int prescriptionID, String prescription) {
        this.doctorID = doctorID;
        this.gender = gender;
        this.prescriptionID = prescriptionID;
        this.prescription = prescription;
    }
    public int getDoctorID() {
        return doctorID;
    }
    public void setDoctorID(int doctorID) {
        this.doctorID = doctorID;
    }
    public Date getGender() {
        return gender;
    }
    public void setGender(Date gender) {
        this.gender = gender;
    }
    public int getPrescriptionID() {
        return prescriptionID;
    }
    public void setPrescriptionID(int prescriptionID) {
        this.prescriptionID = prescriptionID;
    }
    public String getPrescription() {
        return prescription;
    }
    public void setPrescription(String prescription) {
        this.prescription = prescription;
    }
}
