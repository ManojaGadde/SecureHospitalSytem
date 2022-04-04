package com.bezkoder.spring.security.postgresql.payload.response;

public class FetchUserDetailsResponse {
    private int userID;
    private String name;
    private String email;
    private String accountType;
    public FetchUserDetailsResponse(int userID, String name, String email, String accountType) {
        this.userID = userID;
        this.name = name;
        this.email = email;
        this.accountType = accountType;
    }
    public int getUserID() {
        return userID;
    }
    public void setUserID(int userID) {
        this.userID = userID;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getAccountType() {
        return accountType;
    }
    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }
}
