package com.p1024.convenientdetailer.Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.p1024.convenientdetailer.CustomViews.CustomEditText;
import com.p1024.convenientdetailer.CustomViews.CustomTextView;
import com.p1024.convenientdetailer.DataClass.ServiceListDataClass;
import com.p1024.convenientdetailer.R;

import java.util.ArrayList;

/**
 * Created by administrator on 2/15/18.
 */

public class ServiceExpandableHeigthAdapter extends BaseAdapter {
    Context mContext;
    ArrayList<ServiceListDataClass> mArrayService;
    LayoutInflater inflater;
    Viewholder mViewHolder;
    View.OnClickListener onClickListener;
    public ServiceExpandableHeigthAdapter(Context mContext, ArrayList<ServiceListDataClass> mArrayService, View.OnClickListener onClickListener) {
        this.mContext = mContext;
        this.mArrayService = mArrayService;
        this.inflater = LayoutInflater.from(mContext);
        this.onClickListener=onClickListener;
    }

    @Override
    public int getCount() {
        return mArrayService.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        mViewHolder=new Viewholder();
        if (convertView==null)
        {
           convertView=inflater.inflate(R.layout.single_item_service,null);
           mViewHolder.mTvServicename=convertView.findViewById(R.id.txtservicename);
           mViewHolder.mTvServicePrice=convertView.findViewById(R.id.serviceprice);
           mViewHolder.mImgDelete=convertView.findViewById(R.id.imgdelete);
            convertView.setTag(mViewHolder);

        }else {
            mViewHolder = (Viewholder) convertView.getTag();
        }
        mViewHolder.mTvServicename.setText(mArrayService.get(position).getmStrServicename());
        mViewHolder.mTvServicePrice.setText(mArrayService.get(position).getmStrPrice());
        mViewHolder.mImgDelete.setTag(""+position);
        mViewHolder.mImgDelete.setOnClickListener(onClickListener);
        return convertView;
    }

    class Viewholder {
         CustomTextView mTvServicename,mTvServicePrice;
         ImageView mImgDelete;
    }
}
