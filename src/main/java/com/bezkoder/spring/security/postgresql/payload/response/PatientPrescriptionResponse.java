package com.bezkoder.spring.security.postgresql.payload.response;

import java.sql.Date;
import java.util.List;

public class PatientPrescriptionResponse {
    
    private int doctorID;
    private int patientID;

    public PatientPrescriptionResponse(int patientID, int doctorID,  Date date, int prescriptionID,
            String prescription, int age, String gender, String address, String phoneNumber, String creditCard) {
        this.doctorID = doctorID;
        this.patientID = patientID;
        this.date = date;
        this.prescriptionID = prescriptionID;
        this.prescription = prescription;
        this.age = age;
        this.gender = gender;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.creditCard = creditCard;
    }
    private Date date;
    private int prescriptionID;
    public int getPatientID() {
        return patientID;
    }


    public void setPatientID(int patientID) {
        this.patientID = patientID;
    }
    private String prescription;
    private int age;
    private String gender;
    private String address;
    private String phoneNumber;
    private String creditCard;
    
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
    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }
    public String getGender() {
        return gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public String getCreditCard() {
        return creditCard;
    }
    public void setCreditCard(String creditCard) {
        this.creditCard = creditCard;
    }
    
}
