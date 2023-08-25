package com.cse489.tutorbridge.modal;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class MentorProfileClass implements Serializable {
    String mentor, name, email, phone, education, expert, desc, status, location;
    double price;

    public MentorProfileClass(){

    }

    public MentorProfileClass(String mentorid, String name, String email, String phone, String education, String expert, String desc, String status, double price, String location) {
        this.mentor = mentorid;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.education = education;
        this.expert = expert;
        this.desc = desc;
        this.status = status;
        this.price = price;
        this.location = location;
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
                ", price='" + price + '\'' +
                ", location='" + location + '\'' +
                '}';
    }
}
