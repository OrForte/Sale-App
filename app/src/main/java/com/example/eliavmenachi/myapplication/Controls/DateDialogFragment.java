package com.example.eliavmenachi.myapplication.Controls;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;

import com.example.eliavmenachi.myapplication.R;

public class DateDialogFragment extends DialogFragment {
    DateDialogFragmentListener listener;
    int year;
    int month;
    int day;

    public void setDate(int y, int m, int d) {
        year = y;
        month = m;
        day = d;
    }

    interface DateDialogFragmentListener {
        void onDateSet(int y, int m, int d);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new DatePickerDialog(getActivity(), R.style.AppTheme_DatePicker1, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                Log.d("TAG", "date selected: " + i);
                if (listener != null) {
                    listener.onDateSet(i, i1, i2);
                }
            }
        }, year, month, day);
        return dialog;
    }
}
