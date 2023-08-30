package com.cse489.tutorbridge.modal;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class MentorProfileClass implements Serializable {
    String mentor, name, email, phone, education, expert, desc, status, location, year;
    double price, wallet;

    public MentorProfileClass(){

    }

    public MentorProfileClass(String mentor, String name, String email, String phone, String education, String expert, String desc, String status, String location, String year, double price, double wallet) {
        this.mentor = mentor;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.education = education;
        this.expert = expert;
        this.desc = desc;
        this.status = status;
        this.location = location;
        this.year = year;
        this.price = price;
        this.wallet = wallet;
    }

    public String getMentorid() {
        return mentor;
    }

    public void setMentorid(String mentorid) {
        this.mentor = mentorid;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getExpert() {
        return expert;
    }

    public void setExpert(String expert) {
        this.expert = expert;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getPrice() {
        return price;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getMentor() {
        return mentor;
    }

    public void setMentor(String mentor) {
        this.mentor = mentor;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public double getWallet() {
        return wallet;
    }

    public void setWallet(double wallet) {
        this.wallet = wallet;
    }

    @Override
    public String toString() {
        return "MentorProfileClass{" +
                "mentor='" + mentor + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", education='" + education + '\'' +
                ", expert='" + expert + '\'' +
                ", desc='" + desc + '\'' +
                ", status='" + status + '\'' +
                ", location='" + location + '\'' +
                ", year='" + year + '\'' +
                ", price=" + price +
                ", wallet=" + wallet +
                '}';
    }
}
