package com.p1024.convenientdetailer.Utility;

/**
 * Created by username on 20/3/17.
 */

import android.content.Context;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MyValueFormatter {
    public String strFormattedDate;
    private long seconds = 1000; //(1000)
    private long minutes = seconds * 60; //(60 * 1000)
    private long hours = minutes * 60; //(60 * 60 * 1000)
    private long days = hours * 24; //(24 * 60 * 60 * 1000)

    /**
     * Convert Date Format
     *
     * @param mContext             will be the Context
     * @param mStrInputDateFormat  will be the Input Date Format.(eg."yyyy-MM-dd)
     * @param mStrOutputDateFormat will be the Output Date Format.(eg."dd-MM-yyyy)
     * @param mStrDate             will be the Actual Date To Be Formatted.
     * @return strFormattedDate will be the formatted date.
     */
    public String getFormattedDate(Context mContext, String mStrInputDateFormat, String mStrOutputDateFormat, String mStrDate) {
        Date myDate = null;
        SimpleDateFormat inputDateFormat = new SimpleDateFormat(mStrInputDateFormat);
        SimpleDateFormat outputDateFormat = new SimpleDateFormat(mStrOutputDateFormat);
        try {
            myDate = inputDateFormat.parse(mStrDate);
            strFormattedDate = outputDateFormat.format(myDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return strFormattedDate;
    }

    public Date getFormattedDate(Context mContext, String mStrInputDateFormat, String mStrDate) {
        Date myDate = null;
        SimpleDateFormat inputDateFormat = new SimpleDateFormat(mStrInputDateFormat);
        try {
            myDate = inputDateFormat.parse(mStrDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return myDate;
    }

    public String getFormattedDate(Context mContext, String mStrInputDateFormat, Date mDate) {
        String mStr="";
        SimpleDateFormat inputDateFormat = new SimpleDateFormat(mStrInputDateFormat);
        try {
            mStr= inputDateFormat.format (mDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mStr;
    }

    /**
     * COMMA FORMATTING
     * Convert Into Input Value Into Applied Pattern
     *
     * @param value will be the value to be formatted.
     * @return formatter.format(value) will be the result value.
     */
    public String getFormattedValue(long value) {
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        formatter.applyPattern("#,###,###,###,###,###");
        return formatter.format(value);
    }


    /**
     * Get Elapsed Time in SECONDS, MINUTES, HOURS, DAYS
     *
     * @param mContext            will be the Context
     * @param mStrInputDateFormat will be the Input Date Format.(eg."yyyy-MM-dd)
     * @param mStrDate            will be the Actual Date To Be Formatted.
     * @param mStrElapsedTimeIn   will be the Difference You Want In.(eg."dd-MM-yyyy)
     * @return elapsedTime will be the result value.
     */
    public int getElapsedTime(Context mContext, String mStrInputDateFormat, String mStrDate, String mStrElapsedTimeIn) {
        int elapsedTime = 0;
        Date myDate = null;
        SimpleDateFormat inputDateFormat = new SimpleDateFormat(mStrInputDateFormat);
        try {
            myDate = inputDateFormat.parse(mStrDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(new Date());
        cal2.setTime(myDate);

        switch (mStrElapsedTimeIn) {
            case "SECONDS":
                elapsedTime = (int) ((new Date().getTime() - myDate.getTime()) / (seconds));//(1000)
                break;
            case "MINUTES":
                elapsedTime = (int) ((new Date().getTime() - myDate.getTime()) / (minutes));//(60 * 1000)
                break;
            case "HOURS":
                elapsedTime = (int) ((new Date().getTime() - myDate.getTime()) / (hours));//(60 * 60 * 1000)
                break;
            case "DAYS":
                elapsedTime = (int) ((new Date().getTime() - myDate.getTime()) / (days));//(24 * 60 * 60 * 1000)
                break;
        }
        return elapsedTime;
    }


    /**
     * Convert date in string format to Date format
     *
     * @param strdate which you have to convert in Date format
     * @param format  of the date like "yyyy-MM-dd"
     * @return date in Date format
     */
    public static Date stringToDate(String strdate, String format) {
        Date date = null;
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        try {
            date = formatter.parse(strdate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * use for add postfix to the number like: 1st, 3rd..
     *
     * @param number which you have to add postfix
     * @return number in string with postfix
     */
    public static String getPostFixForNumber(int number) {
        String strValue;
        // int npos = Integer.valueOf(Pos);

        switch (number % 10) {
            case 1:
                strValue = (number % 100 == 11) ? "th" : "st";
                break;
            case 2:
                strValue = (number % 100 == 12) ? "th" : "nd";
                break;
            case 3:
                strValue = (number % 100 == 13) ? "th" : "rd";
                break;
            default:
                strValue = "th";
                break;
        }
        return number + strValue;
    }

}