package com.p1024.convenientdetailer.Activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.media.MediaCas;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.service.textservice.SpellCheckerService;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.p1024.convenientdetailer.Adapter.NavigationAdapter;
import com.p1024.convenientdetailer.CustomViews.CustomTextView;
import com.p1024.convenientdetailer.CustomViews.TransparentProgressDialog;
import com.p1024.convenientdetailer.Fragment.FragmentAutoDashboard;
import com.p1024.convenientdetailer.Fragment.FragmentAutodetailer_registration;
import com.p1024.convenientdetailer.Fragment.FragmentBookMyAppointment;
import com.p1024.convenientdetailer.Fragment.FragmentDetailerPublicprofile;
import com.p1024.convenientdetailer.Fragment.FragmentMap;
import com.p1024.convenientdetailer.Fragment.Fragment_Customer_Registration;
import com.p1024.convenientdetailer.Fragment.Fragment_Supplier_Registration;
import com.p1024.convenientdetailer.Fragment.LoginFragment;
import com.p1024.convenientdetailer.Interface.PositionInterface;
import com.p1024.convenientdetailer.R;
import com.p1024.convenientdetailer.Session.SessionClass;
import com.p1024.convenientdetailer.Session.SessionConstants;

import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;


public class MainDashboard extends FragmentActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private ImageView mImageSideMenu;
    private DrawerLayout mDrawerSidebar;
    private ListView mListViewGuest;
    private ImageView mImageBack;
    AQuery mAQuery;
    Intent intent;
    private String mStrCurrentflag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Recreated", "Created");
        /*requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
*/
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setContentView(R.layout.main_dashboard);
        setUI();
        intent = getIntent();
        /*FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, new FragmentMap()).commit();*/


        prepareMenu();

        mDrawerSidebar.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
    }

    public static void hideSoftKeyboard(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
    }

    @Override
    public void onBackPressed() {

        //Toast.makeText(MainDashboard.this,""+getFragmentManager().getBackStackEntryCount(),Toast.LENGTH_LONG).show();
        if (mDrawerSidebar.isDrawerOpen(Gravity.RIGHT)) {
            mDrawerSidebar.closeDrawers();
        } else if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            super.onBackPressed();
        } else {
            AlertDialog.Builder sAlertDialog = new AlertDialog.Builder(this);
            sAlertDialog.setMessage("Do you want to exit?");
            sAlertDialog.setCancelable(false);
            sAlertDialog.setIcon(android.R.drawable.ic_dialog_alert);
            sAlertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub
                    dialog.dismiss();
                    finish();
                }
            });
            sAlertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub
                    dialog.dismiss();
                }
            });
            sAlertDialog.create();

            try {
                sAlertDialog.show();
            } catch (Exception e) {
                e.printStackTrace();
            }


        }

    }

    private void setUI() {
        mImageBack = (ImageView) findViewById(R.id.img_back);
        mAQuery = new AQuery(this);
        mImageSideMenu = (ImageView) findViewById(R.id.img_menu);
        mDrawerSidebar = (DrawerLayout) findViewById(R.id.maindashboard_drawer_lay);
        mListViewGuest = (ListView) findViewById(R.id.list_main_dashboard);
        setListener();
       /* Fragment fragment;
        Bundle bundle = new Bundle();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        fragment = new LoginFragment();
        ft.replace(R.id.content_frame, fragment, "Login").commit();*/
        mDrawerSidebar.setClickable(false);
        getKeyHash();

    }

    private void getKeyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("Keyhash:",
                        Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
        } catch (NoSuchAlgorithmException e) {
        }
    }

    private void setListener() {
        mListViewGuest.setOnItemClickListener(this);
        mImageSideMenu.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Fragment fragment;
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Bundle bundle;

        switch (v.getId()) {
            case R.id.img_menu:
                hideSoftKeyboard(MainDashboard.this);
                mDrawerSidebar.openDrawer(Gravity.LEFT);
                break;

        }
    }

    private void prepareMenu() {
        /**
         * User Type:1 for admin, 2 for Customer,3 for AutoDetailer,4 for Supplier
         */
        if (SessionClass.getInstance().getValue(getApplicationContext(), SessionConstants.KEY_USER_TYPE).equalsIgnoreCase("2")) {
            String[] mStrArrDataHeader = getResources().getStringArray(R.array.str_customer);
            mListViewGuest.setAdapter(new NavigationAdapter(mStrArrDataHeader, MainDashboard.this, positionInterface, "flagForLogin"));
        } else if (SessionClass.getInstance().getValue(getApplicationContext(), SessionConstants.KEY_USER_TYPE).equalsIgnoreCase("3")) {
            String[] mStrArrDataHeader = getResources().getStringArray(R.array.str_detailer_or_autodetailer);
            mListViewGuest.setAdapter(new NavigationAdapter(mStrArrDataHeader, MainDashboard.this, positionInterface, "flagForLogin"));
        } else if (SessionClass.getInstance().getValue(getApplicationContext(), SessionConstants.KEY_USER_TYPE).equalsIgnoreCase("4")) {
            String[] mStrArrDataHeader = getResources().getStringArray(R.array.str_arr_supplier);
            mListViewGuest.setAdapter(new NavigationAdapter(mStrArrDataHeader, MainDashboard.this, positionInterface, "flagForLogin"));
        } else {
            String[] mStrArrDataHeader = getResources().getStringArray(R.array.str_arr_sign_up_for_free);
            mListViewGuest.setAdapter(new NavigationAdapter(mStrArrDataHeader, MainDashboard.this, positionInterface, "flagForLogin"));
        }
        FragmentManager fragmentManager = getSupportFragmentManager();

        /**
         * Open initial fragment as Compare to User type
         */
        if (!SessionClass.getInstance().getValue(getApplicationContext(), SessionConstants.KEY_USER_ID).equalsIgnoreCase("")) {
            if (SessionClass.getInstance().getValue(getApplicationContext(), SessionConstants.KEY_USER_TYPE).equalsIgnoreCase("2")) {
                mStrCurrentflag = "customer";
                fragmentManager.beginTransaction().replace(R.id.content_frame, new FragmentMap()).commit();
            } else if ((SessionClass.getInstance().getValue(getApplicationContext(), SessionConstants.KEY_USER_TYPE).equalsIgnoreCase("3"))) {
                fragmentManager.beginTransaction().replace(R.id.content_frame, new FragmentAutoDashboard()).commit();
                mStrCurrentflag = "Detailer";
            } else if ((SessionClass.getInstance().getValue(getApplicationContext(), SessionConstants.KEY_USER_TYPE).equalsIgnoreCase("4"))) {
                fragmentManager.beginTransaction().replace(R.id.content_frame, new FragmentAutoDashboard()).commit();
                mStrCurrentflag = "Supplier";
            }
            // prepareMenu();
        } else {
            mStrCurrentflag = "Login";
            fragmentManager.beginTransaction().replace(R.id.content_frame, new LoginFragment(), "Login").commit();
            //prepareMenu();
        }

    }

    /**
     * Asynck task for background web service call
     * call this method in Onresume()
     */
    public void callAsynchronousTask() {
        final Handler handler = new Handler();
        Timer timer = new Timer();
        final TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {

                        try {

                            PerformBackgroundTask performBackgroundTask = new PerformBackgroundTask();
                            // PerformBackgroundTask this class is the class that extends AsynchTask
                            performBackgroundTask.execute();
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                        }
                    }
                });
            }
        };
        timer.schedule(doAsynchronousTask, 0, 10000); //execute in every 10000 ms
    }

    /**
     * perform background task
     */
    public class PerformBackgroundTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            //  Activity activity = getActivity();

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

        }
    }


    PositionInterface positionInterface = new PositionInterface() {
        @Override
        public void onClickPosition(int pos) {

        }

       /* @Override
        public void onClickPosition(int pos, int flg) {

        }*/

        @Override
        public void onClickPosition(int pos, int flg, int mOtherFlag) {

        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        mImageBack.setVisibility(View.GONE);
       /* callAsynchronousTask();*/

    }

    private void switchToNextScreen(int flag) {
        Fragment fragment;
        Bundle bundle = new Bundle();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if (SessionClass.getInstance().getValue(getApplicationContext(), SessionConstants.KEY_USER_TYPE).equalsIgnoreCase("")) {
            /**
             * For Without Login Side Menu Click.
             */
            if (flag == 0) {
                fragment = new LoginFragment();
                ft.replace(R.id.content_frame, fragment, "Login").addToBackStack("").commit();
            } else if (flag == 1) {
                fragment = new Fragment_Customer_Registration();
                ft.replace(R.id.content_frame, fragment, "CustomerRegistration").addToBackStack("").commit();
            } else if (flag == 2) {
                fragment = new FragmentAutodetailer_registration();
                ft.replace(R.id.content_frame, fragment, "AutodetailerRegistration").addToBackStack("").commit();
            } else if (flag == 3) {
                fragment = new Fragment_Supplier_Registration();
                ft.replace(R.id.content_frame, fragment, "SupplierRegistration").addToBackStack("").commit();
            }
        } else if (SessionClass.getInstance().getValue(getApplicationContext(), SessionConstants.KEY_USER_TYPE).equalsIgnoreCase("2")) {
            /**
             * For Customer Side Menu Click.
             */
            if (flag == 0) {
                fragment = new FragmentMap();
                ft.replace(R.id.content_frame, fragment, "Login").addToBackStack("").commit();
            } else if (flag == 1) {
                Toast.makeText(getApplicationContext(), "Functionality Under Development.", Toast.LENGTH_SHORT).show();

            } else if (flag == 2) {
                Toast.makeText(getApplicationContext(), "Functionality Under Development.", Toast.LENGTH_SHORT).show();
               /* fragment = new FragmentBookMyAppointment();
                ft.replace(R.id.content_frame, fragment, "").addToBackStack("").commit();*/
            }else if (flag == 3) {
                Toast.makeText(getApplicationContext(), "Functionality Under Development.", Toast.LENGTH_SHORT).show();

            }
            else if (flag == 4) {
                Toast.makeText(getApplicationContext(), "Functionality Under Development.", Toast.LENGTH_SHORT).show();

            }else if (flag == 5) {
                Toast.makeText(getApplicationContext(), "Functionality Under Development.", Toast.LENGTH_SHORT).show();

            }else if (flag == 6) {
                Toast.makeText(getApplicationContext(), "Functionality Under Development.", Toast.LENGTH_SHORT).show();

            }else if (flag == 7) {
                dialogueLogOut();
            }

        } else if (SessionClass.getInstance().getValue(getApplicationContext(), SessionConstants.KEY_USER_TYPE).equalsIgnoreCase("3")) {
            /**
             * For Detailer Side Menu Click.
             */
            if (flag == 10) {
                dialogueLogOut();
            }else if (flag==0)
            {
                //Toast.makeText(getApplicationContext(), "Functionality Under Development.", Toast.LENGTH_SHORT).show();

            }
            else if (flag==1)
            {
                Toast.makeText(getApplicationContext(), "Functionality Under Development.", Toast.LENGTH_SHORT).show();

            }else if (flag==2)
            {
                Toast.makeText(getApplicationContext(), "Functionality Under Development.", Toast.LENGTH_SHORT).show();

            }else if (flag==3)
            {
                Toast.makeText(getApplicationContext(), "Functionality Under Development.", Toast.LENGTH_SHORT).show();

            }else if (flag==4)
            {
                Toast.makeText(getApplicationContext(), "Functionality Under Development.", Toast.LENGTH_SHORT).show();

            }else if (flag==5)
            {
                Toast.makeText(getApplicationContext(), "Functionality Under Development.", Toast.LENGTH_SHORT).show();

            }else if (flag==6)
            {
                Toast.makeText(getApplicationContext(), "Functionality Under Development.", Toast.LENGTH_SHORT).show();

            }else if (flag==7)
            {
                Toast.makeText(getApplicationContext(), "Functionality Under Development.", Toast.LENGTH_SHORT).show();

            }else if (flag==8)
            {
                Toast.makeText(getApplicationContext(), "Functionality Under Development.", Toast.LENGTH_SHORT).show();

            }else if (flag==9)
            {
                Toast.makeText(getApplicationContext(), "Functionality Under Development.", Toast.LENGTH_SHORT).show();

            }

        } else if (SessionClass.getInstance().getValue(getApplicationContext(), SessionConstants.KEY_USER_TYPE).equalsIgnoreCase("4")) {
            /**
             * For Supplier Side Menu Click.
             */
            if (flag == 4) {
                dialogueLogOut();
            }else if (flag==0)
            {
                Toast.makeText(getApplicationContext(), "Functionality Under Development.", Toast.LENGTH_SHORT).show();

            }else if (flag==1)
            {
                Toast.makeText(getApplicationContext(), "Functionality Under Development.", Toast.LENGTH_SHORT).show();

            }else if (flag==2)
            {
                Toast.makeText(getApplicationContext(), "Functionality Under Development.", Toast.LENGTH_SHORT).show();

            }else if (flag==3)
            {
                Toast.makeText(getApplicationContext(), "Functionality Under Development.", Toast.LENGTH_SHORT).show();

            }
        }
    }


    private void switchToLogin() {
        Intent intent = new Intent(this, MainDashboard.class);
        startActivity(intent);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position)

        {

            case 0:
                switchToNextScreen(0);
                mDrawerSidebar.closeDrawers();
                break;
            case 1:
                switchToNextScreen(1);
                //Toast.makeText(this, "Functionality under development", Toast.LENGTH_SHORT).show();
                mDrawerSidebar.closeDrawers();

                break;
            case 2:

                // flag=2;
                switchToNextScreen(2);
                //Toast.makeText(this,"Functionality under development",Toast.LENGTH_SHORT).show();
                mDrawerSidebar.closeDrawers();
                break;
            case 3:
                switchToNextScreen(3);
                //Toast.makeText(this,"Functionality under development",Toast.LENGTH_SHORT).show();
                mDrawerSidebar.closeDrawers();
                break;
            case 4:
                switchToNextScreen(4);
                //Toast.makeText(this,"Functionality under development",Toast.LENGTH_SHORT).show();
                mDrawerSidebar.closeDrawers();

                break;
            case 5:
                 switchToNextScreen(5);
                mDrawerSidebar.closeDrawers();
                break;
            case 6:
                switchToNextScreen(6);
                mDrawerSidebar.closeDrawers();
                break;
            case 7:
                switchToNextScreen(7);
                mDrawerSidebar.closeDrawers();
                break;
            case 8:
                switchToNextScreen(8);
                mDrawerSidebar.closeDrawers();
                break;
            case 9:
                switchToNextScreen(9);
                mDrawerSidebar.closeDrawers();
                break;
            case 10:
                mDrawerSidebar.closeDrawers();
                switchToNextScreen(10);
                break;
            case 11:
                mDrawerSidebar.closeDrawers();
                switchToNextScreen(11);
                break;
            case 12:

                mDrawerSidebar.closeDrawers();
                switchToNextScreen(12);
                break;
            case 13:

                mDrawerSidebar.closeDrawers();
                switchToNextScreen(13);
                break;


        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment currentFragment = fragmentManager.findFragmentById(R.id.content_frame);
        if (currentFragment.getTag().equals("Login")) {
            ((Fragment) getSupportFragmentManager().findFragmentByTag("Login")).onActivityResult(requestCode, resultCode, data);
        } else if (currentFragment.getTag().equals("CustomerRegistration")) {
            ((Fragment) getSupportFragmentManager().findFragmentByTag("CustomerRegistration")).onActivityResult(requestCode, resultCode, data);
        } else if (currentFragment.getTag().equals("AutodetailerRegistration")) {
            ((Fragment) getSupportFragmentManager().findFragmentByTag("AutodetailerRegistration")).onActivityResult(requestCode, resultCode, data);
        }else if (currentFragment.getTag().equals("SupplierRegistration")) {
            ((Fragment) getSupportFragmentManager().findFragmentByTag("SupplierRegistration")).onActivityResult(requestCode, resultCode, data);
        }

    }

    public void dialogueLogOut() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainDashboard.this);
        mBuilder.setMessage("Do you want to Logout..?");
        mBuilder.setCancelable(false);
        mBuilder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SessionClass.getInstance().sessionLogout(getApplicationContext());
                finish();
                switchToLogin();
            }
        });
        mBuilder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        mBuilder.create();
        mBuilder.show();
    }

}
