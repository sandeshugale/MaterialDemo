package com.p1024.convenientdetailer.Fragment;


import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
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
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TimePicker;
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
import com.p1024.convenientdetailer.Adapter.ServiceExpandableHeigthAdapter;
import com.p1024.convenientdetailer.Adapter.SpinnerAdapter;
import com.p1024.convenientdetailer.CustomViews.CustomEditText;
import com.p1024.convenientdetailer.CustomViews.CustomTextView;
import com.p1024.convenientdetailer.CustomViews.ExpandableHeightListView;
import com.p1024.convenientdetailer.CustomViews.TransparentProgressDialog;
import com.p1024.convenientdetailer.DataClass.DetailerClass;
import com.p1024.convenientdetailer.DataClass.ServiceListDataClass;
import com.p1024.convenientdetailer.R;
import com.p1024.convenientdetailer.Session.SessionClass;
import com.p1024.convenientdetailer.Session.SessionConstants;
import com.p1024.convenientdetailer.Utility.AvailableNetwork;
import com.p1024.convenientdetailer.Utility.ImageFilePath;
import com.p1024.convenientdetailer.Validation.Validation;
import com.soundcloud.android.crop.Crop;
import com.tangxiaolv.telegramgallery.GalleryActivity;
import com.tangxiaolv.telegramgallery.GalleryConfig;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by administrator on 2/13/18.
 */

