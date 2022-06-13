package com.example.chethan.industrain.homeadapters;

import java.util.Date;

public class Upload1 {

    String refid,eid;
    double latitude,longitude;
    Date dateadded;

    public long getViews() {
        return views;
    }

    long views;

    public String getRefid() {
        return refid;
    }

    public String getEid() {
        return eid;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public Date getDateadded() {
        return dateadded;
    }

    public Date getDateupdated() {
        return dateupdated;
    }

    public String getImageurl() {
        return imageurl;
    }

    public String getFarmername() {
        return farmername;
    }

    public String getCropname() {
        return cropname;
    }

    public String getCategory() {
        return category;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getState() {
        return state;
    }

    public String getDistrict() {
        return district;
    }

    public String getLocality() {
        return locality;
    }

    public String getAddress() {
        return address;
    }

    public int getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    Date dateupdated;
    String imageurl,farmername,cropname,category,phone,email,state,district,locality,address;
    int price,quantity;

    public Upload1(String refid,String eid,double latitude,double longitude,Date dateadded,Date dateupdated,String imageurl,
                   String farmername,String cropname,String category,String phone,String email,String state,String district,
                   String locality,int quantity,int price,String address,long views)
    {
        this.refid=refid;
        this.eid=eid;
        this.latitude=latitude;
        this.longitude=longitude;
        this.dateadded=dateadded;
        this.dateupdated=dateupdated;
        this.imageurl=imageurl;
        this.farmername=farmername;
        this.cropname=cropname;
        this.category=category;
        this.phone=phone;
        this.email=email;
        this.state=state;
        this.district=district;
        this.locality=locality;
        this.quantity=quantity;
        this.price=price;
        this.address=address;
        this.views=views;
    }
    public Upload1()
    {

    }
}
