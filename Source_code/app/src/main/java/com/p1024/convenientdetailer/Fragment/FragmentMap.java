package com.p1024.convenientdetailer.Fragment;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.p1024.convenientdetailer.CustomViews.CustomEditText;
import com.p1024.convenientdetailer.CustomViews.CustomTextView;
import com.p1024.convenientdetailer.CustomViews.RoundedImageView;
import com.p1024.convenientdetailer.CustomViews.TransparentProgressDialog;
import com.p1024.convenientdetailer.Interface.PositionInterface;
import com.p1024.convenientdetailer.R;
import com.p1024.convenientdetailer.Utility.GPSTracker;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by panacea on 12/26/17.
 */

public class FragmentMap extends Fragment implements OnMapReadyCallback, View.OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,LocationListener {

    private AQuery mAQuery;
    private TransparentProgressDialog mProgressDialog;
    private View mView;
    private GoogleMap mMap;
    private CustomTextView mTxtTalents, mTxtJobs, mTxtPearTrons, mTxtSelectCity;
    private CustomEditText mEdtSearchAll, mEdtSelectMiles;
    private RoundedImageView mRImgUserProfile;
    private CustomTextView mTxtUserName, mTxtUserAddress, mTxtUserMobileNo;
    //  private ImageView mImgDownArrow, mImgUpArrow, mImgSearch;
    private ArrayList<HashMap<String, String>> mCategoryChild;
    private ArrayList<HashMap<String, String>> mArrayServiceProvider;
    private ArrayList<HashMap<String, String>> mCategory;
    // private AdapterExpandableListView adapterListView;
    private ExpandableListView expListView;

