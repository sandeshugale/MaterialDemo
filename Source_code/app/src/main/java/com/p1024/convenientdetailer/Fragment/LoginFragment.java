package com.p1024.convenientdetailer.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.internal.CallbackManagerImpl;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.p1024.convenientdetailer.Activity.LoginActivity;
import com.p1024.convenientdetailer.Activity.MainDashboard;
import com.p1024.convenientdetailer.CustomViews.CustomEditText;
import com.p1024.convenientdetailer.CustomViews.CustomTextView;
import com.p1024.convenientdetailer.CustomViews.TransparentProgressDialog;
import com.p1024.convenientdetailer.R;
import com.p1024.convenientdetailer.Session.SessionClass;
import com.p1024.convenientdetailer.Session.SessionConstants;
import com.p1024.convenientdetailer.Utility.AvailableNetwork;
import com.p1024.convenientdetailer.Validation.Validation;

import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by administrator on 2/9/18.
 */

public class LoginFragment extends Fragment implements View.OnClickListener,
        GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = LoginActivity.class.getSimpleName();
    private GoogleApiClient mGoogleApiClient;
    ImageView mGooglePlusLogin, mFacebookLogin;
    private View mParentView;
    private CustomTextView mTxtLogin, mTxtForgotPassword;
    private CallbackManager callbackManager;
    private LoginManager loginManager = null;
    private CustomEditText mEdtUseremail, mEdtPassword;
    private String mStrwsLogin = "";
    private AQuery mAquery;
    private TransparentProgressDialog mProgressDlg;
    private String mStrSocialRegistration;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(getActivity());
        mParentView = inflater.inflate(R.layout.fragmentlogin_layout, container, false);
        setUI();
        return mParentView;
    }

    /**
     * Inflate All views.
     */
    private void setUI() {
        mAquery = new AQuery(getActivity());
        mProgressDlg = new TransparentProgressDialog(getActivity(), R.drawable.ic_loader_image);
        mGooglePlusLogin = mParentView.findViewById(R.id.googleplus);
        mTxtLogin = mParentView.findViewById(R.id.txtlogin);
        mFacebookLogin = mParentView.findViewById(R.id.fblogin);
        mTxtForgotPassword = mParentView.findViewById(R.id.txtforgotpassword);
        mEdtUseremail = mParentView.findViewById(R.id.edt_login_email);
        mEdtPassword = mParentView.findViewById(R.id.edt_login_password);
        callbackManager = CallbackManager.Factory.create();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(@Nullable Bundle bundle) {
                        Log.d("Status", "CONNECTED");
                    }

                    @Override
                    public void onConnectionSuspended(int i) {
                        Log.d("Status", "SUSPENDED");
                    }
                })

                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        setListener();

    }

    /**
     * set Views Listener.
     */
    private void setListener() {
        mGooglePlusLogin.setOnClickListener(this);
        mTxtLogin.setOnClickListener(this);
        mFacebookLogin.setOnClickListener(this);
        mTxtForgotPassword.setOnClickListener(this);
    }

    /**
     * On click event on View's
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        Fragment fragment;
        Bundle bundleF = new Bundle();
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        switch (v.getId()) {
            case R.id.googleplus:
                if (!checkIsGoogleSignIn()) {
                    signIn();
                } else {
                    signOut();
                    signIn();
                }
                break;
            case R.id.fblogin:
                regFacebook();
                break;
            case R.id.txtlogin:
                if (checkValidation()) {
                    wsLogin();
                }

                break;
            case R.id.txtforgotpassword:
                fragment = new ForgotPasswordFragment();
                fragment.setArguments(bundleF);
                ft.replace(R.id.content_frame, fragment).addToBackStack("").commit();
                break;

        }
    }

    /**
     * Check Login Validation.
     *
     * @return
     */
    private boolean checkValidation() {
        if (mEdtUseremail.isEmpty()) {
            mEdtUseremail.setError("Please enter email.");
            return false;
        } else if (!Validation.checkEmail(mEdtUseremail.getText().toString().trim())) {
            mEdtUseremail.setError("Please enter valid email.");
            return false;
        } else if (mEdtPassword.isEmpty()) {
            mEdtPassword.setError("Please enter password.");
            return false;
        }
        return true;
    }

    /**
     * Call Login Webservice
     */
    private void wsLogin() {
        mStrwsLogin = getActivity().getString(R.string.WS_HOST_URL) + getActivity().getString(R.string.WS_LOGIN);
        HashMap<String, Object> mparam = new HashMap<>();
        mparam.put("email_id", mEdtUseremail.getText().toString());
        mparam.put("password", mEdtPassword.getText().toString());
        if (AvailableNetwork.isNetworkAvailable(getActivity())) {
            mAquery.progress(mProgressDlg).ajax(mStrwsLogin, mparam, JSONObject.class, LoginCallback);

        } else {
            Toast.makeText(getActivity(), "" + getActivity().getString(R.string.str_network_error), Toast.LENGTH_SHORT).show();
        }
    }

    private void wsSocialLogin(String checkSocialStatus, String email, String socialID, String firstName, String lastName) {
        mStrSocialRegistration = getActivity().getString(R.string.WS_HOST_URL) + getActivity().getString(R.string.WS_SOCIAL_LOGIN);
        HashMap<String, Object> mParams = new HashMap<>();
        mParams.put("user_email", email);
        if (checkSocialStatus.equalsIgnoreCase("1")) {
            mParams.put("fb_id", socialID);
            mParams.put("google_id", "");
        } else {
            mParams.put("google_id", socialID);
            mParams.put("fb_id", "");
        }
        if (AvailableNetwork.isNetworkAvailable(getActivity())) {
            mAquery.progress(mProgressDlg).ajax(mStrSocialRegistration, mParams, JSONObject.class, LoginCallback);
        } else {
            Toast.makeText(getActivity(), "" + getActivity().getString(R.string.str_network_error), Toast.LENGTH_SHORT).show();
        }

    }


    /**
     * Server(webservice) Responce.
     */
    AjaxCallback<JSONObject> LoginCallback = new AjaxCallback<JSONObject>() {
        @Override
        public void callback(String url, JSONObject object, AjaxStatus status) {
            super.callback(url, object, status);
            Log.d("URL", "" + url);
            Log.d("Respoce", "" + object);
            if (object != null) {
                try {
                    if (url.equalsIgnoreCase(mStrwsLogin)) {
                        if (object.getString("error_code").equalsIgnoreCase("0")) {
                            JSONObject mJsonData = object.getJSONObject("data");
                            JSONObject mJsonUserInfo = mJsonData.getJSONObject("user_information");
                            if (mJsonUserInfo.getString("user_status").equalsIgnoreCase("1")) {
                                SessionClass.getInstance().updateValue(getActivity(), SessionConstants.KEY_USER_EMAIL, mJsonData.getString("email"));
                                SessionClass.getInstance().updateValue(getActivity(), SessionConstants.KEY_USER_ID, mJsonUserInfo.getString("user_id"));
                                SessionClass.getInstance().updateValue(getActivity(), SessionConstants.KEY_USER_NAME, mJsonUserInfo.getString("user_name"));
                                SessionClass.getInstance().updateValue(getActivity(), SessionConstants.KEY_USER_TYPE, mJsonUserInfo.getString("user_type"));
                                SessionClass.getInstance().updateValue(getActivity(), SessionConstants.KEY_USER_MOBILE_NUMBER, mJsonUserInfo.getString("user_mobile"));
                                switchToDashboard();
                            }else if (mJsonUserInfo.getString("user_status").equalsIgnoreCase("2")){
                                showAlert("We apologies, your account is blocked by administrator. Please contact to administrator for further details.",-1);
                            }else if (mJsonUserInfo.getString("user_status").equalsIgnoreCase("0")){
                                showAlert("We found your account is not yet verified. Kindly see the verification email, sent to your email address.",-1);
                            }

                        } else if (object.getString("error_code").equalsIgnoreCase("1")) {
                            showAlert("The email or password is incorrect. Try again.", -1);
                        }else if (object.getString("error_code").equalsIgnoreCase("2")) {
                            showAlert(object.getString("msg"), -1);
                        }
                    } else if (url.equalsIgnoreCase(mStrSocialRegistration)) {
                        if (object.getString("error_code").equalsIgnoreCase("0")) {
                            JSONObject mJsonData = object.getJSONObject("data");
                            SessionClass.getInstance().updateValue(getActivity(), SessionConstants.KEY_USER_EMAIL, mJsonData.getString("email"));
                            JSONObject mJsonUserInfo = mJsonData.getJSONObject("user_information");
                            SessionClass.getInstance().updateValue(getActivity(), SessionConstants.KEY_USER_ID, mJsonUserInfo.getString("user_id"));
                            SessionClass.getInstance().updateValue(getActivity(), SessionConstants.KEY_USER_NAME, mJsonUserInfo.getString("user_name"));
                            SessionClass.getInstance().updateValue(getActivity(), SessionConstants.KEY_USER_TYPE, mJsonUserInfo.getString("user_type"));
                            SessionClass.getInstance().updateValue(getActivity(), SessionConstants.KEY_USER_MOBILE_NUMBER, mJsonUserInfo.getString("user_mobile"));
                            switchToDashboard();
                        } else {
                            showAlert(object.getString("msg"), -1);
                        }


                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    };

    private void switchToDashboard() {
        Intent intent = new Intent(getActivity(), MainDashboard.class);
        startActivity(intent);
    }

    /**
     * call google plus screen to check login
     */
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, 007);
    }

    /**
     * call  OnActivityresult to handle responce
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == 007) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        } /*else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }*/ else {
            if (FacebookSdk.isFacebookRequestCode(requestCode)) {
                callbackManager.onActivityResult(requestCode, resultCode, data);


            }
        }
    }

    /**
     * Handle Google plus Login
     *
     * @param result
     */
    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            Log.e(TAG, "display name: " + acct.getDisplayName());
            String personName = acct.getDisplayName();
            String[] Name = personName.split(" ");
          //  String personPhotoUrl = acct.getPhotoUrl().toString();
            String email = acct.getEmail();
            String personId = acct.getId();
            wsSocialLogin("0", email, personId, Name[0], Name[1]);

        } else {
            // Signed out, show unauthenticated UI.
            /*updateUI(false);*/
        }
    }

    /**
     * Auto generated method by GooglePlusClient
     *
     * @param connectionResult
     */
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onPause() {
        super.onPause();
        // mGoogleApiClient.stopAutoManage(getActivity());
        // mGoogleApiClient.disconnect();
        Log.d("onPause", String.valueOf(mGoogleApiClient.isConnected()));

    }

    private boolean checkIsGoogleSignIn() {
        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Google plus sign out method
     **/
    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        Log.d("Status", +status.getStatusCode() + "msg" + status.getStatusMessage());

                    }
                });


    }

    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
