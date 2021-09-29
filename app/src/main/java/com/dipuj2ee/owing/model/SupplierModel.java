package com.dipuj2ee.owing.model;

public class SupplierModel {


    String id;
    String userid;
    String supname;
    String srname;
    String phone;

    public SupplierModel(String id, String userid, String supname, String srname, String phone) {
        this.id = id;
        this.userid = userid;
        this.supname = supname;
        this.srname = srname;
        this.phone = phone;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getSupname() {
        return supname;
    }

    public void setSupname(String supname) {
        this.supname = supname;
    }

    public String getSrname() {
        return srname;
    }

    public void setSrname(String srname) {
        this.srname = srname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
