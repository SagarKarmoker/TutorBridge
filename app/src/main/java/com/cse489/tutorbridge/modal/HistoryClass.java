package com.cse489.tutorbridge.modal;

import java.io.Serializable;

public class HistoryClass implements Serializable {

    String mentor, user, orderId, orderDate, orderStatus, paymentMethod, orderCategory, orderValue;

    public HistoryClass(){

    }

    public HistoryClass(String mentor, String user, String orderId, String orderDate, String orderStatus, String paymentMethod, String orderCategory, String orderValue) {
        this.mentor = mentor;
        this.user = user;
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.paymentMethod = paymentMethod;
        this.orderCategory = orderCategory;
        this.orderValue = orderValue;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getOrderCategory() {
        return orderCategory;
    }

    public void setOrderCategory(String orderCategory) {
        this.orderCategory = orderCategory;
    }

    public String getMentor() {
        return mentor;
    }

    public void setMentor(String mentor) {
        this.mentor = mentor;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getOrderValue() {
        return orderValue;
    }

    public void setOrderValue(String orderValue) {
        this.orderValue = orderValue;
    }

    @Override
    public String toString() {
        return "HistoryClass{" +
                "mentor='" + mentor + '\'' +
                ", user='" + user + '\'' +
                ", orderId='" + orderId + '\'' +
                ", orderDate='" + orderDate + '\'' +
                ", orderStatus='" + orderStatus + '\'' +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", orderCategory='" + orderCategory + '\'' +
                ", orderValue='" + orderValue + '\'' +
                '}';
    }
}
