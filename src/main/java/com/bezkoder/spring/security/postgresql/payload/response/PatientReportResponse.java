package com.bezkoder.spring.security.postgresql.payload.response;

import java.sql.Date;

public class PatientReportResponse {
        private int patientID;
        private String testName;
        private String record;
        private int inputter;
        public int getPatientID() {
            return patientID;
        }
        public void setPatientID(int patientID) {
            this.patientID = patientID;
        }
        private String status;
        private Date dateRecommended;
        private int recommender;
        private Date dateFilled;
        private int age;
        private String gender;
        private String address;
        private String phoneNumber;
        private String creditCard;
        
        public PatientReportResponse(int patientID, String testName, String record, int inputter, String status,
                Date dateRecommended, int recommender, Date dateFilled, int age, String gender, String address,
                String phoneNumber, String creditCard) {
            this.patientID = patientID;
            this.testName = testName;
            this.record = record;
            this.inputter = inputter;
            this.status = status;
            this.dateRecommended = dateRecommended;
            this.recommender = recommender;
            this.dateFilled = dateFilled;
            this.age = age;
            this.gender = gender;
            this.address = address;
            this.phoneNumber = phoneNumber;
            this.creditCard = creditCard;
        }
        public PatientReportResponse(String testName, String record, int inputter, String status, Date dateRecommended,
                int recommender, Date dateFilled, int age, String gender, String address, String phoneNumber,
                String creditCard) {
            this.testName = testName;
            this.record = record;
            this.inputter = inputter;
            this.status = status;
            this.dateRecommended = dateRecommended;
            this.recommender = recommender;
            this.dateFilled = dateFilled;
            this.age = age;
            this.gender = gender;
            this.address = address;
            this.phoneNumber = phoneNumber;
            this.creditCard = creditCard;
        }
        public String getTestName() {
            return testName;
        }
        public void setTestName(String testName) {
            this.testName = testName;
        }
        public String getRecord() {
            return record;
        }
        public void setRecord(String record) {
            this.record = record;
        }
        public int getInputter() {
            return inputter;
        }
        public void setInputter(int inputter) {
            this.inputter = inputter;
        }
        public String getStatus() {
            return status;
        }
        public void setStatus(String status) {
            this.status = status;
        }
        public Date getDateRecommended() {
            return dateRecommended;
        }
        public void setDateRecommended(Date dateRecommended) {
            this.dateRecommended = dateRecommended;
        }
        public int getRecommender() {
            return recommender;
        }
        public void setRecommender(int recommender) {
            this.recommender = recommender;
        }
        public Date getDateFilled() {
            return dateFilled;
        }
        public void setDateFilled(Date dateFilled) {
            this.dateFilled = dateFilled;
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
