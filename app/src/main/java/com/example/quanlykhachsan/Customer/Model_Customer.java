package com.example.quanlykhachsan.Customer;

public class Model_Customer {
    private String id;
    private String name;
    private String identityCard;
    private String sex;
    private String dateOfBirth;
    private String phone;
    private String email;
    private String address;
    private int credibilityScore;
    private int accumulatedPoints;

    public Model_Customer(String id, String name, String identityCard, String sex, String dateOfBirth, String phone, String email, String address, int credibilityScore, int accumulatedPoints) {
        this.id = id;
        this.name = name;
        this.identityCard = identityCard;
        this.sex = sex;
        this.dateOfBirth = dateOfBirth;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.credibilityScore = credibilityScore;
        this.accumulatedPoints = accumulatedPoints;
    }
    public Model_Customer(String name, String identityCard, String sex, String dateOfBirth, String phone, String address) {
        this.name = name;
        this.identityCard = identityCard;
        this.sex = sex;
        this.dateOfBirth = dateOfBirth;
        this.phone = phone;
        this.address = address;
    }
    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdentityCard() {
        return identityCard;
    }

    public void setIdentityCard(String identityCard) {
        this.identityCard = identityCard;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getCredibilityScore() {
        return credibilityScore;
    }

    public void setCredibilityScore(int credibilityScore) {
        this.credibilityScore = credibilityScore;
    }

    public int getAccumulatedPoints() {
        return accumulatedPoints;
    }

    public void setAccumulatedPoints(int accumulatedPoints) {
        this.accumulatedPoints = accumulatedPoints;
    }
}
