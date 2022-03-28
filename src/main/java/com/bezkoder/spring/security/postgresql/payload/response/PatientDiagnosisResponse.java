package com.bezkoder.spring.security.postgresql.payload.response;

import java.sql.Date;
import java.util.List;

public class PatientDiagnosisResponse {
    private int doctorID;
    private Date date;
    private List<String> diagnosis;
    
    public PatientDiagnosisResponse(int doctorID, Date date, List<String> diagnosis) {
        this.doctorID = doctorID;
        this.date = date;
        this.diagnosis = diagnosis;
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
    public List<String> getDiagnosis() {
        return diagnosis;
    }
    public void setDiagnosis(List<String> diagnosis) {
        this.diagnosis = diagnosis;
    }
    
}