//           mGoogleApiClient.stopAutoManage(this);

            mGoogleApiClient.disconnect();
            Log.d("OnStop", String.valueOf(mGoogleApiClient.isConnected()));
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
        Log.d("onStart", String.valueOf(mGoogleApiClient.isConnected()));

    }

    /**
     * Facebook Login
     */
    private void regFacebook() {
        callbackManager = CallbackManager.Factory.create();
        loginManager = LoginManager.getInstance();
        if (loginManager == null) {
            LoginManager.getInstance().logOut();
        }
        loginManager.logInWithReadPermissions(getActivity(), Arrays.asList("public_profile", "user_friends", "email"));
        List<String> permissions = new ArrayList<>();
        permissions.add("email");
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {

                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {
                            Log.e("*Facebook", "Facebook responce-->" + response.toString());

                                   /* mStrFbId = object.getString("id");
                                    mName = object.getString("name");
                                    name = mName.split(" ");*/
                            String[] mStrName = object.getString("name").split(" ");
                            wsSocialLogin("1", object.getString("email"), object.getString("id"), mStrName[0], mStrName[1]);

                            // SocialLogin();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                           /* @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                try {
                                    Log.e("*Facebook", "Facebook responce-->" + response.toString());
                                    *//*fbEmail = object.getString("email");
                                    mStrFbId = object.getString("id");
                                    mName = object.getString("name");
                                    name = mName.split(" ");*//*
                                    Toast.makeText(getActivity(), ""+object.getString("email")+" "+object.getString("id"), Toast.LENGTH_SHORT).show();
                                    LoginManager.getInstance().logOut();

                                   // SocialLogin();

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }*/
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,link,email,picture");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException exception) {

                exception.printStackTrace();
            }
        });
    }

    private void showAlert(String message, final int flg) {
        android.support.v7.app.AlertDialog.Builder aBuilder = new android.support.v7.app.AlertDialog.Builder(getActivity());
        aBuilder.setCancelable(false);
        aBuilder.setMessage(message);
        aBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                setNextFlg(flg);
            }
        });
        aBuilder.create();
        aBuilder.show();
    }

    private void setNextFlg(int flg) {
        if (flg == -1) {

        }
    }
}
