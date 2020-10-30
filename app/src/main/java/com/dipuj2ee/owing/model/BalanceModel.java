package com.dipuj2ee.owing.model;

import java.util.Date;

public class BalanceModel {
    String id;
    String userid;
    String cusid;
    Double drBalance;
    Double crBalance;
    String trdate;



    public BalanceModel(String userid, String cusid, Double drBalance, Double crBalance, String trdate) {
        this.userid = userid;
        this.cusid = cusid;
        this.drBalance = drBalance;
        this.crBalance = crBalance;
        this.trdate = trdate;
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

    public String getCusid() {
        return cusid;
    }

    public void setCusid(String cusid) {
        this.cusid = cusid;
    }

    public Double getDrBalance() {
        return drBalance;
    }

    public void setDrBalance(Double drBalance) {
        this.drBalance = drBalance;
    }

    public Double getCrBalance() {
        return crBalance;
    }

    public void setCrBalance(Double crBalance) {
        this.crBalance = crBalance;
    }

    public String getTrdate() {
        return trdate;
    }

    public void setTrdate(String trdate) {
        this.trdate = trdate;
    }
}
