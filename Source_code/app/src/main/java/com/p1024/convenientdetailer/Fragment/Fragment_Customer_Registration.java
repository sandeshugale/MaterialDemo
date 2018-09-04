package com.p1024.convenientdetailer.Fragment;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
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
import com.p1024.convenientdetailer.Activity.MainDashboard;
import com.p1024.convenientdetailer.CustomViews.CustomEditText;
import com.p1024.convenientdetailer.CustomViews.CustomTextView;
import com.p1024.convenientdetailer.CustomViews.TransparentProgressDialog;
import com.p1024.convenientdetailer.R;
import com.p1024.convenientdetailer.Session.SessionClass;
import com.p1024.convenientdetailer.Session.SessionConstants;
import com.p1024.convenientdetailer.Utility.AvailableNetwork;
import com.p1024.convenientdetailer.Utility.ImageFilePath;
import com.p1024.convenientdetailer.Validation.Validation;
import com.soundcloud.android.crop.Crop;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by administrator on 2/16/18.
 */

public class Fragment_Customer_Registration extends Fragment implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {
    private View mParentView;
    private static final String TAG = Fragment_Customer_Registration.class.getSimpleName();
    private ImageView mImgAddProfile, mImgCustomerProfile, mImgFBLogin, mImgGooglePluslogin;
    private CustomTextView mTxtRegister;
    private CallbackManager callbackManagercustomer;
    private LoginManager loginManagercustomer = null;
    private GoogleApiClient mGoogleApiClientcustomer;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private static final int REQUEST_CODE_CAMERA_CAPTURE_IMAGE = 111;
    private static final int REQUEST_CODE_CHOOSE_IMAGE_FROM_LIBRARY = 222;
    private Bitmap bitmap;
    private Uri mCapturedImageUri;
    private String mStrwsRegistration="",mStrSocialRegistration="";
    private AQuery mAquery;
    private TransparentProgressDialog mTransparentdialogue;
    private CustomEditText mEdtuserFirstname,mEdtLastname,mEdtUsername, mEdtEmail, mEdtPassword, mEdtConfirmPassword, mEdtPhoneNumber;
    private CheckBox mChecktermscondition;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(getActivity());
        mParentView = inflater.inflate(R.layout.fragment_customer_registration, container, false);
        setUI();
        /**
         * ask permission for marshmallow devices
         */
        showPhotoDialogue();
        verifyStoragePermissions(getActivity());
        return mParentView;
    }

    /**
     * inflate All view's
     */
    private void setUI() {
        mAquery = new AQuery(getActivity());
        mEdtuserFirstname=mParentView.findViewById(R.id.edt_firstname);
        mEdtLastname=mParentView.findViewById(R.id.edt_lastname);
        mTransparentdialogue = new TransparentProgressDialog(getActivity(), R.drawable.ic_loader_image);
        mImgAddProfile = mParentView.findViewById(R.id.img_add_customerprofile);
        mImgCustomerProfile = mParentView.findViewById(R.id.img_customerprofpic);
        mImgFBLogin = mParentView.findViewById(R.id.img_c_fblogin);
        mImgGooglePluslogin = mParentView.findViewById(R.id.img_c_googleplus);
        mTxtRegister = mParentView.findViewById(R.id.txt_c_registration);
        mEdtUsername = mParentView.findViewById(R.id.edt_c_username);
        mEdtEmail = mParentView.findViewById(R.id.edt_c_email);
        mEdtPassword = mParentView.findViewById(R.id.edt_c_password);
        mEdtConfirmPassword = mParentView.findViewById(R.id.edt_c_confirmpass);
        mEdtPhoneNumber = mParentView.findViewById(R.id.edt_c_phone);
        mChecktermscondition=mParentView.findViewById(R.id.chktermscondition);
        callbackManagercustomer = CallbackManager.Factory.create();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClientcustomer = new GoogleApiClient.Builder(getActivity())
               /* .enableAutoManage(getActivity(), this)*/
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        setClickListener();
    }

    /**
     * set CLick Listener to view's
     */
    private void setClickListener() {
        mImgAddProfile.setOnClickListener(this);
        mImgFBLogin.setOnClickListener(this);
        mImgGooglePluslogin.setOnClickListener(this);
        mTxtRegister.setOnClickListener(this);
        mChecktermscondition.setOnClickListener(this);
    }

    /**
     * view's onClick Method
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_add_customerprofile:
                pickImage();
                break;
            case R.id.img_c_fblogin:
                regFacebook();
                break;
            case R.id.img_c_googleplus:
                if (!checkIsGoogleSignIn()) {
                    signIn();
                } else {
                    signOut();
                    signIn();
                }
                break;
            case R.id.txt_c_registration:
                if (checkValidation()) {
                    wsRegister();
                }

                break;
            case R.id.chktermscondition:
                 if(mChecktermscondition.isChecked())
                 {
                     mChecktermscondition.setButtonDrawable(R.drawable.ic_checked_icon);
                 }else {
                     mChecktermscondition.setButtonDrawable(R.drawable.ic_chekboxrectngle);
                 }
                break;
        }
    }

    private void wsRegister() {
        mStrwsRegistration = getActivity().getString(R.string.WS_HOST_URL) + getString(R.string.WS_CUSTOMER_REGISTRATION);
        HashMap<String, Object> params = new HashMap<>();
       // params.put("user_name", mEdtUsername.getText().toString());
        params.put("first_name", mEdtuserFirstname.getText().toString());
        params.put("last_name", mEdtLastname.getText().toString());
        params.put("user_email", mEdtEmail.getText().toString());
        params.put("user_mobile", mEdtPhoneNumber.getText().toString());
        params.put("password", mEdtPassword.getText().toString());
        params.put("profile_picture", getImageByteArray(((BitmapDrawable) mImgCustomerProfile.getDrawable()).getBitmap()));
        params.put("device",1);

        if (AvailableNetwork.isNetworkAvailable(getActivity())) {
            mAquery.progress(mTransparentdialogue).ajax(mStrwsRegistration, params, JSONObject.class, ajaxCallBack);
        } else {
            Toast.makeText(getActivity(), getString(R.string.str_network_error), Toast.LENGTH_SHORT).show();
        }
    }
    private void wsSocialLogin(String checkSocialStatus, String email, String socialID, String firstName, String lastName)
    {
        mStrSocialRegistration=getActivity().getString(R.string.WS_HOST_URL)+getActivity().getString(R.string.WS_SOCIAL_REGISTRATION);
        HashMap<String,Object>mParams=new HashMap<>();
        mParams.put("first_name",firstName);
        mParams.put("last_name",lastName);
        mParams.put("user_email",email);
        mParams.put("user_type","2");

        if (checkSocialStatus.equalsIgnoreCase("1"))
        {
            mParams.put("fb_id",socialID);
            mParams.put("google_id","");
        }else {
            mParams.put("google_id", socialID);
            mParams.put("fb_id","");
        }
        if (AvailableNetwork.isNetworkAvailable(getActivity()))
        {
            mAquery.progress(mTransparentdialogue).ajax(mStrSocialRegistration,mParams,JSONObject.class,ajaxCallBack);
        }else {
            Toast.makeText(getActivity(), ""+getActivity().getString(R.string.str_network_error), Toast.LENGTH_SHORT).show();
        }

    }
    AjaxCallback<JSONObject> ajaxCallBack = new AjaxCallback<JSONObject>() {
        @Override
        public void callback(String url, JSONObject object, AjaxStatus status) {
            super.callback(url, object, status);
            if (status.getCode() == 200) {
                if (object != null) {
                    try {
                        if (url.equalsIgnoreCase(mStrwsRegistration)) {
                            if (object.getString("error_code").equalsIgnoreCase("0")) {
                                showAlert("Registration done successfully.", 1);
                                /*SessionClass.getInstance().updateValue();
                                SessionClass.getInstance().updateValue();
                                SessionClass.getInstance().updateValue();
                                SessionClass.getInstance().updateValue();
                                SessionClass.getInstance().updateValue();*/

                            } else if (object.getString("error_code").equalsIgnoreCase("1")) {
                                Toast.makeText(getActivity(), "" + object.getString("msg"), Toast.LENGTH_SHORT).show();
                            } else if (object.getString("error_code").equalsIgnoreCase("2")) {
                                //showAlert(" We found your account is not yet verified. Kindly see the verification email, sent to your email address, used at the time of registration.", -1);
                                // Toast.makeText(getActivity(), " We found your account is not yet verified. Kindly see the verification email, sent to your email address, used at the time of registration.", Toast.LENGTH_SHORT).show();
                            }
                        }else if (url.equalsIgnoreCase(mStrSocialRegistration))
                        {
                            if (object.getString("error_code").equalsIgnoreCase("0")) {
                                JSONObject mJsonData = object.getJSONObject("data");
                                SessionClass.getInstance().updateValue(getActivity(), SessionConstants.KEY_USER_EMAIL, mJsonData.getString("email"));
                                JSONObject mJsonUserInfo = mJsonData.getJSONObject("user_information");
                                SessionClass.getInstance().updateValue(getActivity(), SessionConstants.KEY_USER_ID, mJsonUserInfo.getString("user_id"));
                                SessionClass.getInstance().updateValue(getActivity(), SessionConstants.KEY_USER_NAME, mJsonUserInfo.getString("user_name"));
                                SessionClass.getInstance().updateValue(getActivity(), SessionConstants.KEY_USER_TYPE, mJsonUserInfo.getString("user_type"));
                                SessionClass.getInstance().updateValue(getActivity(), SessionConstants.KEY_USER_MOBILE_NUMBER, mJsonUserInfo.getString("user_mobile"));
                                switchToDashboard();
                            }else {
                                showAlert(object.getString("msg"),-1);
                            }

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    showAlert(getString(R.string.str_server_error), -1);
                }
            }
        }
    };

    private void switchToDashboard() {
        Intent intent=new Intent(getActivity(),MainDashboard.class);
        startActivity(intent);
    }

    /**
     * call google plus screen to check login
     */
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClientcustomer);
        startActivityForResult(signInIntent, 8);
    }

    /**
     * call  OnActivityresult to handle responce
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 8) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        } else if (FacebookSdk.isFacebookRequestCode(requestCode)) {
            callbackManagercustomer.onActivityResult(requestCode, resultCode, data);

        } else if (requestCode == REQUEST_CODE_CAMERA_CAPTURE_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                String[] projection = {MediaStore.Images.Media.DATA};
                Cursor cursor = getActivity().managedQuery(mCapturedImageUri, projection, null, null, null);
                int column_index_data = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                String capturedImageFilePath = cursor.getString(column_index_data);
                Uri imageUri = Uri.parse("file:///" + capturedImageFilePath);
                String selectedImagePath;
                selectedImagePath = ImageFilePath.getPath(getActivity(), imageUri);
                String filename = selectedImagePath.substring(selectedImagePath.lastIndexOf("/") + 1);
                beginCrop(imageUri);
            }
        } else if (requestCode == REQUEST_CODE_CHOOSE_IMAGE_FROM_LIBRARY) {
            try {
                Uri imageUri = data.getData();
                InputStream is = getActivity().getContentResolver().openInputStream(imageUri);
                Bitmap bm = BitmapFactory.decodeStream(is);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.PNG, 90, stream);
                bitmap = bm;
                beginCrop(imageUri);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else if (requestCode == Crop.REQUEST_CROP) {
            handleCrop(resultCode, data);
        }

    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            Log.e(TAG, "display name: " + acct.getDisplayName());
            String personName = acct.getDisplayName();
            String[] Name=personName.split(" ");
          //  String personPhotoUrl = acct.getPhotoUrl().toString();
            String email = acct.getEmail();
            String personId = acct.getId();
            wsSocialLogin("0",email,personId,Name[0],Name[1]);
            Log.e(TAG, "Name: " + personName + ", email: " + email
                    + ", Image: ");
        } else {

        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onPause() {
        super.onPause();
       /* mGoogleApiClientcustomer.stopAutoManage(getActivity());
        mGoogleApiClientcustomer.disconnect();*/
    }

    private boolean checkIsGoogleSignIn() {
        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClientcustomer);
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
        Auth.GoogleSignInApi.signOut(mGoogleApiClientcustomer).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                    }
                });
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClientcustomer != null && mGoogleApiClientcustomer.isConnected()) {
            mGoogleApiClientcustomer.disconnect();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mGoogleApiClientcustomer != null) {
            mGoogleApiClientcustomer.connect();
        }
    }

    /**
     * Facebook Login Method
     */
    private void regFacebook() {
        callbackManagercustomer = CallbackManager.Factory.create();
        loginManagercustomer = LoginManager.getInstance();
        if (loginManagercustomer == null) {
            LoginManager.getInstance().logOut();
        }
        loginManagercustomer.logInWithReadPermissions(getActivity(), Arrays.asList("public_profile", "user_friends", "email"));
        List<String> permissions = new ArrayList<>();
        permissions.add("email");
        LoginManager.getInstance().registerCallback(callbackManagercustomer, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {

                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {
                            Log.e("*Facebook", "Facebook responce-->" + response.toString());
                            Toast.makeText(getActivity(), "" + object.getString("email") + " " + object.getString("id"), Toast.LENGTH_SHORT).show();
                            String[] mStrName=object.getString("name").split(" ");
                            wsSocialLogin("1",object.getString("email"),object.getString("id"),mStrName[0],mStrName[1]);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
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



    /**
     * Access permission to upload photo marshmellow
     */
    public void showPhotoDialogue() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ArrayList<String> listPermission = new ArrayList<>();
            if (getActivity().checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                listPermission.add(Manifest.permission.CAMERA);
            }
            if (getActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                listPermission.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
            if (getActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                listPermission.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
            if (listPermission.size() > 0) {
                String[] permArray = listPermission.toArray(new String[listPermission.size()]);
                requestPermissions(permArray, 2909);
            }
        }
    }

    /**
     * Convert bitmap to the byte array.
     */
    private byte[] getImageByteArray(Bitmap bmp) {
        byte[] byteArray = null;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        if (bmp != null)
            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byteArray = stream.toByteArray();
        return byteArray;
    }

    /**
     * Select image dialog
     */
    private void pickImage() {
        final CharSequence[] items = {"Take Photo", "Choose From Library", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    setImagePath();
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageUri);
                    startActivityForResult(intent, REQUEST_CODE_CAMERA_CAPTURE_IMAGE);
                } else if (items[item].equals("Choose From Library")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(Intent.createChooser(intent, "Select File"), REQUEST_CODE_CHOOSE_IMAGE_FROM_LIBRARY);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void setImagePath() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, System.currentTimeMillis() + ".png");
        mCapturedImageUri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
    }

    private void beginCrop(Uri source) {
        Uri destination = Uri.fromFile(new File(getActivity().getCacheDir(), "cropped"));
        Crop.of(source, destination).withMaxSize(400, 400).start(getActivity());
    }

    private void handleCrop(int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            try {
                Bitmap tempBitMap = getCorrectlyOrientedImage(Crop.getOutput(data), 500);
                bitmap = tempBitMap.copy(tempBitMap.getConfig(), true);
                tempBitMap.recycle();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (bitmap != null) {
                mImgCustomerProfile.setImageBitmap(bitmap);
            } else {

            }
            //   mImgProp.setImageResource(R.drawable.imag_not_availbl);
        } else if (resultCode == Crop.RESULT_ERROR) {
        }
    }

    public Bitmap getCorrectlyOrientedImage(Uri photoUri, int MAX_IMAGE_DIMENSION) throws IOException {
        InputStream is = getActivity().getContentResolver().openInputStream(photoUri);
        BitmapFactory.Options dbo = new BitmapFactory.Options();
        dbo.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(is, null, dbo);
        is.close();

        int rotatedWidth, rotatedHeight;

        int orientation = getImageRotation(photoUri);
        if (orientation == 90 || orientation == 270) {
            rotatedWidth = dbo.outHeight;
            rotatedHeight = dbo.outWidth;
        } else {
            rotatedWidth = dbo.outWidth;
            rotatedHeight = dbo.outHeight;
        }

        Bitmap srcBitmap;
        is = getActivity().getContentResolver().openInputStream(photoUri);
        if (rotatedWidth > MAX_IMAGE_DIMENSION || rotatedHeight > MAX_IMAGE_DIMENSION) {
            float widthRatio = ((float) rotatedWidth) / ((float) MAX_IMAGE_DIMENSION);
            float heightRatio = ((float) rotatedHeight) / ((float) MAX_IMAGE_DIMENSION);
            float maxRatio = Math.max(widthRatio, heightRatio);

            // Create the bitmap from file
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = (int) maxRatio;
            srcBitmap = BitmapFactory.decodeStream(is, null, options);
        } else {
            srcBitmap = BitmapFactory.decodeStream(is);
        }
        is.close();

        /*
        * if the orientation is not 0 (or -1, which means we don't know), we
        * have to do a rotation.
        */
        if (orientation > 0) {
            Matrix matrix = new Matrix();
            matrix.postRotate(orientation);

            srcBitmap = Bitmap.createBitmap(srcBitmap, 0, 0, srcBitmap.getWidth(),
                    srcBitmap.getHeight(), matrix, true);
        }

        return srcBitmap;
    }

    public int getImageRotation(Uri imageUri) {
        try {
            ExifInterface exif = new ExifInterface(imageUri.getPath());
            int rotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);

            if (rotation == ExifInterface.ORIENTATION_UNDEFINED)
                return getRotationFromMediaStore(getActivity(), imageUri);
            else return exifToDegrees(rotation);
        } catch (IOException e) {
            return 0;
        }
    }

    private int exifToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        } else {
            return 0;
        }
    }

    public int getRotationFromMediaStore(Context context, Uri imageUri) {
        String[] columns = {MediaStore.Images.Media.DATA, MediaStore.Images.Media.ORIENTATION};
        Cursor cursor = context.getContentResolver().query(imageUri, columns, null, null, null);
        if (cursor == null) return 0;

        cursor.moveToFirst();

        int orientationColumnIndex = cursor.getColumnIndex(columns[1]);
        return cursor.getInt(orientationColumnIndex);
    }

    /**
     * On Screen permission for android 6.0 and above
     *
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have read or write permission
        int writePermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int readPermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);

        if (writePermission != PackageManager.PERMISSION_GRANTED || readPermission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    private boolean checkValidation() {
        if (mEdtuserFirstname.isEmpty()) {
            mEdtuserFirstname.setError("Please enter First Name.");
            return false;
        }else if (mEdtLastname.isEmpty()) {
            mEdtLastname.setError("Please enter Last Name.");
            return false;
        } else if (mEdtEmail.isEmpty()) {
            mEdtEmail.setError("Please enter email.");
            return false;
        } else if (!Validation.checkEmail(mEdtEmail.getString().trim())) {
            mEdtEmail.setError("Please enter valid email.");
            return false;
        } else if (mEdtPhoneNumber.getText().toString().trim().length() < 5) {
            mEdtPhoneNumber.setError("Phone number can't be less than 5 digit.");
            return false;
        } else if (mEdtPassword.isEmpty()) {
            mEdtPassword.setError("Please enter password.");
            return false;
        } else if (mEdtPassword.getString().length() < 8) {
            mEdtPassword.setError("Please enter atleast 8 characters.");
            return false;
        } else if (!mEdtPassword.getText().toString().trim().equalsIgnoreCase(mEdtConfirmPassword.getText().toString().trim())) {
            mEdtConfirmPassword.setError("Password does not match.");
            return false;
        }else if (!mChecktermscondition.isChecked())
        {
            showAlert("Please check terms and Conditions.",-1);
            return false;
        }
        return true;
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
        Fragment fragment;
        Bundle bundleF = new Bundle();

        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if (flg == 1) {
            fragment = new LoginFragment();
            fragment.setArguments(bundleF);
            ft.replace(R.id.content_frame, fragment, "Login").addToBackStack("").commit();
        }
    }

}
