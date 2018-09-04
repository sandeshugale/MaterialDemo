package com.p1024.convenientdetailer.DataClass;

import java.io.Serializable;

/**
 * Created by administrator on 3/22/18.
 */

public class ServiceDataClass implements Serializable {


    /*-------------service list in public profile-------------*/
    private String mStrServicename,mStrServiceprice,mStrServiceimage;

    public String getmStrServicename() {
        return mStrServicename;
    }

    public void setmStrServicename(String mStrServicename) {
        this.mStrServicename = mStrServicename;
    }

    public String getmStrServiceprice() {
        return mStrServiceprice;
    }

    public void setmStrServiceprice(String mStrServiceprice) {
        this.mStrServiceprice = mStrServiceprice;
    }

    public String getmStrServiceimage() {
        return mStrServiceimage;
    }

    public void setmStrServiceimage(String mStrServiceimage) {
        this.mStrServiceimage = mStrServiceimage;
    }
  /*------------------ Service END--------------------------------------*/


    /*---------------------Rating and Review Method----------------*/
    String mStrReviewerName,mStrReviewDescription;
    String mStrReviewStars;

    public String getmStrReviewerName() {
        return mStrReviewerName;
    }

    public void setmStrReviewerName(String mStrReviewerName) {
        this.mStrReviewerName = mStrReviewerName;
    }

    public String getmStrReviewDescription() {
        return mStrReviewDescription;
    }

    public void setmStrReviewDescription(String mStrReviewDescription) {
        this.mStrReviewDescription = mStrReviewDescription;
    }

    public String getmStrReviewStars() {
        return mStrReviewStars;
    }

    public void setmStrReviewStars(String mStrReviewStars) {
        this.mStrReviewStars = mStrReviewStars;
    }
  /*------------------Review END--------------------------------------*/


}
