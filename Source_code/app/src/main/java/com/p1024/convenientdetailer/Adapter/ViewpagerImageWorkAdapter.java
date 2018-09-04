package com.p1024.convenientdetailer.Adapter;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.androidquery.AQuery;
import com.p1024.convenientdetailer.R;

/**
 * Created by ubuntu on 1/8/16 3:51 PM com.p1034.Adapters.
 */
public class ViewpagerImageWorkAdapter extends PagerAdapter implements View.OnClickListener {



    private  int[] mArrayListNews;
    //private ArrayList<ProductDetailData> IMAGES;
    private LayoutInflater inflater;
    private Context context;
    private AQuery mAQuery;
    private View.OnClickListener listener;


    public ViewpagerImageWorkAdapter(Context context, int[] mArrayListNews) {
        this.context = context;
        this.mArrayListNews = mArrayListNews;
        inflater = LayoutInflater.from(context);
        mAQuery = new AQuery(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }

    @Override
    public int getCount() {
        return mArrayListNews.length;
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View itemView = inflater.inflate(R.layout.viewpagersingle_layout, view, false);

        ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);
        imageView.setImageResource(mArrayListNews[position]);

        view.addView(itemView);

        return itemView;
       /* View imageLayout = inflater.inflate(R.layout.viewpagersingle_layout, view, false);
        assert imageLayout!=null;
        final ImageView imageView = (ImageView) imageLayout
                    .findViewById(R.id.img_profile);
        imageView.setImageResource(mArrayListNews[position]);

        view.addView(imageLayout);

        return imageLayout;*/


    }



    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }


    @Override
    public void onClick(View v) {
    switch (v.getId()){

    }

    }
}

/*class CustomPagerAdapter extends PagerAdapter {

    Context mContext;
    LayoutInflater mLayoutInflater;

    public CustomPagerAdapter(Context context) {
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mResources.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.pager_item, container, false);

        ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);
        imageView.setImageResource(mResources[position]);

        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}*/


