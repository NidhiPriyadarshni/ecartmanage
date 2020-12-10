package com.example.sweven;

public class AddressesModel {


    private String fullName;
    private String address;
    private String pincode;
    private String phone;
    private Boolean selected;

    public AddressesModel(String fullName, String address, String pincode, String phone) {
        this.fullName = fullName;
        this.address = address;
        this.pincode = pincode;
        this.phone=phone;
        this.selected=false;
    }
    public AddressesModel(){
        this.selected=false;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone=phone;
    }

    public Boolean getSelected(){return selected;}

    public void setSelected(Boolean selected){this.selected=selected;}
}
