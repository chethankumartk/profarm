package com.example.chethan.industrain;

import android.location.LocationManager;

public class emiclass {
    public String getPa() {
        return pa;
    }

    public String getInterest() {
        return interest;
    }

    public String getBal() {
        return bal;
    }

    private String pa;

    public void setPa(String pa) {
        this.pa = pa;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    public void setBal(String bal) {
        this.bal = bal;
    }

    private String interest;
    private String bal;

    public String getSno() {
        return sno;
    }

    public void setSno(String sno) {
        this.sno = sno;
    }

    private String sno;



    public emiclass() {
    }

    public emiclass(String pa, String interest, String bal,String sno) {
        this.pa=pa;
        this.interest=interest;
        this.bal=bal;
        this.sno=sno;
    }


}