package com.p1024.convenientdetailer.Utility;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.p1024.convenientdetailer.CustomViews.TransparentProgressDialog;
import com.p1024.convenientdetailer.R;
import com.p1024.convenientdetailer.SSL.EasySSLSocketFactory;
import com.p1024.convenientdetailer.Tooltip.Tooltip;
import com.p1024.convenientdetailer.Tooltip.Typefaces;

/**
 * Created by ubuntu on 24/10/16.
 */
public class BaseClass extends Activity {
    public AQuery mAQuery;
    public TransparentProgressDialog mProgressDialog;
    public DisplayMetrics mDisplay;
    private static int width;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AjaxCallback.setNetworkLimit(24);
        AjaxCallback.setReuseHttpClient(true);
        AjaxCallback.setSSF(new EasySSLSocketFactory());

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        initVariables();

    }

    private void initVariables() {
        mAQuery = new AQuery(this);
        //mProgressDialog = new TransparentProgressDialog(this, R.drawable.ic_loader_image);
        mDisplay = getResources().getDisplayMetrics();
        width = mDisplay.widthPixels;

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
    }

    /**
     * Generate error message view and display.
     *
     * @param mView
     * @param message
     */

    public static void setMakeTooltip(Context mContext, View mView, String message) {

        //Typeface myFont = Typefaces.get(mContext, mView.getResources().getString(R.string.str_font_comfortaa_regular));
        Tooltip.make(mContext, new Tooltip.Builder(101)
                .anchor(mView, Tooltip.Gravity.RIGHT)
                .closePolicy(new Tooltip.ClosePolicy()
                        .insidePolicy(true, false)
                        .outsidePolicy(true, false), 5000)
                .activateDelay(500)
                .showDelay(300)
                .text(message)
                .maxWidth(width)//mView.getWidth()
                .withArrow(true)
                .withOverlay(true)
                //.typeface(myFont)
                .fitToScreen(true)
                .setTextColor("#000000")//#3A3839
                .floatingAnimation(Tooltip.AnimationBuilder.DEFAULT)
                .build()
        ).show();

    }

    public static void getLanguage(Context mContext) {
        // ChangeLanguage.getInstance().changeLanguage(mContext, Session.getInstance().getValue(mContext, SessionConstants.KEY_CHANGE_LANGUAGE));
    }
}