    private Marker marker;
    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(37.398160, -122.180831), new LatLng(37.430610, -121.972090));
    private static final int GOOGLE_API_CLIENT_ID = 0;
    private GPSTracker gps;
    private Geocoder geocoder;
    public static String mStrZipcode = "";
    // public static double mStrLatitude, mStrLogitude;
    private GoogleApiClient mGoogleApiClient;
    private String mStrServices;
    private ArrayList<Marker> mArrayMarker;
    private ImageView mImageGpsLocation;
    private double latitude, longitude;
    private CustomTextView mTxtName, mTxtCatName;
    private boolean not_first_time_showing_info_window;
    private ListView mListView;
    private ArrayList<HashMap<String, String>> mArrUsersByLatLon, mArrSameUser;
    private String mStrGetLatLong;
    private Dialog mDialog;
    private ImageView mImgSearch, mImgFilter;
    private LinearLayout mL_LayoutSearch, mLinearHeaderMapLayout;

    /**
     * to get current or last know lacation
     */
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationManager locationManager;
    private String provider;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_map, container, false);
        setUI();
        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();

        /*mImgSearch.setVisibility(View.VISIBLE);
        mImgFilter.setVisibility(View.VISIBLE);*/
    }

    private void setUI() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());

        mL_LayoutSearch = mView.findViewById(R.id.mllSearchlayout);
        mImgSearch = getActivity().findViewById(R.id.icsearch);
        mImgFilter = getActivity().findViewById(R.id.icfilter);
        mLinearHeaderMapLayout = getActivity().findViewById(R.id.maplayout);
        mLinearHeaderMapLayout.setVisibility(View.VISIBLE);
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.mapView);
        mapFragment.getMapAsync(this);
       /* mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(this, GOOGLE_API_CLIENT_ID, this)
                .addConnectionCallbacks(this)
                .build();*/


        mAQuery = new AQuery(getActivity());
        //mProgressDialog = new TransparentProgressDialog(getActivity(), R.drawable.loader);
        mArrayServiceProvider = new ArrayList<>();
        mArrayMarker = new ArrayList<>();
        mView.findViewById(R.id.search_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onMapSearch(v);
            }
        });
        //   mImageGpsLocation = mView.findViewById(R.id.image_gps_location);

       /* mTxtJobs = (CustomTextView) mView.findViewById(R.id.txt_jobs);
        mTxtTalents = (CustomTextView) mView.findViewById(R.id.txt_talents);
        mTxtPearTrons = (CustomTextView) mView.findViewById(R.id.txt_peartrons);
        mTxtSelectCity = (CustomTextView) mView.findViewById(R.id.txt_select_city);
        mEdtSelectMiles = (CustomEditText) mView.findViewById(R.id.edt_select_miles);
        mEdtSearchAll = (CustomEditText) mView.findViewById(R.id.edt_search_all);
        mImgUpArrow = (ImageView) mView.findViewById(R.id.img_up_arrow);
        mImgDownArrow = (ImageView) mView.findViewById(R.id.img_down_arrow);
        mImgSearch = (ImageView) mView.findViewById(R.id.img_search);*/

        /*((ActivityMainDashBoard) getActivity()).setActionBarTitle("Home");*/

       /* mTxtUserName = (CustomTextView) mView.findViewById(R.id.tv_username);
        mTxtUserAddress = (CustomTextView) mView.findViewById(R.id.tv_address);
        mTxtUserMobileNo = (CustomTextView) mView.findViewById(R.id.tv_mobile_no);
        mRImgUserProfile = (RoundedImageView) mView.findViewById(R.id.rimg_dashboard_userProfile);*/
        /*mView.findViewById(R.id.rl_layout_outer).setVisibility(View.VISIBLE);*/
        setListeners();
    }


    private void setListeners() {
        mImgSearch.setOnClickListener(this);
        mImgFilter.setOnClickListener(this);

        //  mTxtPearTrons.setOnClickListener(this);
        //   mEdtSelectMiles.setOnClickListener(this);
        //  mTxtJobs.setOnClickListener(this);
        //  mTxtSelectCity.setOnClickListener(this);
        //   mTxtTalents.setOnClickListener(this);
        //   mImgDownArrow.setOnClickListener(this);
        //  mImgUpArrow.setOnClickListener(this);
        //  mImgSearch.setOnClickListener(this);
        //  mEdtSearchAll.setOnClickListener(this);
//        mImageGpsLocation.setOnClickListener(this);
    }

    @Override
    public void onMapReady(GoogleMap mGoogleMap) {
        mMap = mGoogleMap;
        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getActivity(), R.raw.style_json));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(18.5158, 73.9272)));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(12));
        gps = new GPSTracker(getActivity());

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
       // Location location = locationManager.getLastKnownLocation(provider);

        // Initialize the location fields
       /* if (location != null) {
            System.out.println("Provider " + provider + " has been selected.");
            onLocationChanged(location);
        } else {

        }*/

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                         //   LatLng latLng=new LatLng(location.getLatitude(),location.getLongitude());
                            mMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())).title("It's Me!"));
                            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

                                @Override
                                public boolean onMarkerClick(Marker marker) {
                                    FragmentManager fM=getActivity().getSupportFragmentManager();
                                    FragmentTransaction ft=fM.beginTransaction();
                                    ft.replace(R.id.content_frame,new FragmentDetailerPublicprofile()).commit();
                                    Toast.makeText(getActivity(), "MARKER CLICK"+marker, Toast.LENGTH_SHORT).show();
                                    return false;
                                }
                            });
                            // Logic to handle location object
                        }
                    }
                });


        //  geocoder = new Geocoder(getActivity());
        // check if GPS enabled
        if (gps.canGetLocation()) {
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
            // String lat = String.valueOf(latitude);
            // String log = String.valueOf(longitude);
        }


        //getUsers();
    }

    /* @Override
     public void onClick(View v) {
         switch (v.getId()) {
             case R.id.txt_header:
                 break;

             case R.id.edt_search_all:
                 // OpenServiceDialog();
                 break;

             case R.id.txt_select_city:
                 break;

             case R.id.txt_talents:
                 mView.findViewById(R.id.view_talent).setVisibility(View.VISIBLE);
                 mView.findViewById(R.id.view_job).setVisibility(View.GONE);
                 mView.findViewById(R.id.view_peartron).setVisibility(View.GONE);
                 break;

             case R.id.txt_peartrons:
                 mView.findViewById(R.id.view_talent).setVisibility(View.GONE);
                 mView.findViewById(R.id.view_job).setVisibility(View.GONE);
                 mView.findViewById(R.id.view_peartron).setVisibility(View.VISIBLE);
                 break;

             case R.id.txt_jobs:
                 mView.findViewById(R.id.view_talent).setVisibility(View.GONE);
                 mView.findViewById(R.id.view_job).setVisibility(View.VISIBLE);
                 mView.findViewById(R.id.view_peartron).setVisibility(View.GONE);
                 break;

             case R.id.img_search:
                 break;

             case R.id.edt_select_miles:
                 break;

             case R.id.img_down_arrow:
                 mEdtSearchAll.setClickable(false);
                 mEdtSelectMiles.setClickable(false);
                 mImgUpArrow.setClickable(true);
                 mView.findViewById(R.id.rl_layout_outer).startAnimation(outToDownAnimation());
                 mView.findViewById(R.id.rl_layout_outer).setVisibility(View.GONE);
                 break;

             case R.id.img_up_arrow:
                 mView.findViewById(R.id.rl_layout_outer).startAnimation(inFromDownAnimation());
                 mView.findViewById(R.id.rl_layout_outer).setVisibility(View.VISIBLE);
                 mEdtSearchAll.setClickable(true);
                 mEdtSelectMiles.setClickable(true);
                 mImgUpArrow.setClickable(false);
                 break;

             case R.id.image_gps_location:
                 // CameraUpdate update = CameraUpdateFactory.newLatLng(new LatLng(Double.parseDouble(mArrayServiceProvider.get(0).get("latitude")), Double.parseDouble(mArrayServiceProvider.get(0).get("logitude"))));
                 // mMap.moveCamera(update);
                 break;
         }
     }
 */
    public Animation inFromDownAnimation() {
        Animation inFromLeft = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 5.0f,
                Animation.RELATIVE_TO_SELF, 0.0f);
        inFromLeft.setDuration(500);
        inFromLeft.setFillAfter(false);
        inFromLeft.setFillEnabled(false);
        //inFromLeft.setInterpolator(new AccelerateInterpolator());
        return inFromLeft;
    }

    public Animation outToDownAnimation() {
        Animation inFromLeft = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 5.0f);
        inFromLeft.setDuration(500);
        inFromLeft.setFillAfter(false);
        inFromLeft.setFillEnabled(false);
        //inFromLeft.setInterpolator(new AccelerateInterpolator());
        return inFromLeft;
    }

    private void createChildData() {
        for (int i = 0; i < 2; i++) {
            HashMap cat = new HashMap();
            cat.put("name", "vai" + i);
            cat.put("id", "" + i);
            mCategoryChild.add(cat);
        }
    }

    private void createSubChildData() {
        for (int j = 0; j < 10; j++) {
            HashMap map = new HashMap();
            map.put("name", "sdfs" + j);
            map.put("id", "" + j);
            mCategory.add(map);
        }
    }



