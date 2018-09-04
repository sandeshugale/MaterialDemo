package com.p1024.convenientdetailer.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.p1024.convenientdetailer.CustomViews.CustomTextView;
import com.p1024.convenientdetailer.CustomViews.RoundedImageView;
import com.p1024.convenientdetailer.DataClass.ServiceDataClass;
import com.p1024.convenientdetailer.DataClass.ServiceListDataClass;
import com.p1024.convenientdetailer.R;

import java.util.ArrayList;

/**
 * Created by administrator on 3/22/18.
 */

public class ServiceListAdapter extends BaseAdapter {

    private ArrayList<ServiceDataClass> mList;
    private Context mContext;
    private LayoutInflater mInflater;

    public ServiceListAdapter(Context mContext, ArrayList<ServiceDataClass> mList) {
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
            convertView = mInflater.inflate(R.layout.layout_singleservice_item, null);
            mHolder.mImgService = convertView.findViewById(R.id.serviceimg);
            mHolder.mTxtServicename = convertView.findViewById(R.id.txtsname);
            mHolder.mTxtServiceprice = convertView.findViewById(R.id.txtserviceprice);
            convertView.setTag(mHolder);
        }else {
            mHolder= (ViewHolder) convertView.getTag();
        }
        mHolder.mTxtServicename.setText(mList.get(position).getmStrServicename());
        mHolder.mTxtServiceprice.setText(mList.get(position).getmStrServiceprice());

        return convertView;
    }

    private class ViewHolder {
        RoundedImageView mImgService;
        CustomTextView mTxtServicename, mTxtServiceprice;
    }

}
