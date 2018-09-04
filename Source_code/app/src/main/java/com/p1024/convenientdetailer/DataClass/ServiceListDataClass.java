package com.p1024.convenientdetailer.DataClass;

import java.io.Serializable;

/**
 * Created by administrator on 2/15/18.
 */

public class ServiceListDataClass implements Serializable {
    public String getmStrServiceDesc() {
        return mStrServiceDesc;
    }

    public void setmStrServiceDesc(String mStrServiceDesc) {
        this.mStrServiceDesc = mStrServiceDesc;
    }

    public String getmStrCatID() {
        return mStrCatID;
    }

    public void setmStrCatID(String mStrCatID) {
        this.mStrCatID = mStrCatID;
    }

    private String mStrServiceDesc,mStrCatID;
    public String getmStrServicename() {
        return mStrServicename;
    }

    public void setmStrServicename(String mStrServicename) {
        this.mStrServicename = mStrServicename;
    }

    public String getmStrPrice() {
        return mStrPrice;
    }

    public void setmStrPrice(String mStrPrice) {
        this.mStrPrice = mStrPrice;
    }

    String mStrServicename, mStrPrice;

}
