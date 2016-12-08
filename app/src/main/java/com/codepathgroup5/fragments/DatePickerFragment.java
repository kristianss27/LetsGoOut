package com.codepathgroup5.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;

import com.codepathgroup5.wanttoknow.R;

import java.util.Calendar;


/**
 * Created by kristianss27
 */

public class DatePickerFragment extends android.support.v4.app.DialogFragment implements DatePickerDialog.OnDateSetListener,DatePicker.OnDateChangedListener{

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        DatePickerDialog datePickerDialog;
        // Use the current date as the default date for the picker
        final Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);

        if(getArguments()!=null){
            String dayArg = getArguments().getString("day");
            String monthArg = getArguments().getString("month");
            String yearArg = getArguments().getString("year");
            datePickerDialog = new DatePickerDialog(getActivity(),R.style.DatePicker,this,Integer.parseInt(yearArg),Integer.parseInt(monthArg),Integer.parseInt(dayArg));
        }

        else{
            datePickerDialog = new DatePickerDialog(getActivity(),R.style.DatePicker,this,year,month,day);
        }
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        // Create a new instance of DatePickerDialog and return it
        return datePickerDialog;

    }

    public static DatePickerFragment newInstance(String day, String month, String year) {
        DatePickerFragment frag = new DatePickerFragment();
        int monthDef = Integer.parseInt(month)-1;

        Bundle args = new Bundle();
        args.putString("day", day);
        args.putString("month",String.valueOf(monthDef));
        args.putString("year",year);
        frag.setArguments(args);
        return frag;
    }


    public void onDateSet(DatePicker view, int year, int month, int day) {
        DatePickerDialog.OnDateSetListener listener = (DatePickerDialog.OnDateSetListener) getTargetFragment();
        listener.onDateSet(view, year, month, day);
    }

    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth){
        DatePicker.OnDateChangedListener listener = (DatePicker.OnDateChangedListener) getActivity();
    }


}

