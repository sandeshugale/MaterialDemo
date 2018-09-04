package com.p1024.convenientdetailer.DataClass;

/**
 * Created by ubuntu on 20/3/17.
 */

public class CalenderDate {
    private String mStrDate="",mStrDay="",mStrFormatedDate="";
    private boolean isBookingAvailable=false;
    private boolean isClickOnTheView=false;


    public String getmStrDate() {
        return mStrDate;
    }

    public void setmStrDate(String mStrDate) {
        this.mStrDate = mStrDate;
    }

    public String getmStrDay() {
        return mStrDay;
    }

    public void setmStrDay(String mStrDay) {
        this.mStrDay = mStrDay;
    }

    public String getmStrFormatedDate() {
        return mStrFormatedDate;
    }

    public void setmStrFormatedDate(String mStrFormatedDate) {
        this.mStrFormatedDate = mStrFormatedDate;
    }

    public boolean isBookingAvailable() {
        return isBookingAvailable;
    }

    public void setBookingAvailable(boolean bookingAvailable) {
        isBookingAvailable = bookingAvailable;
    }

    public boolean isClickOnTheView() {
        return isClickOnTheView;
    }

    public void setClickOnTheView(boolean clickOnTheView) {
        isClickOnTheView = clickOnTheView;
    }
}
