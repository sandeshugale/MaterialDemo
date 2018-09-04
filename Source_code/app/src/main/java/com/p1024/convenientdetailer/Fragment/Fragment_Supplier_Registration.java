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
import android.location.Address;
import android.location.Geocoder;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
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
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.p1024.convenientdetailer.Activity.MainDashboard;
import com.p1024.convenientdetailer.Adapter.PlaceArrayAdapter;
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
import java.lang.reflect.MalformedParameterizedTypeException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by administrator on 2/16/18.
 */

public class Fragment_Supplier_Registration extends Fragment implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {
    private static final String TAG = Fragment_Supplier_Registration.class.getSimpleName();
    private View mParentView;
    private ImageView mImgFBLogin, mImgGoogleLogin, mImgAddProfileImage, mImgProfilePic;
    private CallbackManager callbackManagercustomer;
    private LoginManager loginManagercustomer = null;
    private GoogleApiClient mGoogleApiClientcustomer;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private static final int REQUEST_CODE_CAMERA_CAPTURE_IMAGE = 111;
    private static final int REQUEST_CODE_CHOOSE_IMAGE_FROM_LIBRARY = 223;
    private Bitmap bitmap;
    private Uri mCapturedImageUri;
    private AutoCompleteTextView mEdtSuburbRegistration;
    private GoogleApiClient mGoogleApiClientplaces;
    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(37.398160, -122.180831), new LatLng(37.430610, -121.972090));
    private PlaceArrayAdapter mPlaceArrayAdapter;

    private CustomEditText mEdtCompanyname, mEdtWebsiteLink, mEdtEmail, mEdtPassword, mEdtConfirmPassword, mEdtFacebookLink, mEdtInstalink, mEdtPhoneNumber;
    private CustomTextView mTxtRegister;
    private String mStrwsSupplierURl, mStrSocialRegistration;
    private AQuery mAquery;
    private TransparentProgressDialog mProgressdlg;
    String lattitude, lagnitude;
    private CheckBox mChecktermscondition;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(getActivity());
        mParentView = inflater.inflate(R.layout.fragment_supplier_registration, container, false);
        setUI();
        showPhotoDialogue();
        verifyStoragePermissions(getActivity());
        return mParentView;
    }

    private void setUI() {
        mAquery = new AQuery(getActivity());
        mChecktermscondition=mParentView.findViewById(R.id.chktermscondition);

        mImgFBLogin = mParentView.findViewById(R.id.img_supplierfblogin);
        mImgGoogleLogin = mParentView.findViewById(R.id.img_suppliergoogleplus);
        callbackManagercustomer = CallbackManager.Factory.create();
        mImgProfilePic = mParentView.findViewById(R.id.img_supplierprofpic);
        mImgAddProfileImage = mParentView.findViewById(R.id.img_addsupplier_profile);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClientcustomer = new GoogleApiClient.Builder(getActivity())
               /* .enableAutoManage(getActivity(), this)*/
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        /**
         * inflate All Input Views
         */
        mEdtCompanyname = mParentView.findViewById(R.id.edt_s_companyname);
        mEdtWebsiteLink = mParentView.findViewById(R.id.edt_s_website);
        mEdtEmail = mParentView.findViewById(R.id.edt_s_email);
        mEdtPassword = mParentView.findViewById(R.id.edt_s_password);
        mEdtConfirmPassword = mParentView.findViewById(R.id.edt_s_confmpassword);
        mEdtFacebookLink = mParentView.findViewById(R.id.edt_s_fblink);
        mEdtInstalink = mParentView.findViewById(R.id.edt_s_instalink);
        mEdtPhoneNumber = mParentView.findViewById(R.id.edt_s_contactno);
        mTxtRegister = mParentView.findViewById(R.id.txt_s_register);
        mProgressdlg = new TransparentProgressDialog(getActivity(), R.drawable.ic_loader_image);
        /**
         * Autocomplete google places
         */
        mEdtSuburbRegistration = (AutoCompleteTextView) mParentView.findViewById(R.id.edt_s_autoaddress);
        mGoogleApiClientplaces = new GoogleApiClient.Builder(getActivity())
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(getActivity(), (int) Math.random() * 1000, this)
                .addConnectionCallbacks(this)
                .build();
        mEdtSuburbRegistration.setThreshold(3);
        // Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), getString(R.string.font_lato_regular));
        //  mEdtSuburbRegistration.setTypeface(tf);
        mEdtSuburbRegistration.setOnItemClickListener(mAutocompleteClickListener);
        // AutocompleteFilter filter = new AutocompleteFilter.Builder().setCountry("AU").build();
        mPlaceArrayAdapter = new PlaceArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, BOUNDS_MOUNTAIN_VIEW, null);
        mPlaceArrayAdapter.setGoogleApiClient(mGoogleApiClientplaces);
        mEdtSuburbRegistration.setAdapter(mPlaceArrayAdapter);
        setListener();
    }

    private void setListener() {
        mImgGoogleLogin.setOnClickListener(this);
        mImgFBLogin.setOnClickListener(this);
        mImgAddProfileImage.setOnClickListener(this);
        mTxtRegister.setOnClickListener(this);
        mChecktermscondition.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_supplierfblogin:
                regFacebook();
                break;
            case R.id.img_suppliergoogleplus:
                if (!checkIsGoogleSignIn()) {
                    signIn();
                } else {
                    signOut();
                    signIn();
                }
                break;
            case R.id.img_addsupplier_profile:
                pickImage();
                break;
            case R.id.txt_s_register:
                if (checkValidation()) {
                    wsSupplierRegistration();
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

    private void wsSocialLogin(String checkSocialStatus, String email, String socialID, String firstName, String lastName) {
        mStrSocialRegistration = getActivity().getString(R.string.WS_HOST_URL) + getActivity().getString(R.string.WS_SOCIAL_REGISTRATION);
        HashMap<String, Object> mParams = new HashMap<>();
        mParams.put("first_name", firstName);
        mParams.put("last_name", lastName);
        mParams.put("user_email", email);
        mParams.put("user_type", "4");

        if (checkSocialStatus.equalsIgnoreCase("1")) {
            mParams.put("fb_id", socialID);
            mParams.put("google_id", "");
        } else {
            mParams.put("google_id", socialID);
            mParams.put("fb_id", "");
        }
        if (AvailableNetwork.isNetworkAvailable(getActivity())) {
            mAquery.progress(mProgressdlg).ajax(mStrSocialRegistration, mParams, JSONObject.class, ServerResponce);
        } else {
            Toast.makeText(getActivity(), "" + getActivity().getString(R.string.str_network_error), Toast.LENGTH_SHORT).show();
        }

    }

    private void wsSupplierRegistration() {
        mStrwsSupplierURl = getActivity().getString(R.string.WS_HOST_URL) + getActivity().getString(R.string.WS_SUPPLIER_REGISTRATION);
        HashMap<String, Object> mParams = new HashMap<>();
        mParams.put("company_name", mEdtCompanyname.getText().toString());
        mParams.put("website_link", mEdtWebsiteLink.getText().toString());
        mParams.put("user_email", mEdtEmail.getText().toString());
        mParams.put("phone_number", mEdtPhoneNumber.getText().toString());
        mParams.put("facebook_connection_link", mEdtFacebookLink.getText().toString());
        mParams.put("instagram_connection_link", mEdtInstalink.getText().toString());
        mParams.put("password", mEdtPassword.getText().toString());
        mParams.put("user_address",mEdtSuburbRegistration.getText().toString());
        /*mParams.put("latitude",lattitude);
        mParams.put("longitude",lagnitude);*/
        mParams.put("device",1);

        mParams.put("profile_picture", getImageByteArray(((BitmapDrawable) mImgProfilePic.getDrawable()).getBitmap()));
        if (AvailableNetwork.isNetworkAvailable(getActivity())) {
            mAquery.progress(mProgressdlg).ajax(mStrwsSupplierURl, mParams, JSONObject.class, ServerResponce);
        } else {
            Toast.makeText(getActivity(), "" + getString(R.string.str_network_error), Toast.LENGTH_SHORT).show();
        }


    }

    /**
     * Server(webservice responce_)
     */
    AjaxCallback<JSONObject> ServerResponce = new AjaxCallback<JSONObject>() {
        @Override
        public void callback(String url, JSONObject object, AjaxStatus status) {
            super.callback(url, object, status);
            Log.d("URL-->>", "" + url);
            Log.d("Responce-->", "" + object);
            if (object != null) {
                try {
                    if (url.equalsIgnoreCase(mStrwsSupplierURl)) {
                        if (object.getString("error_code").equalsIgnoreCase("0")) {
                            showAlert(object.getString("msg"), 1);
                        } else {
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
                           /* Intent intent=new Intent(getActivity(), MainDashboard.class);
                            startActivity(intent);*/
                            //  showAlert(object.getString("msg"), 1);

                        }else {
                            showAlert(object.getString("msg"),-1);
                        }
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else {
                Toast.makeText(getActivity(), "" + getString(R.string.str_server_error), Toast.LENGTH_SHORT).show();
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
            String[] Name = personName.split(" ");
          //  String personPhotoUrl = acct.getPhotoUrl().toString();
            String email = acct.getEmail();
            String personId = acct.getId();
            wsSocialLogin("0", email, personId, Name[0], Name[1]);
            Log.e(TAG, "Name: " + personName + ", email: " + email
                    + ", Image: ");
        } else {

        }
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
        if (mGoogleApiClientplaces != null && mGoogleApiClientplaces.isConnected()) {
            mGoogleApiClientplaces.stopAutoManage(getActivity());
            mGoogleApiClientplaces.disconnect();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mGoogleApiClientcustomer != null) {
            mGoogleApiClientcustomer.connect();
        }
        if (mGoogleApiClientplaces != null)
            mGoogleApiClientplaces.connect();
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
                            //  Toast.makeText(getActivity(), "" + object.getString("email") + " " + object.getString("id"), Toast.LENGTH_SHORT).show();
                            String[] mStrName = object.getString("name").split(" ");
                            wsSocialLogin("1", object.getString("email"), object.getString("id"), mStrName[0], mStrName[1]);


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
                mImgProfilePic.setImageBitmap(bitmap);
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

    private AdapterView.OnItemClickListener mAutocompleteClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final PlaceArrayAdapter.PlaceAutocomplete item = mPlaceArrayAdapter.getItem(position);
            final String placeId = String.valueOf(item.placeId);
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi.getPlaceById(mGoogleApiClientplaces, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
        }
    };

    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                return;
            }
            final Place place = places.get(0);
            CharSequence attributions = places.getAttributions();
            Geocoder geocoder = new Geocoder(getActivity(), Locale.ENGLISH);
            LatLng mLatLng = place.getLatLng();
            List<Address> address = null;
            try {
                mEdtSuburbRegistration.clearFocus();
                address = geocoder.getFromLocation(mLatLng.latitude, mLatLng.longitude, 1);
                lattitude = String.valueOf(mLatLng.latitude);
                lagnitude = String.valueOf(mLatLng.longitude);
              //Toast.makeText(getActivity(), "" + lattitude + "   " + lagnitude, Toast.LENGTH_SHORT).show();
                Address add = address.get(0);
                String city = add.getLocality();
                String mStr_Post_Code = add.getPostalCode();
                String addressLine = add.getAddressLine(0);
                String mStrAdminArea = add.getAdminArea();
                //   mEdtPostCodeRegistration.setText(mStr_Post_Code);
                //  mEdtSuburbRegistration.setText(Html.fromHtml(attributions.toString()));
               /* if (mStrAdminArea.equalsIgnoreCase("New South Wales")) {
                    mEdtSuburbRegistration.setText("NSW");
                } else if (mStrAdminArea.equalsIgnoreCase("Tasmania")) {
                    mEdtSuburbRegistration.setText("TAS");
                } else if (mStrAdminArea.equalsIgnoreCase("Queensland")) {
                    mEdtSuburbRegistration.setText("QLD");
                } else if (mStrAdminArea.equalsIgnoreCase("South Australia")) {
                    mEdtSuburbRegistration.setText("SA");
                } else if (mStrAdminArea.equalsIgnoreCase("Western Australia")) {
                    mEdtSuburbRegistration.setText("WA");
                } else if (mStrAdminArea.equalsIgnoreCase("Victoria")) {
                    mEdtSuburbRegistration.setText("VIC");
                } else if (mStrAdminArea.equalsIgnoreCase("Australian Capital Territory")) {
                    mEdtSuburbRegistration.setText("ACT");
                } else {
                    mEdtSuburbRegistration.setText("");
                }*/
                if (attributions != null) {
                    mEdtSuburbRegistration.setText(Html.fromHtml(attributions.toString()));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    };

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        mPlaceArrayAdapter.setGoogleApiClient(null);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mPlaceArrayAdapter.setGoogleApiClient(mGoogleApiClientplaces);
    }

    @Override
    public void onConnectionSuspended(int i) {

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
        Fragment fragment;
        Bundle bundleF = new Bundle();

        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if (flag == 1) {
            fragment = new LoginFragment();
            fragment.setArguments(bundleF);
            ft.replace(R.id.content_frame, fragment, "Login").addToBackStack("").commit();
        }
    }

    private boolean checkValidation() {
        if (mEdtCompanyname.isEmpty()) {
            mEdtCompanyname.setError("Please enter Company Name.");
            return false;
        } else if (mEdtWebsiteLink.isEmpty()) {
            mEdtWebsiteLink.setError("Please enter Website Link.");
            return false;
        }else if (!Validation.chekWebSiteUrl(mEdtWebsiteLink.getText().toString().trim())) {
            mEdtWebsiteLink.setError("Please enter valid Web url.(example: "+"www.xyz.com"+") ");
            return false;
        }/*else if (!android.util.Patterns.WEB_URL.matcher(mEdtWebsiteLink.getText().toString().trim()).matches()) {
            mEdtWebsiteLink.setError("Please enter valid Web url.");
            return false;
        }*/ else if (mEdtEmail.isEmpty()) {
            mEdtEmail.setError("Please enter email.");
            return false;
        } else if (!Validation.checkEmail(mEdtEmail.getString().trim())) {
            mEdtEmail.setError("Please enter valid email.");
            return false;
        } else if (mEdtPassword.isEmpty()) {
            mEdtPassword.setError("Please enter password.");
            return false;
        } else if (mEdtPassword.getString().length() < 8) {
            mEdtPassword.setError("Please enter atleast 8 characters password.");
            return false;
        } else if (!mEdtPassword.getText().toString().trim().equalsIgnoreCase(mEdtConfirmPassword.getText().toString().trim())) {
            mEdtConfirmPassword.setError("Password does not match.");
            return false;
        } else if (mEdtPhoneNumber.getText().toString().trim().length() < 5) {
            mEdtPhoneNumber.setError("Phone Number can't be less than 5 digit.");
            return false;
        }else if (mEdtFacebookLink.isEmpty()) {
            mEdtFacebookLink.setError("Please enter your Facebook profile link.");
            return false;
        }else if (!Validation.checkfacebookprofileurl(mEdtFacebookLink.getString().trim())) {
            mEdtFacebookLink.setError("Please enter Valid Facebook profile link.");
            return false;
        }/*else if (mEdtFacebooklink.getText().toString().matches(regex)) {
            mEdtFacebooklink.setError("Please enter valid Facebook profile link.");
            return false;
        }*/ else if (mEdtInstalink.isEmpty()) {
            mEdtInstalink.setError("Please enter your Instagram profile link.");
            return false;
        }else if (!Validation.checkinstagramprofile(mEdtInstalink.getString().trim())) {
            mEdtInstalink.setError("Please enter Valid Instagram profile link.");
            return false;
        }else if (!mChecktermscondition.isChecked())
        {
            showAlert("Please check terms and Conditions.",-1);
            return false;
        }
        return true;
    }
}