/*
    private void setAdapter() {
        adapterListView = new AdapterExpandableListView(getActivity(), mCategoryChild, mCategory);
        expListView.setAdapter(adapterListView);

        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                //   final String selected = (String) expListAdapter.getChild(groupPosition, childPosition);
                //Toast.makeText(getBaseContext(), "some", Toast.LENGTH_LONG)
                // .show();

                return true;
            }
        });
    }
*/

  /*  */
    /**
     * get markers from location
     *//*
    private void getUsers() {
        mStrServices = getString(R.string.WS_HOST) + getString(R.string.WS_GET_SERVICE_PROVIDERS);
        HashMap<String, Object> mParams = new HashMap<>();
        mParams.put("user_id", SessionClass.getInstance().getValue(getActivity(), SessionConstants.KEY_USER_ID));

        if (AvailableNetwork.isNetworkAvailable(getActivity())) {
            mAQuery.progress(mProgressDialog).ajax(mStrServices, mParams, JSONObject.class, mAjaxCallback);
        } else {
            showAlert(getString(R.string.str_network_error), -1);
        }
    }

    AjaxCallback<JSONObject> mAjaxCallback = new AjaxCallback<JSONObject>() {
        @Override
        public void callback(String url, JSONObject object, AjaxStatus status) {
            super.callback(url, object, status);
            if (object != null) {
                try {
                    JSONObject mJson = object.getJSONObject("Response");
                    if (url.equalsIgnoreCase(mStrServices)) {
                        if (mJson.getString("error_code").equalsIgnoreCase("0")) {
                            JSONArray arrayServiceProvider = mJson.getJSONArray("service_providers");
                            mArrayServiceProvider.clear();
                            for (int i = 0; i < arrayServiceProvider.length(); i++) {
                                JSONObject objectServiceProvider = arrayServiceProvider.getJSONObject(i);
                                HashMap<String, String> mHash = new HashMap<>();
                                mHash.put("user_id", objectServiceProvider.getString("user_id"));
                                mHash.put("user_name", objectServiceProvider.getString("user_name"));
                                mHash.put("user_email", objectServiceProvider.getString("user_email"));
                                mHash.put("profile_picture", objectServiceProvider.getString("profile_picture"));
                                mHash.put("cover_image", objectServiceProvider.getString("cover_image"));
                                mHash.put("latitude", objectServiceProvider.getString("latitude"));
                                mHash.put("logitude", objectServiceProvider.getString("logitude"));
                                mHash.put("city_name", objectServiceProvider.getString("city_name"));
                                mHash.put("state_name", objectServiceProvider.getString("state_name"));
                                mHash.put("total_rating", objectServiceProvider.getString("total_rating"));
                                mHash.put("category_name", objectServiceProvider.getString("category_name"));
                                mHash.put("average_price_rate", objectServiceProvider.getString("average_price_rate"));
                                mHash.put("one_line_description", objectServiceProvider.getString("one_line_description"));
                                mHash.put("description", objectServiceProvider.getString("description"));
                                mHash.put("phone_no", objectServiceProvider.getString("phone_no"));
                                mHash.put("about_me", objectServiceProvider.getString("about_me"));
                                mHash.put("first_name", objectServiceProvider.getString("first_name"));
                                mHash.put("last_name", objectServiceProvider.getString("last_name"));
                                mHash.put("user_birth_date", objectServiceProvider.getString("user_birth_date"));
                                mHash.put("first_name", objectServiceProvider.getString("first_name"));
                                mHash.put("zip_code", objectServiceProvider.getString("zip_code"));

                                mArrayServiceProvider.add(mHash);
                            }
                            setMapMarker(mArrayServiceProvider);
                        } else {
                            showAlert(mJson.getString("msg"), -1);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                showAlert(getString(R.string.str_server_error), -1);
            }
        }
    };*/

    private int ii;

    /**
     * set marker on map
     */
    private void setMapMarker(final ArrayList<HashMap<String, String>> mArrayServiceProvider) {
        try {
            if (marker != null) {
                marker.remove();
            }
            mMap.clear();
            if (mArrayServiceProvider.size() > 0) {
                for (int j = 0; j < mArrayServiceProvider.size(); j++) {
                    //   for (int k = 0; k < mArrayServiceProvider.size(); k++) {
                    //   if (!mArrayServiceProvider.get(j).get("zip_code").equalsIgnoreCase(mArrayServiceProvider.get(k).get("zip_code")))
                    marker = mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(Double.parseDouble(mArrayServiceProvider.get(j).get("latitude")), Double.parseDouble(mArrayServiceProvider.get(j).get("logitude"))))
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_menu)));

                    mArrayMarker.add(marker);

                    //  }
                }
                CameraUpdate update = CameraUpdateFactory.newLatLng(new LatLng(Double.parseDouble(mArrayServiceProvider.get(0).get("latitude")), Double.parseDouble(mArrayServiceProvider.get(0).get("logitude"))));
                //  CameraUpdate zoom1 = CameraUpdateFactory.zoomTo(10.7f);
                mMap.moveCamera(update);
                //   mMap.animateCamera(zoom1);

                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    private View view = null;
                    private ImageView mImgProfile;

                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        LatLng latLng = marker.getPosition();
                        double latitude1 = latLng.latitude;
                        double longitude1 = latLng.longitude;

                        for (int i = 0; i < mArrayServiceProvider.size(); i++) {
                            if (mArrayServiceProvider.get(i).get("latitude").equalsIgnoreCase(formatNumber(6, latitude1)) && mArrayServiceProvider.get(i).get("logitude").equalsIgnoreCase(formatNumber(6, longitude1))) {
                                mArrSameUser = new ArrayList<>();
                                for (int j = 0; j < mArrayServiceProvider.size(); j++) {
                                    if (mArrayServiceProvider.get(i).get("zip_code").equalsIgnoreCase(mArrayServiceProvider.get(j).get("zip_code"))) {
                                        mArrSameUser.add(mArrayServiceProvider.get(j));
                                    }
                                }
                                //  setUserListAdapter();
                                // openDialog(mArrSameUser);
                                break;
                            }
                        }


                        // view = getActivity().getLayoutInflater().inflate(R.layout.dialog_marker, null);
                        // view.setBackgroundResource(0);
                        // mListView = (ListView) view.findViewById(R.id.listview);

