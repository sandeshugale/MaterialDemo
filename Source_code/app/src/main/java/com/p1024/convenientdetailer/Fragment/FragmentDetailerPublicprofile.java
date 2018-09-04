package com.p1024.convenientdetailer.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.p1024.convenientdetailer.Adapter.ServiceListAdapter;
import com.p1024.convenientdetailer.Adapter.ServiceRatingReviewAdapter;
import com.p1024.convenientdetailer.Adapter.ViewpagerImageWorkAdapter;
import com.p1024.convenientdetailer.CustomViews.CirclePageIndicator;
import com.p1024.convenientdetailer.DataClass.ServiceDataClass;
import com.p1024.convenientdetailer.R;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by administrator on 3/22/18.
 */

public class FragmentDetailerPublicprofile extends Fragment {
    private View mView;
    private ListView mLvServices,mLvRatingReviews;
    private ArrayList<ServiceDataClass>mArrayServiceList,mArrayReviewList,mArrayWorkImageList;
    private ViewPager mPager;
    private int[] mImageArray;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_detailerpublicprofile_layout, container, false);
        setUI();
        return mView;
    }

    /**
     * inflate All Views
     */
    private void setUI() {
        mLvServices = mView.findViewById(R.id.listviewservice);
        mArrayServiceList=new ArrayList<>();
        mArrayReviewList=new ArrayList<>();
        mArrayWorkImageList=new ArrayList<>();
        mLvRatingReviews=mView.findViewById(R.id.listrating);
        mImageArray=new int[]{R.drawable.place_holder,R.drawable.tempservice1,R.drawable.tempservice2};
        init();

        /**
         * Dummy Array Data.
         */
        for (int i=0;i<8;i++)
        {
            ServiceDataClass serviceDataClass=new ServiceDataClass();
            serviceDataClass.setmStrServicename("Washing"+i);
            serviceDataClass.setmStrServiceprice(""+(100+i));
            mArrayServiceList.add(serviceDataClass);
        }

        setServiceAdapter();
        /**
         * Dummy Array Data.
         */
        for (int j=0;j<8;j++)
        {
            ServiceDataClass ReviewerObject=new ServiceDataClass();
            ReviewerObject.setmStrReviewDescription("Dummy textDummy textDummyDummy textDummy text Dummy text Dummy text Dummy text");
            ReviewerObject.setmStrReviewStars(""+1+j);
            ReviewerObject.setmStrReviewerName("Customer Name"+j);
            mArrayReviewList.add(ReviewerObject);
        }
         setReviewAdapter();


    }

    private void setReviewAdapter() {
        ArrayList<ServiceDataClass>mArrayFinalreview=new ArrayList<>();
        for (int j=0;j<mArrayReviewList.size();j++)
        {
            if (j<2) {
                mArrayFinalreview.add(mArrayReviewList.get(j));
            }
        }
        ServiceRatingReviewAdapter ratingAdapter=new ServiceRatingReviewAdapter(getActivity(),mArrayFinalreview);
        mLvRatingReviews.setAdapter(ratingAdapter);
    }

    private void setServiceAdapter() {
        ArrayList<ServiceDataClass>mArrayFinal=new ArrayList<>();
        for (int j=0;j<mArrayServiceList.size();j++)
        {
            if (j<2) {
                mArrayFinal.add(mArrayServiceList.get(j));
            }
        }
        ServiceListAdapter serviceListAdapter=new ServiceListAdapter(getActivity(),mArrayFinal);
        mLvServices.setAdapter(serviceListAdapter);
    }
    private void init() {

        /**Viewpagar
         *
         */
        mPager = (ViewPager) mView.findViewById(R.id.pager);
        mPager.setAdapter(new ViewpagerImageWorkAdapter(getActivity(),mImageArray));

       // mPager.setAdapter(new SlidingImage_Adapter(MainActivity.this,ImagesArray));


        CirclePageIndicator indicator = (CirclePageIndicator)
                mView.findViewById(R.id.indicator);

        indicator.setViewPager(mPager);

        final float density = getResources().getDisplayMetrics().density;

//Set circle indicator radius
        indicator.setRadius(5 * density);

        NUM_PAGES =mImageArray.length;

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 3000, 3000);

        // Pager listener over indicator
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currentPage = position;

            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });

    }
}
