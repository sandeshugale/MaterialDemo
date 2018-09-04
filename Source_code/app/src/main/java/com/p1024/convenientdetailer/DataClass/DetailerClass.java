package com.p1024.convenientdetailer.DataClass;

import java.io.Serializable;

/**
 * Created by administrator on 3/6/18.
 */

public class DetailerClass implements Serializable {
    private String mStrServiceCategoryID;

    public String getmStrServiceCategoryID() {
        return mStrServiceCategoryID;
    }

    public void setmStrServiceCategoryID(String mStrServiceCategoryID) {
        this.mStrServiceCategoryID = mStrServiceCategoryID;
    }

    public String getmStrServiceCategoryName() {
        return mStrServiceCategoryName;
    }

    public void setmStrServiceCategoryName(String mStrServiceCategoryName) {
        this.mStrServiceCategoryName = mStrServiceCategoryName;
    }

    private String mStrServiceCategoryName;
}
