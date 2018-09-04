package com.p1024.convenientdetailer.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.p1024.convenientdetailer.Activity.MainDashboard;
import com.p1024.convenientdetailer.CustomViews.CustomEditText;
import com.p1024.convenientdetailer.CustomViews.CustomTextView;
import com.p1024.convenientdetailer.CustomViews.TransparentProgressDialog;
import com.p1024.convenientdetailer.R;
import com.p1024.convenientdetailer.Utility.AvailableNetwork;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by administrator on 2/27/18.
 */

public class ForgotPasswordFragment extends Fragment  {
    private View mParentView;
    private CustomEditText mEdtEmail;
    private CustomTextView mTxtSendPasword;
    private String mStrURLForgotPassword;
    private TransparentProgressDialog mProgressdlg;
    private AQuery aQuery;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mParentView = inflater.inflate(R.layout.layout_forgotpassword, container, false);
        setUI();
        return mParentView;
    }

    private void setUI() {
        mEdtEmail = mParentView.findViewById(R.id.edtuseremail);
        mProgressdlg=new TransparentProgressDialog(getActivity(),R.drawable.ic_loader_image);
        aQuery=new AQuery(getActivity());
        mParentView.findViewById(R.id.txtforgotpassword).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wsForgotPass();
            }
        });
    }

    private void wsForgotPass()
    {
        mStrURLForgotPassword = getActivity().getString(R.string.WS_HOST_URL) + getActivity().getString(R.string.WS_FORGOT_PASSWORD);
        HashMap<String, Object> mParam = new HashMap<>();
        mParam.put("email_id", mEdtEmail.getText().toString().trim());
        if (AvailableNetwork.isNetworkAvailable(getActivity())) {
            aQuery.progress(mProgressdlg).ajax(mStrURLForgotPassword, mParam, JSONObject.class, new AjaxCallback<JSONObject>()
            {
                @Override
                public void callback(String url, JSONObject object, AjaxStatus status) {
                    super.callback(url, object, status);
                    if (object!=null)
                    {
                        try {
                            if (object.getString("error_code").equalsIgnoreCase("0"))
                            {
                                showAlert("Your password is sent to your registered email",1);
                            }else
                            {
                                showAlert("Please enter registered email address",-1);

                            }

                        }catch(Exception ex)
                        {
                            ex.printStackTrace();
                        }
                    }
                }
            });
        }


    }
    private void showAlert(String message, final int flag) {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
        mBuilder.setCancelable(false);
        mBuilder.setMessage(message);
        mBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                navigateFlag(flag);
                dialog.dismiss();
            }
        });

        mBuilder.create();
        mBuilder.show();
    }

    private void navigateFlag(int flag) {
        if (flag==1)
        {
            startActivity(new Intent(getActivity(), MainDashboard.class));
        }
    }

}
