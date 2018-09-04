package com.p1024.convenientdetailer.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.p1024.convenientdetailer.Adapter.TemporyAdapter;
import com.p1024.convenientdetailer.R;


/**
 * Created by administrator on 3/8/18.
 */

public class FragmentAutoDashboard extends Fragment {
    private View mParentview;
    private ListView mListDetailerservice;
    private String[] mStrArrServicelist, mImgArrServicelist;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mParentview = inflater.inflate(R.layout.fragmentautodashboard, container, false);
        setUI();
        return mParentview;
    }

    private void setUI() {
        mStrArrServicelist=new String[]{"Washing","Quick Services","Wrapping","Detailing","Repairing","Cleaning"};
        mImgArrServicelist=new String[]{""+R.drawable.tempservice1,""+R.drawable.tempservice2,""+R.drawable.tempservice3,""+R.drawable.tempservice1,""+R.drawable.tempservice2,""+R.drawable.tempservice3};

        mListDetailerservice = mParentview.findViewById(R.id.mlistdetailerservice);
        TemporyAdapter temporyAdapter=new TemporyAdapter(getActivity(),mStrArrServicelist,mImgArrServicelist);
        mListDetailerservice.setAdapter(temporyAdapter);
    }
}
