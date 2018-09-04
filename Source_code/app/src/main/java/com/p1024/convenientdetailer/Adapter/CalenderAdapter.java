package com.p1024.convenientdetailer.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.p1024.convenientdetailer.CustomViews.CustomTextView;
import com.p1024.convenientdetailer.DataClass.CalenderDate;
import com.p1024.convenientdetailer.Interface.interfaceOnClickListView;
import com.p1024.convenientdetailer.R;

import java.util.ArrayList;


/**
 * Created by ubuntu on 20/3/17.
 */

public class CalenderAdapter extends RecyclerView.Adapter<CalenderAdapter.MyViewHolder> {

    private ArrayList<CalenderDate> mArrListCalenderDate;
    private LayoutInflater mInflater;
    private Context mContext;
    private interfaceOnClickListView poistionInterFace;

    public CalenderAdapter(Context mContext, ArrayList<CalenderDate> mArrListCalenderDate, interfaceOnClickListView poistionInterFace) {
        this.mContext = mContext;
        this.mArrListCalenderDate = mArrListCalenderDate;
        this.poistionInterFace = poistionInterFace;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.layout_single_manage_availablity_date, null);
        /*View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_single_manage_availablity_date, parent, false);*/

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.mTxtDate.setText(mArrListCalenderDate.get(position).getmStrDate());
        ;
        holder.mTxtDay.setText(mArrListCalenderDate.get(position).getmStrDay());
        ;

        holder.mImgBookingAvailable.setVisibility(View.GONE);
        holder.mTxtDate.setTextColor(mContext.getResources().getColor(R.color.clr_grey));
        holder.mTxtDay.setTextColor(mContext.getResources().getColor(R.color.clr_grey));

        if (mArrListCalenderDate.get(position).isClickOnTheView()) {
            holder.mTxtDate.setTextColor(mContext.getResources().getColor(R.color.clr_white));
            holder.mTxtDate.setBackgroundResource(R.drawable.redcircledrawable);
            holder.mTxtDay.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
        }else {
            holder.mTxtDate.setBackgroundResource(0);
        }

        if (mArrListCalenderDate.get(position).isBookingAvailable()) {
            holder.mImgBookingAvailable.setVisibility(View.VISIBLE);
        } else {
            holder.mImgBookingAvailable.setVisibility(View.GONE);
        }

        holder.mLayoutCalenderDate.setTag("" + position);
        holder.mLayoutCalenderDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                poistionInterFace.onClickListView(position, true, 1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mArrListCalenderDate.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public CustomTextView mTxtDate, mTxtDay;
        public LinearLayout mLayoutCalenderDate;
        public ImageView mImgBookingAvailable,mImgSelector;

        public MyViewHolder(View view) {
            super(view);
            mTxtDate = (CustomTextView) view.findViewById(R.id.txt_lsmad_date);
            mTxtDay = (CustomTextView) view.findViewById(R.id.txt_lsmad_day);
            mLayoutCalenderDate = (LinearLayout) view.findViewById(R.id.layout_calender_date);
            mImgBookingAvailable = (ImageView) view.findViewById(R.id.view_date);
           // mImgSelector= (ImageView) view.findViewById(R.id.selector);
        }
    }

}

