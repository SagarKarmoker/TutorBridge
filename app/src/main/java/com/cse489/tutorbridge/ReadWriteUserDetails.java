package com.cse489.tutorbridge;

public class ReadWriteUserDetails {
    public String textName,textPhone,textAddress,textEducation,
            textExpert,textYears,textPrice,textDescription;
    public ReadWriteUserDetails(String textName,String textPhone,String textAddress,String textEducation,
                                String textExpert,String textYears,String textPrice,String textDescription){
        this.textName = textName;
        this.textPhone = textPhone;
        this.textAddress = textAddress;
        this.textEducation = textEducation;
        this.textExpert = textExpert;
        this.textYears = textYears;
        this.textPrice = textPrice;
        this.textDescription = textDescription;
    }

    public String getTextName() {
        return textName;
    }

    public void setTextName(String textName) {
        this.textName = textName;
    }

    public String getTextPhone() {
        return textPhone;
    }

    public void setTextPhone(String textPhone) {
        this.textPhone = textPhone;
    }

    public String getTextAddress() {
        return textAddress;
    }

    public void setTextAddress(String textAddress) {
        this.textAddress = textAddress;
    }

    public String getTextEducation() {
        return textEducation;
    }

    public void setTextEducation(String textEducation) {
        this.textEducation = textEducation;
    }

    public String getTextExpert() {
        return textExpert;
    }

    public void setTextExpert(String textExpert) {
        this.textExpert = textExpert;
    }

    public String getTextYears() {
        return textYears;
    }

    public void setTextYears(String textYears) {
        this.textYears = textYears;
    }

    public String getTextPrice() {
        return textPrice;
    }

    public void setTextPrice(String textPrice) {
        this.textPrice = textPrice;
    }

    public String getTextDescription() {
        return textDescription;
    }

    public void setTextDescription(String textDescription) {
        this.textDescription = textDescription;
    }

    @Override
    public String toString() {
        return "ReadWriteUserDetails{" +
                "textName='" + textName + '\'' +
                ", textPhone='" + textPhone + '\'' +
                ", textAddress='" + textAddress + '\'' +
                ", textEducation='" + textEducation + '\'' +
                ", textExpert='" + textExpert + '\'' +
                ", textYears='" + textYears + '\'' +
                ", textPrice='" + textPrice + '\'' +
                ", textDescription='" + textDescription + '\'' +
                '}';
    }
}
