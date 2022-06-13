package com.example.chethan.industrain.notificationskalsakke;

public class Notifications {

    String from;
    String message;

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    String rid;

    public String getS() {
        return s;
    }

    public void setS(String s) {
        this.s = s;
    }

    String s;

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    String cid;

    public Notifications() {

    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Notifications(String from, String message,String cid,String s,String rid) {
        this.from = from;
        this.message = message;
        this.cid=cid;
        this.s=s;
        this.rid=rid;
    }
}
