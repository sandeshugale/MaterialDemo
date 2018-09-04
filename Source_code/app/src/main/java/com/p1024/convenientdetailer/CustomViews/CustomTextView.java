
package com.p1024.convenientdetailer.CustomViews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import com.p1024.convenientdetailer.R;
import com.p1024.convenientdetailer.Utility.TypeFaceProvider;


/**
 * @author Windows
 */
public class CustomTextView extends TextView {

    private static final String TAG = "TextView";

    public CustomTextView(Context context) {
        super(context);
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setCustomFont(context, attrs);
    }

    public CustomTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setCustomFont(context, attrs);
    }

    private void setCustomFont(Context ctx, AttributeSet attrs) {
        TypedArray a = ctx.obtainStyledAttributes(attrs, R.styleable.CustomView);
        String customFont = a.getString(R.styleable.CustomView_customFont);
        setCustomFont(ctx, customFont);
        a.recycle();
    }

    public boolean setCustomFont(Context ctx, String asset) {
        Typeface tf = null;
        try {

            //tf = Typeface.createFromAsset(ctx.getAssets(), asset);
            //tf = Typeface.createFromAsset(ctx.getAssets(), asset);
            //tf = Typeface.createFromAsset(ctx.getAssets(), asset);

            tf = TypeFaceProvider.getTypeFace(ctx, asset);
        } catch (Exception e) {
            Log.e(TAG, "Could not get typeface: " + e.getMessage() + getText());
            return false;
        }

        setTypeface(tf);
        return true;
    }

}
