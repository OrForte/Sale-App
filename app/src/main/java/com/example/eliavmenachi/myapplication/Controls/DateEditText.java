package com.example.eliavmenachi.myapplication.Controls;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;

import java.util.Calendar;

public class DateEditText extends EditText {
    int year;
    int month;
    int day;

    public void setDate(int y, int m, int d) {
        year = y;
        month = m;
        day = d;
    }

    private void readDate() {
        String txt = getText().toString();
        String[] arr = txt.split("/");
        if (arr.length == 3) {
            day = Integer.parseInt(arr[0]);
            month = Integer.parseInt(arr[1]);
            year = Integer.parseInt(arr[2]);
        }
    }

    public DateEditText(Context context) {
        super(context);
    }

    public DateEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DateEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            DateDialogFragment fragment = new DateDialogFragment();
            readDate();
            fragment.setDate(year, month, day);

            final Calendar c = Calendar.getInstance();
            int mFYear = c.get(Calendar.YEAR);
            int mFMonth = c.get(Calendar.MONTH) + 1;
            int mFDay = c.get(Calendar.DAY_OF_MONTH);
            fragment.day = mFDay;
            fragment.month = mFMonth;
            fragment.year = mFYear;
            fragment.listener = new DateDialogFragment.DateDialogFragmentListener() {
                @Override
                public void onDateSet(int y, int m, int d) {
                    setText("" + d + "/" + (m + 1) + "/" + y);
                }
            };
            fragment.show(((Activity) getContext()).getFragmentManager(), "");
        }
        return true;
    }
}
