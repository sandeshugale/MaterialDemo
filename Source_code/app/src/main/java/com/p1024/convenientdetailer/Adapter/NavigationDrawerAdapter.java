package com.p1024.convenientdetailer.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.p1024.convenientdetailer.CustomViews.CustomTextView;
import com.p1024.convenientdetailer.DataClass.DataNavigation;
import com.p1024.convenientdetailer.R;

import java.util.ArrayList;

/**
 * Created by ubuntu on 8/12/17.
 */

public class NavigationDrawerAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<DataNavigation> mArrListNavItem;
    private LayoutInflater mInflater;
    //private AQuery mAquery;

    public NavigationDrawerAdapter(Context mContext, ArrayList<DataNavigation> mArrListNavItem) {
        this.mContext = mContext;
        this.mArrListNavItem = mArrListNavItem;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // mAquery = new AQuery(mContext);
    }

    @Override
    public int getCount() {
        return mArrListNavItem.size();
    }

    @Override
    public Object getItem(int position) {
        return mArrListNavItem.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder mViewHolder;
        if (convertView == null) {
            mViewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.activity_drawer_item, null);
            mViewHolder.mImgMenu = (ImageView) convertView.findViewById(R.id.img_nav_item_menu);
            mViewHolder.mTxtMenuName = (CustomTextView) convertView.findViewById(R.id.tv_nav_item_title);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        mViewHolder.mImgMenu.setVisibility(View.VISIBLE);

        mViewHolder.mTxtMenuName.setText(mArrListNavItem.get(position).getMstrNavTitle().trim());
        mViewHolder.mImgMenu.setImageResource(mArrListNavItem.get(position).getmStrNavIcon());
        return convertView;
    }

    private class ViewHolder {
        ImageView mImgMenu;
        CustomTextView mTxtMenuName, mTxtCount;
    }
}
