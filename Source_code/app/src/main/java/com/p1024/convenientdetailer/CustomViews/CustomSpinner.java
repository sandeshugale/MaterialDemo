package com.p1024.convenientdetailer.CustomViews;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Spinner;

/**
 * Created by administrator on 1/31/18.
 */

public class CustomSpinner extends Spinner {
    int prevPos=0;
   /* public CustomSpinner(Context context)
        { super(context); }

        public CustomSpinner(Context context, AttributeSet attrs)
        { super(context, attrs); }

        public CustomSpinner(Context context, AttributeSet attrs, int defStyle)
        { super(context, attrs, defStyle); }

        @Override public void
        setSelection(int position, boolean animate)
        {
            boolean sameSelected = position == getSelectedItemPosition();
            super.setSelection(position, animate);
            if (sameSelected) {
                // Spinner does not call the OnItemSelectedListener if the same item is selected, so do it manually now
                getOnItemSelectedListener().onItemSelected(this, getSelectedView(), position, getSelectedItemId());
            }
        }

        @Override public void
        setSelection(int position)
        {
            boolean sameSelected = position == getSelectedItemPosition();
            super.setSelection(position);
            if (sameSelected) {
                // Spinner does not call the OnItemSelectedListener if the same item is selected, so do it manually now
                getOnItemSelectedListener().onItemSelected(this, getSelectedView(), position, getSelectedItemId());
            }
        }*/

       OnItemSelectedListener listener;

       public CustomSpinner(Context context, AttributeSet attrs) {
           super(context, attrs);
       }

       @Override
       public void setSelection(int position) {
           super.setSelection(position);
           if (listener != null && prevPos==position)
               getOnItemSelectedListener().onItemSelected(null, null, position, 0);
       }

       public void setOnItemSelectedEvenIfUnchangedListener(
               OnItemSelectedListener listener) {
           this.listener = listener;
       }


}
