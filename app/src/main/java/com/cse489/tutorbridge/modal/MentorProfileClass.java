package com.cse489.tutorbridge.modal;

import java.io.Serializable;

public class MentorProfileClass implements Serializable {
    String uuid, mentorid, name, email, phone, education, expert, desc, status, location, year, date;
    double price, wallet;

    public MentorProfileClass(){

    }

    public MentorProfileClass(String uuid, String mentor, String name, String email, String phone, String education, String expert, String desc, String status, String location, String year, String date, double price, double wallet) {
        this.uuid = uuid;
        this.mentorid = mentor;
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
        this.date = date;
    }

    public MentorProfileClass(String name, String phone, String education, String expert, String desc, String status, String location, String year, String date, double price, double wallet) {
        this.name = name;
        this.phone = phone;
        this.education = education;
        this.expert = expert;
        this.desc = desc;
        this.status = status;
        this.location = location;
        this.year = year;
        this.price = price;
        this.wallet = wallet;
        this.date = date;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getMentor() {
        return mentorid;
    }

    public void setMentor(String mentorid) {
        this.mentorid = mentorid;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "MentorProfileClass{" +
                "uuid='" + uuid + '\'' +
                ", mentorid='" + mentorid + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", education='" + education + '\'' +
                ", expert='" + expert + '\'' +
                ", desc='" + desc + '\'' +
                ", status='" + status + '\'' +
                ", location='" + location + '\'' +
                ", year='" + year + '\'' +
                ", date='" + date + '\'' +
                ", price=" + price +
                ", wallet=" + wallet +
                '}';
    }
}
