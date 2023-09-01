package com.cse489.tutorbridge.modal;

public class OrderModal {
    private String orderId, orderStatus, orderDate, paymentMethod, catagory;
    private String mentorId, userId;

    public OrderModal(){
        //empty
    }

    @Override
    public String toString() {
        return "OrderModal{" +
                "orderId='" + orderId + '\'' +
                ", orderStatus='" + orderStatus + '\'' +
                ", orderDate='" + orderDate + '\'' +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", catagory='" + catagory + '\'' +
                '}';
    }

    public OrderModal( String mentorId, String userId,String orderId, String orderStatus, String orderDate, String paymentMethod, String catagory) {
        this.orderId = orderId;
        this.orderStatus = orderStatus;
        this.orderDate = orderDate;
        this.paymentMethod = paymentMethod;
        this.catagory = catagory;
        this.mentorId=mentorId;
        this.userId = userId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getCatagory() {
        return catagory;
    }

    public void setCatagory(String catagory) {
        this.catagory = catagory;
    }

    public String getMetorId() {
        return mentorId;
    }

    public void setMetorId(String metorId) {
        this.mentorId = metorId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
