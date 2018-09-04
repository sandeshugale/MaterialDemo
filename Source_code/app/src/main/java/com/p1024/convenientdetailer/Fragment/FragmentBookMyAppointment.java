package com.p1024.convenientdetailer.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.p1024.convenientdetailer.Adapter.CalenderAdapter;
import com.p1024.convenientdetailer.CustomViews.CustomTextView;
import com.p1024.convenientdetailer.DataClass.CalenderDate;
import com.p1024.convenientdetailer.Interface.interfaceOnClickListView;
import com.p1024.convenientdetailer.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by administrator on 3/12/18.
 */

public class FragmentBookMyAppointment extends Fragment {
 //   private View mParentview;
    private View mView;
    LinearLayout mLayoutCalendar;
    CustomTextView mTxtCurrentDate,mTxtDay;
    SimpleDateFormat smipleDateFormat;
    CustomTextView mTxtHeader;
    private ArrayList<CalenderDate> mArrListCalenderDate;
    RecyclerView mRecyclarViewDate;
    CalenderAdapter mCalenderAdapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView=inflater.inflate(R.layout.layoutbookappointment,container,false);
        setUI();
        return mView;
    }

    private void setUI()
    {
     //   mTxtHeader = (CustomTextView) getActivity().findViewById(R.id.txt_header);

        mLayoutCalendar=(LinearLayout)mView.findViewById(R.id.layout_calendar);
        mTxtCurrentDate=(CustomTextView) mView.findViewById(R.id.txt_custcurrentdate);
        mTxtCurrentDate.setVisibility(View.GONE);
        mTxtDay=(CustomTextView) mView.findViewById(R.id.txt_custmsg_currdate);
        mTxtDay.setVisibility(View.GONE);
        mRecyclarViewDate= (RecyclerView) mView.findViewById(R.id.horizontal_recycler_view);
        mArrListCalenderDate=new ArrayList<>();
        setDates();
        LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        mRecyclarViewDate.setLayoutManager(horizontalLayoutManagaer);
        mCalenderAdapter=new CalenderAdapter(getActivity(),mArrListCalenderDate,mPosInterfaceOnClickListView);
        //   setDates();
        mRecyclarViewDate.setAdapter(mCalenderAdapter);
    }

    /**
     * ***** SET-CALENDAR-DATE
     */

    private void setDates() {
        try {
            Calendar mCalendar = Calendar.getInstance();
            mCalendar.add(Calendar.DAY_OF_YEAR,-31);
            smipleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            mArrListCalenderDate.clear();
            for(int i=0;i<60;i++){
                mCalendar.add(Calendar.DAY_OF_YEAR, 1);
                String date = smipleDateFormat.format(mCalendar.getTime());
                Date dateObj = smipleDateFormat.parse(date);
                SimpleDateFormat sdf = new SimpleDateFormat("EEE");
                String day=sdf.format(dateObj);
                CalenderDate item =new CalenderDate();
                String[] items1 = date.split("-");
                String year=items1[2];
                item.setmStrDate(year);
                item.setmStrDay(day);
                item.setmStrFormatedDate(date);
                mArrListCalenderDate.add(item);
            }

            mCalenderAdapter.notifyDataSetChanged();
            // getWeeklyJob();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static String getMonth(String date) throws ParseException {
        Date d = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(date);
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        String monthName = new SimpleDateFormat("MMMM").format(cal.getTime());
        return monthName;
    }
    private static String getYear(String date) throws ParseException {
        Date d = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(date);
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        String year = new SimpleDateFormat("yyyy").format(cal.getTime());
        return year;
    }


    private static String getDay(String mStrDate)throws ParseException {
        Date d = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(mStrDate);
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        String dayName = new SimpleDateFormat("EEEE").format(cal.getTime());
        return dayName;
    }
    /**
     * ***** INTERFACE-CALENDAR
     */
    interfaceOnClickListView mPosInterfaceOnClickListView=new interfaceOnClickListView() {
        @Override
        public void onClickListView(int position, boolean b, int flg) {
            try {
                for(int i=0;i<mArrListCalenderDate.size();i++){
                    mArrListCalenderDate.get(i).setClickOnTheView(false);
                }
                mArrListCalenderDate.get(position).setClickOnTheView(true);
                //mArrListCalenderDate.get(position).setBookingAvailable(true);
                String day=getDay(mArrListCalenderDate.get(position).getmStrFormatedDate());
                if (mArrListCalenderDate.get(position).getmStrFormatedDate().equalsIgnoreCase(mArrListCalenderDate.get(30).getmStrFormatedDate())){
                    mTxtDay.setText("Today");
                }else {
                    mTxtDay.setText(day);
                }
                String month=getMonth(mArrListCalenderDate.get(position).getmStrFormatedDate());
                String year=getYear(mArrListCalenderDate.get(position).getmStrFormatedDate());
                mTxtCurrentDate.setText(month+" "+year);
              //  mTxtHeader.setText(month);
                Toast.makeText(getActivity(), ""+mArrListCalenderDate.get(position).getmStrFormatedDate(), Toast.LENGTH_SHORT).show();
                mCalenderAdapter.notifyDataSetChanged();
                // getDateBooking(mArrListCalenderDate.get(position).getmStrFormatedDate());
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    };
}
