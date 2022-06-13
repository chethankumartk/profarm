package com.example.chethan.industrain.newsadapter;

import java.util.Date;

public class news {

    public String getMainhead() {
        return mainhead;
    }

    public String getSubhead() {
        return subhead;
    }

    public String getImageurl() {
        return imageurl;
    }

    public String getFulltext() {
        return fulltext;
    }

    public Date getUploaddate() {
        return uploaddate;
    }

    String mainhead;
    String subhead;
    String imageurl;
    String fulltext;

    public String getRefid() {
        return refid;
    }

    String refid;
    Date uploaddate;

    public news()
    {

    }
    public news(String mainhead,String subhead,String imageurl,String fulltext,Date uploaddate,String refid)
    {
        this.mainhead=mainhead;
        this.subhead=subhead;
        this.imageurl=imageurl;
        this.fulltext=fulltext;
        this.uploaddate=uploaddate;
        this.refid=refid;

    }
}