//                        for (int i = 0; i < mArrayServiceProvider.size(); i++) {
                        //                          if (mArrayServiceProvider.get(i).get("latitude").equalsIgnoreCase(formatNumber(6, latLng.latitude)) && mArrayServiceProvider.get(i).get("logitude").equalsIgnoreCase(formatNumber(6, latLng.longitude))) {
                        //                            mArrSameUser = new ArrayList<>();
                        //                          for (int j = 0; j < mArrayServiceProvider.size(); j++) {
                        //                            if (mArrayServiceProvider.get(i).get("zip_code").equalsIgnoreCase(mArrayServiceProvider.get(j).get("zip_code"))) {
                        //                              mArrSameUser.add(mArrayServiceProvider.get(j));
                        //                        }
                        //                  }
                               /* AdapterUserByLatLong mAdapter = new AdapterUserByLatLong(getActivity(), posInterfaceUserByLatLong, mArrSameUser, marker);
                                mListView.setAdapter(mAdapter);*/

                                /*mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        Toast.makeText(getActivity(), "" + position, Toast.LENGTH_SHORT).show();
                                    }
                                });*/
                        //                 break;
                        //            }

                           /* if (mArrayServiceProvider.get(i).get("latitude").equalsIgnoreCase(formatNumber(6, latLng.latitude)) && mArrayServiceProvider.get(i).get("logitude").equalsIgnoreCase(formatNumber(6, latLng.longitude))) {

                                getUsersByLatlng(mArrayServiceProvider.get(i).get("user_id"), mArrayServiceProvider.get(i).get("user_type"), mArrayServiceProvider.get(i).get("latitude"), mArrayServiceProvider.get(i).get("logitude"));

                             //   break;
                            } else {
                                view = null;
                            }*/

                        //marker.hideInfoWindow();
                        // marker.hideInfoWindow();

                        return false;
                    }
                });
              /*  mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                    private View view = null;
                    private ImageView mImgProfile;

                    @Override
                    public View getInfoWindow(Marker mark) {
                        LatLng latLng = mark.getPosition();
                        double latitude1 = latLng.latitude;
                        double longitude1 = latLng.longitude;

                        view = getActivity().getLayoutInflater().inflate(R.layout.dialog_marker, null);
                        view.setBackgroundResource(0);
                        mListView = (ListView) view.findViewById(R.id.listview);

                        for (int i = 0; i < mArrayServiceProvider.size(); i++) {
                            if (mArrayServiceProvider.get(i).get("latitude").equalsIgnoreCase(formatNumber(6, latLng.latitude)) && mArrayServiceProvider.get(i).get("logitude").equalsIgnoreCase(formatNumber(6, latLng.longitude))) {
                                mArrSameUser = new ArrayList<>();
                                for (int j = 0; j < mArrayServiceProvider.size(); j++) {
                                    if (mArrayServiceProvider.get(i).get("zip_code").equalsIgnoreCase(mArrayServiceProvider.get(j).get("zip_code"))) {
                                        mArrSameUser.add(mArrayServiceProvider.get(j));
                                    }
                                }

                                AdapterUserByLatLong mAdapter = new AdapterUserByLatLong(getActivity(), posInterfaceUserByLatLong, mArrSameUser, mark);
                                mListView.setAdapter(mAdapter);

                                mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        Toast.makeText(getActivity(), "" + position, Toast.LENGTH_SHORT).show();
                                    }
                                });
                                break;

                            }

                           *//* if (mArrayServiceProvider.get(i).get("latitude").equalsIgnoreCase(formatNumber(6, latLng.latitude)) && mArrayServiceProvider.get(i).get("logitude").equalsIgnoreCase(formatNumber(6, latLng.longitude))) {

                                getUsersByLatlng(mArrayServiceProvider.get(i).get("user_id"), mArrayServiceProvider.get(i).get("user_type"), mArrayServiceProvider.get(i).get("latitude"), mArrayServiceProvider.get(i).get("logitude"));

                             //   break;
                            } else {
                                view = null;
                            }*//*
                        }

                        mark.hideInfoWindow();
                        marker.hideInfoWindow();

                        return view;
                    }

                  *//*  @Override
                    public View getInfoWindow(final Marker mark) {
                        LatLng latLng = mark.getPosition();
                        double latitude1 = latLng.latitude;
                        double longitude1 = latLng.longitude;

                        for (int i = 0; i < mArrayServiceProvider.size(); i++) {
                            if (mArrayServiceProvider.get(i).get("latitude").equalsIgnoreCase(formatNumber(6, latLng.latitude)) && mArrayServiceProvider.get(i).get("logitude").equalsIgnoreCase(formatNumber(6, latLng.longitude))) {
                                view = getActivity().getLayoutInflater().inflate(R.layout.dialog_marker, null);
                                view.setBackgroundResource(0);

                                mImgProfile = (RoundedImageView) view.findViewById(R.id.img_marker_profile);
                                mTxtName = (CustomTextView) view.findViewById(R.id.txt_name);
                                mTxtCatName = (CustomTextView) view.findViewById(R.id.txt_category);


                                *//**//*Glide.with(getActivity())
                                        .load(getString(R.string.WS_PROFILE_IMAGE_PATH) + mArrayServiceProvider.get(i).get("profile_picture"))
                                        .into(mImgProfile);*//**//*

                                mAQuery.id(mImgProfile).image(getString(R.string.WS_PROFILE_IMAGE_PATH) + mArrayServiceProvider.get(i).get("profile_picture"), true, true, 35, R.drawable.imag_not_availbl, new BitmapAjaxCallback() {
                                    @Override
                                    protected void callback(String url, ImageView iv, Bitmap bm, AjaxStatus status) {
                                        super.callback(url, iv, bm, status);
                                        if (bm != null) {
                                            if (mark.isInfoWindowShown()) {
                                                mark.hideInfoWindow();
                                                mark.showInfoWindow();
                                            }
                                            // mark.hideInfoWindow();
                                            //mark.showInfoWindow();
                                        }
                                    }
                                });
                                mTxtName.setText(mArrayServiceProvider.get(i).get("first_name") + " " + mArrayServiceProvider.get(i).get("last_name"));
                                mTxtCatName.setText(mArrayServiceProvider.get(i).get("category_name"));
                                break;
                            } *//**//*else {
                                view = null;
                            }*//**//*
                        }
                        mark.hideInfoWindow();
                        marker.hideInfoWindow();

                        return view;
                    }*//*

                    @Override
                    public View getInfoContents(Marker mark) {
                        View v = null;
                        LatLng latLng = mark.getPosition();
                        for (int i = 0; i < mArrayServiceProvider.size(); i++) {
                            if (mArrayServiceProvider.get(i).get("latitude").equalsIgnoreCase(formatNumber(6, latLng.latitude)) && mArrayServiceProvider.get(i).get("logitude").equalsIgnoreCase(formatNumber(6, latLng.longitude))) {
                                v = getActivity().getLayoutInflater().inflate(R.layout.dialog_marker, null);
                                break;
                            } else {
                                v = null;
                            }
                        }
                        return v;
                    }
                });*/

               /* mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick(Marker marker) {
                        double mLat = marker.getPosition().latitude;
                        double mLong = marker.getPosition().longitude;
                        for (int i = 0; i < mArrayServiceProvider.size(); i++) {
                            double mArrLat = Double.parseDouble(mArrayServiceProvider.get(i).get("latitude"));
                            double mArrLon = Double.parseDouble(mArrayServiceProvider.get(i).get("logitude"));
                            if (mLat == mArrLat && mLong == mArrLon) {
                                Intent intentDetail = new Intent(getActivity(), ActivityGetServiceUsersList.class);
                                //intentDetail.putExtra("user_id", mArrLstPhotoDetail.get(i).get("user_id"));
                                //intentDetail.putExtra("profile_picture", mArrLstPhotoDetail.get(i).get("profile_picture"));
                                intentDetail.putExtra("arrDetailPhoto", mArrayServiceProvider);
                                intentDetail.putExtra("position", String.valueOf(i));
                                startActivity(intentDetail);
                            }
                        }
                    }
                });*/
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onMapSearch(View view) {
        EditText locationSearch = (EditText) mView.findViewById(R.id.editText);
        String location = locationSearch.getText().toString();
        List<Address> addressList = null;

        if (location != null || !location.equals("")) {
            Geocoder geocoder = new Geocoder(getActivity());
            try {
                addressList = geocoder.getFromLocationName(location, 1);

            } catch (IOException e) {
                e.printStackTrace();
            }
            Address address = addressList.get(0);
            LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
            mMap.addMarker(new MarkerOptions().position(latLng).title("Marker"));
            mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        }
    }

