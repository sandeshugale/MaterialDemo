package com.p1024.convenientdetailer.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.p1024.convenientdetailer.CustomViews.CustomTextView;
import com.p1024.convenientdetailer.CustomViews.RoundedImageView;
import com.p1024.convenientdetailer.DataClass.ServiceDataClass;
import com.p1024.convenientdetailer.R;

import java.util.ArrayList;

/**
 * Created by administrator on 3/22/18.
 */

public class ServiceRatingReviewAdapter extends BaseAdapter {

    private ArrayList<ServiceDataClass> mList;
    private Context mContext;
    private LayoutInflater mInflater;

    public ServiceRatingReviewAdapter(Context mContext, ArrayList<ServiceDataClass> mList) {
        this.mContext = mContext;
        this.mList = mList;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder mHolder;
        if (convertView == null) {
            mHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.layout_singleratingreview_item, null);
            mHolder.mImgCustomProfile = convertView.findViewById(R.id.customerimg);
            mHolder.mTxtCustomerName = convertView.findViewById(R.id.customername);
            mHolder.mTxtCustomerDescription = convertView.findViewById(R.id.txtdescriptionrating);
            convertView.setTag(mHolder);
        }else {
            mHolder= (ViewHolder) convertView.getTag();
        }
        mHolder.mTxtCustomerName.setText(mList.get(position).getmStrReviewerName());
        mHolder.mTxtCustomerDescription.setText(mList.get(position).getmStrReviewDescription());

        return convertView;
    }

    private class ViewHolder {
        RoundedImageView mImgCustomProfile;
        CustomTextView mTxtCustomerName, mTxtCustomerDescription;
    }

}
