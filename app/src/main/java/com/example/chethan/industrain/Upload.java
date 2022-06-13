package com.example.chethan.industrain;

public class Upload {
    private  String  mName,mImageUrl,idd,mCropname,mAddress,phon,eid;
    int price;

    public Upload() {
    }

    public Upload(String id,String mNam, String mImageur,String mCropnam,String mAddress,String phoneno,int price, String eid) {
        if(mNam.trim().equals(""))
        {
            mName="No Name";
        }
        this.mName = mNam;
        this.mImageUrl = mImageur;
        this.idd=id;
        this.mCropname=mCropnam;
        this.mAddress=mAddress;
        this.phon=phoneno;
        this.price=price;
        this.eid=eid;
    }


    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public String getIdd() {
        return idd;
    }

    public String getmAddress() {
        return mAddress;
    }

    public String getmCropname() {
        return mCropname;
    }
    public String getPhon()
    {
        return phon;
    }
    public String getEid()
    {
        return eid;
    }

    public int getPrice() {
        return price;
    }

    public void setImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
    }
}