/*
    private void openDialog(ArrayList<HashMap<String, String>> mArrSameUser) {
        mDialog = new Dialog(getActivity());
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.dialog_marker);
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.getWindow().setLayout((LinearLayout.LayoutParams.WRAP_CONTENT), LinearLayout.LayoutParams.WRAP_CONTENT);
        mDialog.setCancelable(true);
        mDialog.show();

        mListView = (ListView) mDialog.findViewById(R.id.listview);

        setUserListAdapter();
    }
*/


    /**
     * get markers from location
     */
/*
    private void getUsersByLatlng(String userID, String userType, String mLat, String mLon) {
        mStrGetLatLong = getString(R.string.WS_HOST) + getString(R.string.WS_GET_USERS_BY_LATLNG);
        HashMap<String, Object> mParams = new HashMap<>();
        mParams.put("user_id", userID);
        mParams.put("user_type", userType);
        mParams.put("latitude", mLat);
        mParams.put("logitude", mLon);

        if (AvailableNetwork.isNetworkAvailable(getActivity())) {
            mAQuery.progress(mProgressDialog).ajax(mStrGetLatLong, mParams, JSONObject.class, mAjaxCallbackUsers);
        } else {
            showAlert(getString(R.string.str_network_error), -1);
        }
    }
*/

  /*  AjaxCallback<JSONObject> mAjaxCallbackUsers = new AjaxCallback<JSONObject>() {
        @Override
        public void callback(String url, JSONObject object, AjaxStatus status) {
            super.callback(url, object, status);
            if (object != null) {
                try {
                    JSONObject mJson = object.getJSONObject("Response");
                    if (url.equalsIgnoreCase(mStrGetLatLong)) {
                        if (mJson.getString("error_code").equalsIgnoreCase("0")) {
                            JSONArray arrayServiceProvider = mJson.getJSONArray("arr_providers_data");
                            mArrUsersByLatLon.clear();
                            for (int i = 0; i < arrayServiceProvider.length(); i++) {
                                JSONObject objectServiceProvider = arrayServiceProvider.getJSONObject(i);
                                HashMap<String, String> mHash = new HashMap<>();
                                mHash.put("user_id", objectServiceProvider.getString("user_id"));
                                mHash.put("user_type", objectServiceProvider.getString("user_type"));
                                mHash.put("user_name", objectServiceProvider.getString("user_name"));
                                mHash.put("user_email", objectServiceProvider.getString("user_email"));
                                mHash.put("profile_picture", objectServiceProvider.getString("profile_picture"));
                                mHash.put("first_name", objectServiceProvider.getString("first_name"));
                                mHash.put("last_name", objectServiceProvider.getString("last_name"));
                                mHash.put("category_name", objectServiceProvider.getString("category_name"));

                                mArrUsersByLatLon.add(mHash);
                            }
                            // setMapMarker(mArrayServiceProvider);
                            // setUserListAdapter();
                        } else {
                            showAlert(mJson.getString("msg"), -1);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                showAlert(getString(R.string.str_server_error), -1);
            }
        }
    };
*/
   /* private void setUserListAdapter() {
        AdapterUserByLatLong mAdapter = new AdapterUserByLatLong(getActivity(), posInterfaceUserByLatLong, mArrSameUser, marker);
        mListView.setAdapter(mAdapter);
    }*/

 /*   PositionInterface posInterfaceUserByLatLong = new PositionInterface() {
        @Override
        public void onClickPosition(int pos) {

        }

        @Override
        public void onClickPosition(int pos, int mPos, int flg) {
            if (flg == 1) {
                Intent intentDetail = new Intent(getActivity(), ActivityGetServiceUsersList.class);
                intentDetail.putExtra("user_id", mArrSameUser.get(mPos).get("user_id"));
                intentDetail.putExtra("position", mPos);
                startActivity(intentDetail);
            }
        }
    };*/
    public String formatNumber(int decimals, double number) {
        StringBuilder sb = new StringBuilder(decimals + 2);
        sb.append("#.");
        for (int i = 0; i < decimals; i++) {
            sb.append("0");
        }
        return new DecimalFormat(sb.toString()).format(number);
    }


    private void showAlert(String strMessage, final int flag) {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity(), R.style.MyAlertDialogStyle);
        mBuilder.setMessage(strMessage);
        mBuilder.setCancelable(false);
        mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });
        mBuilder.show();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.icsearch:
                if (mL_LayoutSearch.isShown()) {
                    mL_LayoutSearch.setVisibility(View.GONE);
                } else {
                    mL_LayoutSearch.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.icfilter:
                break;
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        mMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())).title("It's Me!"));


    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
