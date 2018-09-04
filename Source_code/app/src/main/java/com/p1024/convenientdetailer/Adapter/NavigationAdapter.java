package com.p1024.convenientdetailer.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.p1024.convenientdetailer.Interface.PositionInterface;
import com.p1024.convenientdetailer.R;


/**
 * Created by ubuntu on 21/7/16 5:32 PM com.panaceatek.p1033.Adapter.
 */
public class NavigationAdapter extends BaseAdapter {
    Context mContext;
    PositionInterface positionInterface;
    String[] mStrArrDataHeader;
    int[] mImageArray;
    LayoutInflater inflator;
    String flagForLogin = "";

    public NavigationAdapter(String[] mStrArrDataHeader, int ImaegArray[], Context mContext, PositionInterface positionInterface) {
        this.mContext = mContext;
        this.mImageArray = ImaegArray;
        this.mStrArrDataHeader = mStrArrDataHeader;
        this.positionInterface = positionInterface;
        inflator = LayoutInflater.from(mContext);

    }

    public NavigationAdapter(String[] mStrArrDataHeader, Context mContext, PositionInterface positionInterface, String flag) {
        this.mContext = mContext;
        this.mStrArrDataHeader = mStrArrDataHeader;
        this.positionInterface = positionInterface;
        inflator = LayoutInflater.from(mContext);
        this.flagForLogin = flag;

    }

    @Override
    public int getCount() {
        return mStrArrDataHeader.length;
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
        ViewHolder holder = new ViewHolder();
        if (convertView == null) {
            convertView = inflator.inflate(R.layout.single_list_item_guest_dashboard, parent, false);
            holder = new ViewHolder();
            holder.mTxtNames = (TextView) convertView.findViewById(R.id.txt_activity_single_group_dashboard);

            holder.mimageIcon = (ImageView) convertView.findViewById(R.id.img_activity_icon_slide);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (flagForLogin.equalsIgnoreCase("flagForLogin")) {
            holder.mimageIcon.setVisibility(View.INVISIBLE);
        } else if (mImageArray.length != 0) {
            holder.mimageIcon.setImageResource(mImageArray[position]);
        } else {
            holder.mimageIcon.setVisibility(View.INVISIBLE);
        }
        holder.mTxtNames.setText(mStrArrDataHeader[position]);
        // holder.mimageIcon.setImageResource(mStr);


        return convertView;
    }

    public class ViewHolder {
        TextView mTxtNames;
        ImageView imgIndicator;
        ImageView mimageIcon;
    }
}
