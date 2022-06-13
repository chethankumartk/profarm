package com.example.chethan.industrain.ViewMadiroList;

public class viewcontract {

    public viewcontract()
    {

    }
    public viewcontract(String email,String phone, String refid,String name,String eid,String refidd)
    {
        this.email=email;
        this.phone=phone;
        this.refid=refid;
        this.name=name;
        this.eid=eid;
        this.refidd=refidd;
    }

    String email;
    String phone;
    String refid;
    String name;
    String eid;

    public String getRefidd() {
        return refidd;
    }

    String refidd;

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getRefid() {
        return refid;
    }

    public String getName() {
        return name;
    }

    public String getEid() {
        return eid;
    }
}
