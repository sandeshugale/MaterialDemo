package com.p1024.convenientdetailer.CustomViews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.androidquery.AQuery;

import java.util.ArrayList;

public class CustomBaseAdapter extends BaseAdapter {
    protected LayoutInflater mInflater;
    protected AQuery mAQuery;
    protected Context mContext;
    protected ArrayList<?> mArrList;

    public CustomBaseAdapter(Context mContext, ArrayList<?> mArrList) {
        this.mArrList = mArrList;
        this.mContext = mContext;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mAQuery = new AQuery(mContext);
    }

    @Override
    public int getCount() {
        return mArrList.size();
    }

    @Override
    public Object getItem(int position) {
        return mArrList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return convertView;
    }
}
