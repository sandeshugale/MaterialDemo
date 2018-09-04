package com.p1024.convenientdetailer.Adapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;

import com.p1024.convenientdetailer.CustomViews.CustomTextView;
import com.p1024.convenientdetailer.R;

/**
 * Created by administrator on 3/8/18.
 */

public class TemporyAdapter extends BaseAdapter {
       Context context;
       String[] mStrArrServicelist,mImgArrServicelist;
       LayoutInflater layoutInflater;
    public TemporyAdapter(Context context, String[] mStrArrServicelist, String[] mImgArrServicelist){
        this.context=context;
        this.mStrArrServicelist=mStrArrServicelist;
        this.mImgArrServicelist=mImgArrServicelist;
        layoutInflater=LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mStrArrServicelist.length;
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
          convertView=layoutInflater.inflate(R.layout.singleitemdash,parent,false);
        CustomTextView customTextView=convertView.findViewById(R.id.txtserviceName);
        customTextView.setText(mStrArrServicelist[position].toString());
        RelativeLayout relativeLayout=convertView.findViewById(R.id.mbacklayout);
        relativeLayout.setBackgroundResource(Integer.parseInt(mImgArrServicelist[position]));
        return convertView;
    }
}
