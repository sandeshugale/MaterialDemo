package com.p1024.convenientdetailer.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;


import com.p1024.convenientdetailer.CustomViews.CustomTextView;
import com.p1024.convenientdetailer.DataClass.DetailerClass;
import com.p1024.convenientdetailer.R;

import java.util.ArrayList;

/**
 * Created by android on 9/9/16.
 */
public class SpinnerAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    ArrayList<DetailerClass> mArrList;

    public SpinnerAdapter(Context mContext, ArrayList<DetailerClass> mList){
        this.mContext=mContext;
        this.mArrList=mList;
        mInflater= (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE );
    }
    @Override
    public int getCount() {
        return mArrList.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        final ViewHolder mHolder;

        if (convertView == null) {
            mHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.activity_single_spinner_item, null);

            mHolder.mTxtSpinnerItem = (CustomTextView) convertView.findViewById(R.id.txt_single_spinner_item);
            convertView.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) convertView.getTag();
        }
      //  mHolder.mTxtSpinnerItem.setTextColor(mContext.getResources().getColor(R.color.font_color));
        mHolder.mTxtSpinnerItem.setText(mArrList.get(i).getmStrServiceCategoryName());
        return convertView;
    }
    private class ViewHolder{
        CustomTextView mTxtSpinnerItem;
    }
}
