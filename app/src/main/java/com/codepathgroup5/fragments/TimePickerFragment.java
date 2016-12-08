package com.codepathgroup5.fragments;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.widget.TextView;
import android.widget.TimePicker;

import com.codepathgroup5.adapters.BusinessCardAdapter;
import com.codepathgroup5.adapters.BusinessesAdapter;
import com.codepathgroup5.wanttoknow.R;

import java.util.Calendar;

/**
 * Created by kristianss27 on 12/4/16.
 */

public class TimePickerFragment extends android.support.v4.app.DialogFragment implements TimePickerDialog.OnTimeSetListener,TimePicker.OnTimeChangedListener{
    private int itemPosition;
    private TextView textView;
    MyTimePickerListener listener;
    MyTimePickerListener2 listener2;
    MyTimePickerListener3 listener3;
    BusinessCardAdapter.BusinessViewHolder viewHolder;
    BusinessesAdapter.BusinessViewHolder viewHolder2;

    public interface MyTimePickerListener{
        void onTimeSet(TimePicker view, int hourOfDay, int minute, int position, BusinessCardAdapter.BusinessViewHolder viewHolder);
    }

    public interface MyTimePickerListener2{
        void onTimeSet(TimePicker view, int hourOfDay, int minute, TextView textView);
    }

    public interface MyTimePickerListener3{
        void onTimeSet(TimePicker view, int hourOfDay, int minute, int position, BusinessesAdapter.BusinessViewHolder viewHolder);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        TimePickerDialog timePickerDialog;
        Calendar c = Calendar.getInstance();
        int hour;
        int minute;
        if(getArguments()!=null){
            hour = getArguments().getInt("hour");
            minute = getArguments().getInt("minute");
        }
        else{
            hour = c.get(Calendar.HOUR_OF_DAY);
            minute = c.get(Calendar.MINUTE);
        }

        timePickerDialog = new TimePickerDialog(getActivity(),R.style.DatePicker,this,hour,minute,DateFormat.is24HourFormat(getActivity()));

        return timePickerDialog;
    }

    public static TimePickerFragment newInstance(RecyclerView.ViewHolder viewHolder, int position, MyTimePickerListener listener) {
        TimePickerFragment frag = new TimePickerFragment();
        frag.itemPosition = position;
        frag.listener = listener;
        if(viewHolder instanceof BusinessCardAdapter.BusinessViewHolder){
          frag.viewHolder = (BusinessCardAdapter.BusinessViewHolder) viewHolder;
        }
        else{
            frag.viewHolder2 = (BusinessesAdapter.BusinessViewHolder) viewHolder;
        }

        return frag;
    }

    public static TimePickerFragment newInstance2(TextView textView, MyTimePickerListener2 listener2) {
        TimePickerFragment frag = new TimePickerFragment();
        frag.textView = textView;
        frag.listener2 = listener2;
        return frag;
    }

    public static TimePickerFragment newInstance(RecyclerView.ViewHolder viewHolder, int position, MyTimePickerListener3 listener) {
        TimePickerFragment frag = new TimePickerFragment();
        frag.itemPosition = position;
        frag.listener3 = listener;
        if(viewHolder instanceof BusinessCardAdapter.BusinessViewHolder){
            frag.viewHolder = (BusinessCardAdapter.BusinessViewHolder) viewHolder;
        }
        else{
            frag.viewHolder2 = (BusinessesAdapter.BusinessViewHolder) viewHolder;
        }

        return frag;
    }

    @Override
    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
        TimePicker.OnTimeChangedListener listener = (TimePicker.OnTimeChangedListener) getActivity();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        if(listener2!=null){
            listener2.onTimeSet(view, hourOfDay, minute, textView);
        }
        else if(listener3!=null){
            listener3.onTimeSet(view,hourOfDay,minute,itemPosition,viewHolder2);
        }
        else {
            listener.onTimeSet(view, hourOfDay, minute, itemPosition, viewHolder);
        }
    }
}

