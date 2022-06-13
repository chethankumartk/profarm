package com.example.chethan.industrain.fragmentclasses;

public class WishlistAdd {


    public String getId1() {
        return id1;
    }

    public void setId1(String id1) {
        this.id1 = id1;
    }

    String id1;

    public String getEid() {
        return eid;
    }

    public void setEid(String eid) {
        this.eid = eid;
    }

    String eid;

    public WishlistAdd() {
    }
    public WishlistAdd(String id1,String eid,String imageurl,String cropname,String locality,int price)
    {
       this.id1=id1;
       this.eid=eid;
       this.imageurl=imageurl;
       this.cropname=cropname;
       this.locality=locality;
       this.price=price;;
    }
    String imageurl;

    public String getImageurl() {
        return imageurl;
    }

    public String getCropname() {
        return cropname;
    }

    public String getLocality() {
        return locality;
    }

    public int getPrice() {
        return price;
    }

    String cropname;
    String locality;
    int price;

}

