package com.bezkoder.spring.security.postgresql.payload.response;

import java.sql.Date;
import java.util.List;

public class PatientDiagnosisResponse {
    private int patientID;
    private int doctorID;
    private Date date;
    private String diagnosis;
    public int getPatientID() {
        return patientID;
    }

    public void setPatientID(int patientID) {
        this.patientID = patientID;
    }
    public PatientDiagnosisResponse(int patientID, int doctorID, Date date, String diagnosis, int age, String gender,
            String address, String phoneNumber, String creditCard) {
        this.patientID = patientID;
        this.doctorID = doctorID;
        this.date = date;
        this.diagnosis = diagnosis;
        this.age = age;
        this.gender = gender;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.creditCard = creditCard;
    }
    private int age;
    private String gender;
    private String address;
    private String phoneNumber;
    private String creditCard;

    

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
    public String getDiagnosis() {
        return diagnosis;
    }
    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }
    
}
