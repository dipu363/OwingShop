package com.dipuj2ee.owing.model;

public class CustomerInfoModel {

    String id;
    String name;
    String address;
    String phone;
    String userid;
    String duebalance;

    public CustomerInfoModel() {
    }

    public CustomerInfoModel(String duebalance) {

        this.duebalance = duebalance;

    }

    public CustomerInfoModel(String id, String name, String address, String phone, String userid) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.userid = userid;
    }

    public CustomerInfoModel(String id, String name, String address, String phone, String userid, String duebalance) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.userid = userid;
        this.duebalance = duebalance;
    }

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getDuebalance() {
        return duebalance;
    }

    public void setDuebalance(String duebalance) {
        this.duebalance = duebalance;
    }
}
