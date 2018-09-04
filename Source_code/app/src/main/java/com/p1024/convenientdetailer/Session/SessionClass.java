package com.p1024.convenientdetailer.Session;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by administrator on 11/1/17.
 */
public class SessionClass {
    public static SharedPreferences mSharedPreference;
    public static SharedPreferences.Editor mShEditor;
    public static SessionClass ourInstance = null;

    public static SessionClass getInstance() {
        if (ourInstance == null) {
            ourInstance = new SessionClass();
        }
        return ourInstance;
    }

    private void init(Context mContext) {
        mSharedPreference = mContext.getSharedPreferences(SessionConstants.KEY_PROJECT_NAME, Context.MODE_PRIVATE);
        mShEditor = mSharedPreference.edit();
    }

    public String getValue(Context mContext, String mStrKey) {
        if (mSharedPreference == null) {
            init(mContext);
        }
        return mSharedPreference.getString(mStrKey, "");
    }
    public int getValueInt(Context mContext, String mStrKey) {
        if (mSharedPreference == null) {
            init(mContext);
        }
        return mSharedPreference.getInt (mStrKey, -1);
    }


    public void updateValue(Context mContext, String mStrKey, String mStrValue) {
        if (mSharedPreference == null) {
            init(mContext);
        }
        mShEditor.putString(mStrKey, mStrValue);
        mShEditor.commit();
    }
    public void updateValue(Context mContext, String mStrKey, int mStrValue) {
        if (mSharedPreference == null) {
            init(mContext);
        }
        mShEditor.putInt (mStrKey, mStrValue);
        mShEditor.commit();
    }

    public void sessionLogout(Context mContext) {
        if (mSharedPreference == null) {
            init(mContext);
        }
        /*String mStrDeviceToken = SessionClass.getInstance().getValue(mContext, SessionConstants.KEY_FIREBASE_TOKEN_ID);
        String mStrLanguage= SessionClass.getInstance().getValue(mContext, SessionConstants.KEY_LANGUAGE);*/
        mShEditor.clear();
      /*  SessionClass.getInstance().updateValue(mContext, SessionConstants.KEY_FIREBASE_TOKEN_ID, mStrDeviceToken);
        SessionClass.getInstance().updateValue(mContext, SessionConstants.KEY_LANGUAGE, mStrLanguage);*/
        mShEditor.commit();
    }
}