public class FragmentAutodetailer_registration extends Fragment implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {
    private View mParentView;
    private TextInputEditText mEditInputOpeningTime, mEditInputClosingtime;
    private String mFlagsetdata;
    private LinearLayout mLLMainLayout;
    private ImageView mImgAddImage, mImgUploadVideo, mImgAddServcies, mImgAddprofpic, mImgProfilePic, mImgFbLogin, mImgGoogleplusLogin;
    private CallbackManager callbackManagerDetailer;
    private LoginManager loginManagercustomer = null;
    private GoogleApiClient mGoogleApiClientdetailer;
    private static final String TAG = FragmentAutodetailer_registration.class.getSimpleName();
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private static final int REQUEST_CODE_CAMERA_CAPTURE_IMAGE = 11;
    private static final int REQUEST_CODE_CHOOSE_IMAGE_FROM_LIBRARY = 22;
    private static final int REQUEST_CODE_FOR_PICK_FILE = 33;
    String mStrSelectedFileExtension = "";
    String mStrSelectedFileExtention;
    private byte[] mAttachment = null;
    private Bitmap bitmap;
    private Uri mCapturedImageUri;
    String filename = "";
    String mStrProfImgFlag;
    RadioButton rb;
    private List<HelperWorkImage> mLstHelperWorkImages;
    private CustomTextView mTxtVideoname;
    private AutoCompleteTextView mAutoTxtServices;
    private CustomEditText mEdtPrices;
    private ExpandableHeightListView mListviewServices;
    private ArrayList<ServiceListDataClass> mArrayService;
    private CustomEditText mEdtCmpnyname, mEdtWebsite, mEdtMobileNumber, mEdtEmail, mEdtPassword, mEdtFacebooklink, mEdtInstagramLink, mEdtAboutme, mEdtContactinfo, mEdtAddress, mEdtConfirmPasword;
    private CustomTextView mTxtRegister;
    private String mStrWsdetailerURL = "";
    private CustomTextView mTxtAddService;
    private String mStrGetCategorURL;
    private AQuery mAquery;
    private TransparentProgressDialog mProgressDlg;
    private ArrayList<DetailerClass> mArraylistServiceCat;
    private ArrayList mArrayServiceCatgory;
    private String mStrCatID;
    private String mStrSocialRegistration;
    private AutoCompleteTextView mEdtSuburbRegistration;
    private GoogleApiClient mGoogleApiClientplaces;
    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(37.398160, -122.180831), new LatLng(37.430610, -121.972090));
    private PlaceArrayAdapter mPlaceArrayAdapter;
    private CheckBox mChecktermscondition;
    private List<String> mListWorkImagesLocalPath;
    private RadioGroup mparentradio;
    private RadioButton mRadioAddress, mRadioMobile;
    LinearLayout mLayoutAddress;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(getActivity());
        mParentView = inflater.inflate(R.layout.fragment_detailer_registration, container, false);
        setUI();
        /**
         * ask permission for marshmallow devices
         */
        showPhotoDialogue();
        verifyStoragePermissions(getActivity());
        return mParentView;
    }

    private void setUI() {
        mAquery = new AQuery(getActivity());
        mLayoutAddress = mParentView.findViewById(R.id.addresslayout);
        mProgressDlg = new TransparentProgressDialog(getActivity(), R.drawable.ic_loader_image);
        mEditInputOpeningTime = mParentView.findViewById(R.id.openingtime);
        mEditInputClosingtime = mParentView.findViewById(R.id.closingtime);
        mLLMainLayout = mParentView.findViewById(R.id.mainlayout);
        mImgAddImage = mParentView.findViewById(R.id.img_addimage);
        mImgUploadVideo = mParentView.findViewById(R.id.imguploadvideo);
        mTxtVideoname = mParentView.findViewById(R.id.novideoselected);
        mImgAddServcies = mParentView.findViewById(R.id.imgaddservices);
        mChecktermscondition = mParentView.findViewById(R.id.chktermscondition);
        mAutoTxtServices = mParentView.findViewById(R.id.autocmpltservices);
        mEdtPrices = mParentView.findViewById(R.id.edtprices);
        mListviewServices = mParentView.findViewById(R.id.serviceslist);
        mImgAddprofpic = mParentView.findViewById(R.id.img_add_profile);
        mImgProfilePic = mParentView.findViewById(R.id.img_profpic);
        mImgFbLogin = mParentView.findViewById(R.id.imgdetailerfblogin);
        mEdtConfirmPasword = mParentView.findViewById(R.id.edt_d_confirmpassword);
        mImgGoogleplusLogin = mParentView.findViewById(R.id.imgdetailergoogleplus);
        callbackManagerDetailer = CallbackManager.Factory.create();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClientdetailer = new GoogleApiClient.Builder(getActivity())
                /*.enableAutoManage(getActivity(), this)*/
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        mArrayService = new ArrayList<>();
        mListviewServices.setExpanded(true);
        mLstHelperWorkImages = new ArrayList();
        /**
         * Inflate All edittext input
         */
        mEdtCmpnyname = mParentView.findViewById(R.id.edt_d_cmpnyname);
        mEdtWebsite = mParentView.findViewById(R.id.edt_d_website);
        mEdtEmail = mParentView.findViewById(R.id.edt_d_email);
        mEdtMobileNumber = mParentView.findViewById(R.id.edt_d_phonenumber);
        mEdtPassword = mParentView.findViewById(R.id.edt_d_password);
        mEdtFacebooklink = mParentView.findViewById(R.id.edt_d_fb);
        mEdtInstagramLink = mParentView.findViewById(R.id.edt_d_instalink);
        mEdtContactinfo = mParentView.findViewById(R.id.edt_d_contactinfo);

        /**
         * radio button
         */
        mparentradio = (RadioGroup) mParentView.findViewById(R.id.radioGroup);
        mRadioAddress = (RadioButton) mParentView.findViewById(R.id.rdaddress);
        mRadioMobile = (RadioButton) mParentView.findViewById(R.id.rdmobile);
        mRadioAddress.setChecked(true);
        mparentradio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
                rb = (RadioButton) mParentView.findViewById(checkedId);
                /*textViewChoice.setText("You Selected " );*/
                if (rb.getText().toString().equalsIgnoreCase("Address")) {
                    mLayoutAddress.setVisibility(View.VISIBLE);
                } else {
                    mLayoutAddress.setVisibility(View.GONE);
                }
                //     Toast.makeText(getActivity(), ""+ rb.getText(), Toast.LENGTH_SHORT).show();
                //Toast.makeText(getApplicationContext(), rb.getText(), Toast.LENGTH_SHORT).show();
            }
        });
        //     mEdtAddress = mParentView.findViewById(R.id.edt_d_address);
        mEdtAboutme = mParentView.findViewById(R.id.edt_d_aboutme);
        mTxtRegister = mParentView.findViewById(R.id.txt_d_register);
        mTxtAddService = mParentView.findViewById(R.id.txtaddservice);
        mListWorkImagesLocalPath = new ArrayList<>();
        /**
         * Autocomplete google places
         */
        mEdtSuburbRegistration = (AutoCompleteTextView) mParentView.findViewById(R.id.edt_d_address);
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

    private AdapterView.OnItemClickListener mAutocompleteClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final PlaceArrayAdapter.PlaceAutocomplete item = mPlaceArrayAdapter.getItem(position);
            final String placeId = String.valueOf(item.placeId);
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi.getPlaceById(mGoogleApiClientplaces, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
        }
    };

    private String lagnitude, lattitude;
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
                //  Toast.makeText(getActivity(), "" + lattitude + "   " + lagnitude, Toast.LENGTH_SHORT).show();
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

    private void setListener() {
        mEditInputOpeningTime.setOnClickListener(this);
        mEditInputClosingtime.setOnClickListener(this);
        mImgUploadVideo.setOnClickListener(this);
        mImgAddImage.setOnClickListener(this);
        mImgAddServcies.setOnClickListener(this);
        mImgAddprofpic.setOnClickListener(this);
        mImgFbLogin.setOnClickListener(this);
        mImgGoogleplusLogin.setOnClickListener(this);
        mTxtRegister.setOnClickListener(this);
        mTxtAddService.setOnClickListener(this);
        mChecktermscondition.setOnClickListener(this);

        setAdapter();
    }

    ArrayList<String> mArrayServices;

    private void setAdapter() {
        mArrayServices = new ArrayList<>();
        mArrayServices.add("car wash");
        mArrayServices.add("washing");
        mArrayServices.add("car parts");
        ArrayAdapter adapter = new ArrayAdapter
                (getActivity(), android.R.layout.select_dialog_item, mArrayServices);
        mAutoTxtServices.setThreshold(1);//will start working from first character
        mAutoTxtServices.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.openingtime:
                mFlagsetdata = "opening";
                openTimePickerDialogue();
                break;
            case R.id.closingtime:
                mFlagsetdata = "closing";
                openTimePickerDialogue();
                break;
            case R.id.img_addimage:
                mStrProfImgFlag = "addworkimg";
                pickImage();
                break;
            case R.id.imguploadvideo:
                pickVideo();
                break;
            case R.id.imgaddservices:
                ServiceListDataClass serviceListDataClass = new ServiceListDataClass();
                serviceListDataClass.setmStrServicename(mAutoTxtServices.getText().toString());
                serviceListDataClass.setmStrPrice(mEdtPrices.getText().toString());
                mArrayService.add(serviceListDataClass);
                setServiceAdapter();
                break;
            case R.id.img_add_profile:
                mStrProfImgFlag = "profileimg";
                pickImage();
                break;
            case R.id.imgdetailerfblogin:
                regFacebook();
                break;
            case R.id.imgdetailergoogleplus:
                if (!checkIsGoogleSignIn()) {
                    signIn();
                } else {
                    signOut();
                    signIn();
                }
                break;
            case R.id.txt_d_register:
                convertIntoJSON();
                if (checkValidation()) {
                    if (serviceArray.length() == 0) {
                        showAlert("Please select atleast one service.", -1);
                    }else if (!mChecktermscondition.isChecked()) {
                        showAlert("Please check terms and Conditions.", -1);
                    }else {
                       wsDetailerRegistration();

                    }
                }
                break;
            case R.id.txtaddservice:
                openAddServiceDlg();
                break;
            case R.id.chktermscondition:
                if (mChecktermscondition.isChecked()) {
                    mChecktermscondition.setButtonDrawable(R.drawable.ic_checked_icon);
                } else {
                    mChecktermscondition.setButtonDrawable(R.drawable.ic_chekboxrectngle);
                }
                break;

        }
    }

    JSONArray serviceArray;

    private void convertIntoJSON() {
        try {
            serviceArray = new JSONArray();
            for (int i = 0; i < mArrayService.size(); i++) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("cat_id", mArrayService.get(i).getmStrCatID());
                jsonObject.put("service_name", mArrayService.get(i).getmStrServicename());
                jsonObject.put("service_price", mArrayService.get(i).getmStrPrice());
                jsonObject.put("service_description", mArrayService.get(i).getmStrServiceDesc());
                serviceArray.put(jsonObject);
            }
            Log.d("jsonArray", "" + serviceArray);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    Dialog mDialogservice;
    CustomEditText mEdtServiceName, mEdtServicePrice, mEdtServiceDesc;
    CustomTextView mTxtSave;
    Spinner mSp_Category;

    private void openAddServiceDlg() {
        mDialogservice = new Dialog(getActivity());
        mDialogservice.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialogservice.setContentView(R.layout.dialogue_layout_service);
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        mDialogservice.getWindow().setLayout((6 * width) / 6, LinearLayout.LayoutParams.WRAP_CONTENT);
        mDialogservice.setCancelable(true);
        mEdtServiceName = (CustomEditText) mDialogservice.findViewById(R.id.txt_servicename);
        mEdtServicePrice = (CustomEditText) mDialogservice.findViewById(R.id.txt_serviceprice);
        mEdtServiceDesc = (CustomEditText) mDialogservice.findViewById(R.id.txt_servicedesc);
        mSp_Category = (Spinner) mDialogservice.findViewById(R.id.sp_addservice);
        ImageView mImv_cancle = mDialogservice.findViewById(R.id.imgdialogueclose);
        mTxtSave = mDialogservice.findViewById(R.id.txtsave);
        mArraylistServiceCat = new ArrayList<>();
        wsGetServiceCategory();
        mTxtSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkserviceValidation()) {
                    ServiceListDataClass serviceListDataClass = new ServiceListDataClass();
                    serviceListDataClass.setmStrServicename(mEdtServiceName.getText().toString());
                    serviceListDataClass.setmStrPrice(mEdtServicePrice.getText().toString());
                    serviceListDataClass.setmStrCatID(mStrCatID);
                    serviceListDataClass.setmStrServiceDesc(mEdtServiceDesc.getText().toString());
                    mArrayService.add(serviceListDataClass);
                    setServiceAdapter();
                    mDialogservice.dismiss();
                }

            }
        });

        /**
         * Dismiss dialogue
         */
        mImv_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialogservice.dismiss();
            }
        });
        mDialogservice.show();
    }

    private void checkServiceValidation() {

    }

    private void wsGetServiceCategory() {
        mStrGetCategorURL = getActivity().getString(R.string.WS_HOST_URL) + getString(R.string.WS_GET_SERVICE_CATEGORY);
        if (AvailableNetwork.isNetworkAvailable(getActivity())) {
            mAquery.progress(mProgressDlg).ajax(mStrGetCategorURL, JSONObject.class, ResponceAjax);
        } else {
            Toast.makeText(getActivity(), "" + getString(R.string.str_network_error), Toast.LENGTH_SHORT).show();
        }
    }

    AjaxCallback<JSONObject> ResponceAjax = new AjaxCallback<JSONObject>() {
        @Override
        public void callback(String url, JSONObject object, AjaxStatus status) {
            super.callback(url, object, status);
            if (object != null) {
                try {
                    if (url.equalsIgnoreCase(mStrGetCategorURL)) {
                        mArraylistServiceCat.clear();
                        JSONArray mJsonCatarray = object.getJSONArray("data");
                        for (int i = 0; i < mJsonCatarray.length(); i++) {
                            if (i == 0) {
                                DetailerClass detailerClass1 = new DetailerClass();
                                // JSONObject mJsoncatObject = mJsonCatarray.getJSONObject(i);
                                detailerClass1.setmStrServiceCategoryID("****");
                                detailerClass1.setmStrServiceCategoryName("Select Category");
                                mArraylistServiceCat.add(detailerClass1);
                            }
                            DetailerClass detailerClass = new DetailerClass();
                            JSONObject mJsoncatObject = mJsonCatarray.getJSONObject(i);
                            detailerClass.setmStrServiceCategoryID(mJsoncatObject.getString("id"));
                            detailerClass.setmStrServiceCategoryName(mJsoncatObject.getString("name"));
                            mArraylistServiceCat.add(detailerClass);
                        }
                        setSpinnerAdapter();
                    } else if (url.equalsIgnoreCase(mStrWsdetailerURL)) {
                        showAlert("Detailer Registration Successfully Done.", 1);

                    } else if (url.equalsIgnoreCase(mStrSocialRegistration)) {
                        if (object.getString("error_code").equalsIgnoreCase("0"))
                        {
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

                           /* Intent intent=new Intent(getActivity(), MainDashboard.class);
                            startActivity(intent);*/
                        //  showAlert(object.getString("msg"), 1);

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

    private void setSpinnerAdapter() {
        SpinnerAdapter spinnerAdapter = new SpinnerAdapter(getActivity(), mArraylistServiceCat);
        mSp_Category.setAdapter(spinnerAdapter);
        mSp_Category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    mStrCatID = mArraylistServiceCat.get(position).getmStrServiceCategoryID();
                } else {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void wsDetailerRegistration() {
        mStrWsdetailerURL = getActivity().getString(R.string.WS_HOST_URL) + getActivity().getString(R.string.WS_DETAILER_REGISTRATION);
        HashMap<String, Object> mParam = new HashMap<>();
        mParam.put("user_email", mEdtEmail.getText().toString());
        mParam.put("user_mobile", mEdtContactinfo.getText().toString());
        mParam.put("website_link", mEdtWebsite.getText().toString());
        mParam.put("facebook_connection_link", mEdtFacebooklink.getText().toString());
        mParam.put("instagram_connection_link", mEdtInstagramLink.getText().toString());
        mParam.put("password", mEdtPassword.getText().toString());
        mParam.put("opening_time",mStr24hoursopening);
        mParam.put("services", serviceArray);
        mParam.put("closing_time",mStr24hoursclossing);
        mParam.put("about_me", mEdtAboutme.getText().toString());
        mParam.put("company_name", mEdtCmpnyname.getText().toString());
        mParam.put("profile_picture", getImageByteArray(((BitmapDrawable) mImgProfilePic.getDrawable()).getBitmap()));
        mParam.put("work_video[0]", mAttachment);
        if (mEdtSuburbRegistration.isShown()) {
            mParam.put("user_address", mEdtSuburbRegistration.getText().toString());
        }
        mParam.put("latitude", lattitude);
        mParam.put("longitude", lagnitude);
        mParam.put("work_image_count", mLstHelperWorkImages.size());
        mParam.put("device", 1);
        mParam.put("ext", mStrSelectedFileExtension);
        for (int i = 0; i < mLstHelperWorkImages.size(); i++) {
            mParam.put("work_images[" + i + "]", mLstHelperWorkImages.get(i).imageByteArray);
        }
        if (AvailableNetwork.isNetworkAvailable(getActivity())) {
            mAquery.progress(mProgressDlg).ajax(mStrWsdetailerURL, mParam, JSONObject.class, ResponceAjax);
        }
        Log.d("mParameter", "" + mParam);
    }

    /*//to check dd print by Service dev
    AjaxCallback<String>sesponceAjax=new AjaxCallback<String>()
    {
        @Override
        public void callback(String url, String object, AjaxStatus status) {
            super.callback(url, object, status);
        }
    };*/
    private void setServiceAdapter() {
        ServiceExpandableHeigthAdapter mServiceAdapter = new ServiceExpandableHeigthAdapter(getActivity(), mArrayService, onClickListener);
        mListviewServices.setAdapter(mServiceAdapter);
        mServiceAdapter.notifyDataSetChanged();

    }

    /**
     * call onclick from adapter
     */
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String mStrRowIndex = "" + v.getTag();
            int Tagindex = Integer.parseInt(mStrRowIndex);
            mArrayService.remove(Tagindex);
            setServiceAdapter();
            convertIntoJSON();
        }
    };

    /**
     * select Image or any Doc
     */
    private void pickVideo() {
        Intent intent = new Intent();
        intent.setType("*/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select File"), REQUEST_CODE_FOR_PICK_FILE);
    }

    /**
     * call google plus screen to check login
     */
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClientdetailer);
        startActivityForResult(signInIntent, 999);
    }

    /**
     * Select image dialog
     *//*
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
                    intent.setType("image*//*");
                    startActivityForResult(Intent.createChooser(intent, "Select File"), REQUEST_CODE_CHOOSE_IMAGE_FROM_LIBRARY);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }*/


    /**
     * Select image dialog
     */
    private void pickImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};
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
                }else if (mStrProfImgFlag.equalsIgnoreCase("profileimg")) {
                    if (items[item].equals("Choose from Library")) {
                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/*");
                        startActivityForResult(Intent.createChooser(intent, "Select File"), REQUEST_CODE_CHOOSE_IMAGE_FROM_LIBRARY);
                    }
                }else if (mStrProfImgFlag.equalsIgnoreCase("addworkimg")){
                    if (items[item].equals("Choose from Library")) {
                        GalleryConfig config = new GalleryConfig.Build()
                                .limitPickPhoto(6)
                                .singlePhoto(false)
                                .hintOfPick("Select Multiple Image")
                                .filterMimeTypes(new String[]{"image/jpeg"})
                                .build();
                        GalleryActivity.openActivity(getActivity(), REQUEST_CODE_CHOOSE_IMAGE_FROM_LIBRARY, config);


                   /* Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image*//*");
                    startActivityForResult(Intent.createChooser(intent, "Select File"), REQUEST_CODE_CHOOSE_IMAGE_FROM_LIBRARY);*/
                    }
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
     * Time Picker Dialogue
     */
    Dialog mDialog;
    CustomTextView mTxtTitle;
    TimePicker mTmPicker;
    String mStrselectedTime = "null";
    String mStr24hoursopening,mStr24hoursclossing;
    private void openTimePickerDialogue() {
        mDialog = new Dialog(getActivity());
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.dialogue_timepicker);
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        mDialog.getWindow().setLayout((6 * width) / 6, LinearLayout.LayoutParams.WRAP_CONTENT);
        mTxtTitle = mDialog.findViewById(R.id.tv_title);
        mTmPicker = mDialog.findViewById(R.id.timepicker);
        if (mFlagsetdata.equalsIgnoreCase("opening")) {
            mTxtTitle.setText("Pick Opening Time");

        } else {
            mTxtTitle.setText("Pick Closing Time");

        }
        mTmPicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                //Display the new time to app interface
                if (mFlagsetdata.equalsIgnoreCase("opening")) {
                    DecimalFormat mFormat= new DecimalFormat("00");
                  //  mFormat.format(Double.valueOf(hourOfDay));
                    mStr24hoursopening = mFormat.format(Double.valueOf(hourOfDay)) + ":" + mFormat.format(Double.valueOf(minute));
                   // Toast.makeText(getActivity(), ""+mStr24hoursopening, Toast.LENGTH_SHORT).show();
                }else {
                    DecimalFormat mFormat1= new DecimalFormat("00");
                    mStr24hoursclossing =mFormat1.format(Double.valueOf(hourOfDay)) + ":" + mFormat1.format(Double.valueOf(minute));
                }
                String AMPM = "AM";
                if (hourOfDay > 11) {
                    hourOfDay = hourOfDay - 12;
                    AMPM = "PM";
                }
                DecimalFormat mFormat2= new DecimalFormat("00");

                mStrselectedTime = mFormat2.format(Double.valueOf(hourOfDay)) + ":" + mFormat2.format(Double.valueOf(minute))+" " + AMPM;
               // mStrselectedTime = hourOfDay + ":" + minute;

            }
        });
        mDialog.findViewById(R.id.tv_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mStrselectedTime.equalsIgnoreCase("null") || mStrselectedTime.equalsIgnoreCase("")) {
                    if (mFlagsetdata.equalsIgnoreCase("opening")) {
                        mEditInputOpeningTime.setText(mStrselectedTime);
                        mEditInputOpeningTime.clearFocus();
                    } else {
                        mEditInputClosingtime.setText(mStrselectedTime);
                        mEditInputClosingtime.clearFocus();
                    }
                    mDialog.dismiss();
                } else {
                    Toast.makeText(getActivity(), "Please Select Time First", Toast.LENGTH_SHORT).show();
                }

            }
        });
        mDialog.findViewById(R.id.tv_cancle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
        mDialog.setCancelable(true);
        mDialog.show();


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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_CAMERA_CAPTURE_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                String[] projection = {MediaStore.Images.Media.DATA};
                Cursor cursor = getActivity().managedQuery(mCapturedImageUri, projection, null, null, null);
                int column_index_data = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                String capturedImageFilePath = cursor.getString(column_index_data);
                Uri imageUri = Uri.parse("file:///" + capturedImageFilePath);
                String selectedImagePath;
                //MEDIA GALLERY
                /** @Flag
                 *  for Upload Doc Image Selection and Set Path to Textview
                 **/
                // if (Flag == 0) {
                selectedImagePath = ImageFilePath.getPath(getActivity(), imageUri);
                String filename = selectedImagePath.substring(selectedImagePath.lastIndexOf("/") + 1);
                    /*mTv_filename.setText(filename.toString());*/
                //  Log.d("Byte", "" + getImageByteArray(((BitmapDrawable) mImgAddServiceProfile.getDrawable()).getBitmap()));
                // }
                beginCrop(imageUri);
            }
        } else if (requestCode == REQUEST_CODE_CHOOSE_IMAGE_FROM_LIBRARY) {
            if (resultCode == Activity.RESULT_OK) {
                if (mStrProfImgFlag.equalsIgnoreCase("profileimg")) {
                    // mImgProfilePic.setImageURI(Uri.parse("file://" + mListWorkImagesLocalPath.get(0)));
                    //mImgProfilePic.setImageBitmap(bitmap);

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

                } else {
                    mListWorkImagesLocalPath = (List<String>) data.getSerializableExtra(GalleryActivity.PHOTOS);
                    setHorizontalView(mListWorkImagesLocalPath);
                }
           /* try {
                Uri imageUri = data.getData();
                InputStream is = getActivity().getContentResolver().openInputStream(imageUri);
                Bitmap bm = BitmapFactory.decodeStream(is);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.PNG, 90, stream);
                String selectedImagePath;
                *//** @Flag
                 *  for Upload Doc Image Selection and Set Path to Textview
                 **//*
                //MEDIA GALLERY

                bitmap = bm;
                beginCrop(imageUri);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }*/
            }
        } else if (requestCode == Crop.REQUEST_CROP) {
            // if (mMediaTypeFlag == 1) {
            handleCrop(resultCode, data);
            // }
        } else if (requestCode == REQUEST_CODE_FOR_PICK_FILE) {
            //pick file for upload
            if (resultCode == Activity.RESULT_OK) {
                try {
                    long mFileSize = 0;
                    Log.e("", "Data is null" + (data == null));
                    if (null == data) return;

                    String selectedImagePath;
                    Uri selectedImageUri = data.getData();

                    //MEDIA GALLERY
                    selectedImagePath = ImageFilePath.getPath(getActivity(), selectedImageUri);
                    mStrSelectedFileExtension = selectedImagePath.substring(selectedImagePath.lastIndexOf(".") + 1);
                    //  mStrSelectedFileExtention = mStrSelectedFileExtension;
                    Log.i("Image File Path", "" + selectedImagePath);
                    //txta.setText("File Path : \n"+selectedImagePath);

                    try {

                        File file = new File(selectedImagePath);
                        long length = file.length();
                        //  float a=((float) Math.round((length / (1024 * 1024)) * 10) / 10);
                        if (length > 1024) {
                            mFileSize = length / (1024 * 1024);
                        } else {
                            mFileSize = length;
                        }
                        // filesizemb=filesizekb/1024;
                        //  System.out.println("File Path : " + file.getPath() + ", File size : " + length +" MB");
                    } catch (Exception e) {
                        System.out.println("File not found : " + e.getMessage() + e);
                    }

                    Log.i("Image File Path", "" + selectedImagePath);
                    //txta.setText("File Path : \n"+selectedImagePath);
                    //if (mFileSize <= 4) {
                    Uri uri = data.getData();
                    if (uri != null) {
                        //     Toast.makeText(getActivity(), "" + mFileSize, Toast.LENGTH_SHORT).show();
                        try {
                            Log.e("", "lkj :" + uri.getPath());
                            File file = new File(selectedImagePath);
                            mAttachment = new byte[(int) file.length()];
                            FileInputStream fos = new FileInputStream(file);
                            BufferedInputStream bis = new BufferedInputStream(fos);
                            bis.read(mAttachment, 0, mAttachment.length);
                            Log.d("Attachment in BYte", "" + mAttachment);
                            filename = selectedImagePath.substring(selectedImagePath.lastIndexOf("/") + 1);
                            //  mTxtVideoName.setText(filename);
                            mTxtVideoname.setText(filename);
                        } catch (Exception e) {
                            Log.e("", "Exception" + e);
                        }
                    }

                    //  } else {
                    // Toast.makeText(getActivity(), "Video should be less than 4 MB.", Toast.LENGTH_SHORT).show();
                    //  }

                   /* Uri uri = data.getData();
                    if (uri != null) {
                        try {
                            Log.e("", "lkj :" + uri.getPath());
                            File file = new File(selectedImagePath);
                            mAttachment = new byte[(int) file.length()];
                            FileInputStream fos = new FileInputStream(file);
                            BufferedInputStream bis = new BufferedInputStream(fos);
                            bis.read(mAttachment, 0, mAttachment.length);
                            Log.d("Attachment in BYte", "" + mAttachment);
                            filename = selectedImagePath.substring(selectedImagePath.lastIndexOf("/") + 1);
                            mTxtVideoName.setText(filename);//https://www.youtube.com/watch?v=nDOpAEW2q2w

                        } catch (Exception e) {
                            Log.e("", "Exception" + e);
                        }
                    }*/

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if (requestCode == 999) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        } else if (FacebookSdk.isFacebookRequestCode(requestCode)) {
            callbackManagerDetailer.onActivityResult(requestCode, resultCode, data);

        }
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
                // mImgProp.setImageBitmap(bitmap);
                // if (EditFlag == 1) {
                if (mStrProfImgFlag.equalsIgnoreCase("profileimg")) {
                    mImgProfilePic.setImageBitmap(bitmap);
                } else {
                    setHorizontalViewfromcamera(bitmap);
                }

                // }
                // Toast.makeText(getActivity(), "" + bitmap, Toast.LENGTH_SHORT).show();
               /* mTxtImageName.setText("file selected");*/
            } else {

            }
            //   mImgProp.setImageResource(R.drawable.imag_not_availbl);
        } else if (resultCode == Crop.RESULT_ERROR) {
        }
    }

    ImageView mImgProp1;

    /**
     * image From Gallary
     *
     * @param mArraylist
     */
    private void setHorizontalView(List<String> mArraylist) {
        for (int i = 0; i < mArraylist.size(); i++) {
            HelperWorkImage item = new HelperWorkImage();
            View mViewUser = getActivity().getLayoutInflater().inflate(R.layout.singleimagelayout, null);
            mImgProp1 = (ImageView) mViewUser.findViewById(R.id.uploadedimg);
            ImageView imgDelete = (ImageView) mViewUser.findViewById(R.id.deleteimg);
            // mImgProp1.setImageBitmap(mBmp);
            mImgProp1.setImageURI(Uri.parse("file://" + mArraylist.get(i)));
            item.mImgTaskImage = mImgProp1;
            item.mImgDelete = imgDelete;
            item.imageByteArray = getImageByteArray(((BitmapDrawable) mImgProp1.getDrawable()).getBitmap());
            item.mImgDelete.setTag("" + mLstHelperWorkImages.size());
            item.mImgDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        int pos = Integer.parseInt("" + v.getTag());
                        mLLMainLayout.removeViewAt(pos);
                        mLstHelperWorkImages.remove(pos);
                        reindexing();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //     showDialogDeleteImage("Do you want to delete this image?", v);
                }
            });
            mLstHelperWorkImages.add(item);
            mLLMainLayout.addView(mViewUser);
            if (mLstHelperWorkImages.size() > 0) {
                //  mTxtImageName.setText(mLstHelperWorkImages.size() + " file Selected");
            } else {
                // mTxtImageName.setText("No file Selected");
            }
        }
    }
    private void setHorizontalViewfromcamera(Bitmap mBmp) {
       // for (int i = 0; i < mArraylist.size(); i++) {
            HelperWorkImage item = new HelperWorkImage();
            View mViewUser = getActivity().getLayoutInflater().inflate(R.layout.singleimagelayout, null);
            mImgProp1 = (ImageView) mViewUser.findViewById(R.id.uploadedimg);
            ImageView imgDelete = (ImageView) mViewUser.findViewById(R.id.deleteimg);
             mImgProp1.setImageBitmap(mBmp);
          //  mImgProp1.setImageURI(Uri.parse("file://" + mArraylist.get(i)));
            item.mImgTaskImage = mImgProp1;
            item.mImgDelete = imgDelete;
            item.imageByteArray = getImageByteArray(((BitmapDrawable) mImgProp1.getDrawable()).getBitmap());
            item.mImgDelete.setTag("" + mLstHelperWorkImages.size());
            item.mImgDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        int pos = Integer.parseInt("" + v.getTag());
                        mLLMainLayout.removeViewAt(pos);
                        mLstHelperWorkImages.remove(pos);
                        reindexing();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //     showDialogDeleteImage("Do you want to delete this image?", v);
                }
            });
            mLstHelperWorkImages.add(item);
            mLLMainLayout.addView(mViewUser);
            if (mLstHelperWorkImages.size() > 0) {
                //  mTxtImageName.setText(mLstHelperWorkImages.size() + " file Selected");
            } else {
                // mTxtImageName.setText("No file Selected");
            }
       // }
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

    private class HelperWorkImage {
        Bitmap mBmp;
        byte[] imageByteArray;
        ImageView mImgTaskImage;
        ImageView mImgDelete;
    }

    private void showDialogDelete(final int mPos) {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
        mBuilder.setCancelable(false);
        mBuilder.setMessage("Do you want to delete work?");
        mBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                //  navigateFlagDeleteWork(mPos);
            }
        });

        mBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                // navigateFlag(flag);
            }
        });

        mBuilder.create();
        mBuilder.show();
    }

    private void reindexing() {
        for (int i = 0; i < mLstHelperWorkImages.size(); i++) {
            mLstHelperWorkImages.get(i).mImgDelete.setTag("" + i);
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

    /**
     * Facebook Login Method
     */
    private void regFacebook() {
        callbackManagerDetailer = CallbackManager.Factory.create();
        loginManagercustomer = LoginManager.getInstance();
        if (loginManagercustomer == null) {
            LoginManager.getInstance().logOut();
        }
        loginManagercustomer.logInWithReadPermissions(getActivity(), Arrays.asList("public_profile", "user_friends", "email"));
        List<String> permissions = new ArrayList<>();
        permissions.add("email");
        LoginManager.getInstance().registerCallback(callbackManagerDetailer, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {

                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {
                            Log.e("*Facebook", "Facebook responce-->" + response.toString());
                            //   Toast.makeText(getActivity(), "" + object.getString("email") + " " + object.getString("id"), Toast.LENGTH_SHORT).show();
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

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            Log.e(TAG, "display name: " + acct.getDisplayName());
            String personName = acct.getDisplayName();
            String[] Name = personName.split(" ");
           // String personPhotoUrl = acct.getPhotoUrl().toString();
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
      /*  mGoogleApiClientdetailer.stopAutoManage(getActivity());
        mGoogleApiClientdetailer.disconnect();*/
    }

    private boolean checkIsGoogleSignIn() {
        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClientdetailer);
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
        Auth.GoogleSignInApi.signOut(mGoogleApiClientdetailer).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                    }
                });
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClientdetailer != null && mGoogleApiClientdetailer.isConnected()) {
            mGoogleApiClientdetailer.disconnect();
        }
        if (mGoogleApiClientplaces != null && mGoogleApiClientplaces.isConnected()) {
            mGoogleApiClientplaces.stopAutoManage(getActivity());
            mGoogleApiClientplaces.disconnect();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mGoogleApiClientdetailer != null) {
            mGoogleApiClientdetailer.connect();
        }
        if (mGoogleApiClientplaces != null)
            mGoogleApiClientplaces.connect();
    }

    private void wsSocialLogin(String checkSocialStatus, String email, String socialID, String firstName, String lastName) {
        mStrSocialRegistration = getActivity().getString(R.string.WS_HOST_URL) + getActivity().getString(R.string.WS_SOCIAL_REGISTRATION);
        HashMap<String, Object> mParams = new HashMap<>();
        mParams.put("first_name", firstName);
        mParams.put("last_name", lastName);
        mParams.put("user_email", email);
        mParams.put("user_type", "3");

        if (checkSocialStatus.equalsIgnoreCase("1")) {
            mParams.put("fb_id", socialID);
            mParams.put("google_id", "");
        } else {
            mParams.put("google_id", socialID);
            mParams.put("fb_id", "");
        }
        if (AvailableNetwork.isNetworkAvailable(getActivity())) {
            mAquery.progress(mProgressDlg).ajax(mStrSocialRegistration, mParams, JSONObject.class, ResponceAjax);
        } else {
            Toast.makeText(getActivity(), "" + getActivity().getString(R.string.str_network_error), Toast.LENGTH_SHORT).show();
        }

    }
    static String regex = "^(https?://www.facebook.com/)$";
    private boolean checkValidation() {
        if (mEdtCmpnyname.isEmpty()) {
            mEdtCmpnyname.setError("Please enter Company Name.");
            return false;
        } else if (mEdtWebsite.isEmpty()) {
            mEdtWebsite.setError("Please enter Website Link.");
            return false;
        } else if (!Validation.chekWebSiteUrl(mEdtWebsite.getText().toString().trim())) {
            mEdtWebsite.setError("Please enter valid Web url.(example: " + "www.xyz.com" + ") ");
            return false;
        } else if (mEdtEmail.isEmpty()) {
            mEdtEmail.setError("Please enter email.");
            return false;
        } else if (!Validation.checkEmail(mEdtEmail.getString().trim())) {
            mEdtEmail.setError("Please enter valid email.");
            return false;
        } else if (mEdtPassword.isEmpty()) {
            mEdtPassword.setError("Please enter password.");
            return false;
        } else if (mEdtPassword.getString().length() < 8) {
            mEdtPassword.setError("Please enter atleast 8 characters.");
            return false;
        } else if (!mEdtPassword.getText().toString().trim().equalsIgnoreCase(mEdtConfirmPasword.getText().toString().trim())) {
            mEdtConfirmPasword.setError("Password does not match.");
            return false;
        } else if (mEdtFacebooklink.isEmpty()) {
            mEdtFacebooklink.setError("Please enter your facebook profile link.");
            return false;
        }else if (!Validation.checkfacebookprofileurl(mEdtFacebooklink.getString().trim())) {
            mEdtFacebooklink.setError("Please enter valid facebook profile link.");
            return false;
        }/*else if (mEdtFacebooklink.getText().toString().matches(regex)) {
            mEdtFacebooklink.setError("Please enter valid Facebook profile link.");
            return false;
        }*/ else if (mEdtInstagramLink.isEmpty()) {
            mEdtInstagramLink.setError("Please enter your Instagram profile link.");
            return false;
        }else if (!Validation.checkinstagramprofile(mEdtInstagramLink.getString().trim())) {
            mEdtInstagramLink.setError("Please enter Valid Instagram profile link.");
            return false;
        } /* else if (!Validation.chekWebSiteUrl(mEdtWebsite.getText().toString())) {
            mEdtWebsite.setError("Please enter valid Web url.");
            return false;
        }*/ else if (mLstHelperWorkImages.size() == 0) {
            showAlert("Please select atleast one Work Image.", -1);
            return false;
        } else if (mEditInputOpeningTime.getText().toString().trim().length() == 0) {
            //mEditInputOpeningTime.setError("Please select opening time.");
            showAlert("Please select opening time.",-1);
            return false;
        } else if (mEditInputClosingtime.getText().toString().trim().length() == 0) {
            showAlert("Please select closing  time.",-1);
            return false;
        } else if (mEdtContactinfo.getText().toString().trim().length() < 5) {
            mEdtContactinfo.setError("Phone number can't be less than 5 digit.");
            return false;
        } else if (mEdtSuburbRegistration.isShown()) {
            if (mEdtSuburbRegistration.getText().toString().trim().length() > 0) {
                return true;
            } else {
                mEdtSuburbRegistration.setError("Please enter Address.");
                return false;
            }
        } else if (!mChecktermscondition.isChecked()) {
            showAlert("Please check terms and Conditions.", -1);
            return false;
        }
        return true;
    }
    private boolean checkserviceValidation() {
        if (mSp_Category.getSelectedItemPosition()==0) {
            showAlert("Please select category first.",-1);
            return false;
        } else if (mEdtServiceName.isEmpty()) {
            mEdtServiceName.setError("Please enter Service Name.");
            return false;
        } else if (mEdtServicePrice.isEmpty()) {
            mEdtServicePrice.setError("Please enter service price.");
            return false;
        } else if (mEdtServiceDesc.isEmpty()) {
            mEdtServiceDesc.setError("Please enter service description.");
            return false;
        }
        return true;
    }

    void navigateFlag(int flag) {
        if (flag == 1) {
            Intent intent = new Intent(getActivity(), MainDashboard.class);
            startActivity(intent);
        }
    }
}
