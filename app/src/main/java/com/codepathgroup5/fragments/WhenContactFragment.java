package com.codepathgroup5.fragments;


import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepathgroup5.listeners.ChildFragmentListener;
import com.codepathgroup5.models.Message;
import com.codepathgroup5.utilities.Utility;
import com.codepathgroup5.wanttoknow.R;

import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class WhenContactFragment extends Fragment implements DatePickerDialog.OnDateSetListener{
    private static final String TAG = WhenContactFragment.class.getName();

    @BindView(R.id.imgCalendar)
    ImageView imgCalendar;
    @BindView(R.id.tvDate) TextView tvDate;
    @BindView(R.id.btnNext) Button btnNext;
    @BindView(R.id.btnBack) Button btnBack;

    private Calendar calendar;
    private Date date;
    private int year;
    private int month;
    private int day;
    private String query;
    private Unbinder unbinder;
    private String title;
    private int page;
    private Message message;

    // newInstance constructor for creating fragment with arguments
    public static WhenContactFragment newInstance(int page, String title) {
        WhenContactFragment whenContactFragment = new WhenContactFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        whenContactFragment.setArguments(args);
        return whenContactFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("someInt", 0);
        title = getArguments().getString("someTitle");
        message = new Message();
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_when_contact, container, false);
        unbinder = ButterKnife.bind(this, view);
        //Set up Listeners
        setListeners();
        //Today
        Calendar today = Calendar.getInstance();
        date = today.getTime();
        String date = new Utility().getFormatDate(today,true);

        TextView tvLabel = (TextView) view.findViewById(R.id.tvLabel);
        tvLabel.setText(getTitle());

        return view;
    }

    // This event is triggered soon after onCreateView().
    // onViewCreated() is only called if the view returned from onCreateView() is non-null.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    // When binding a fragment in onCreateView, set the views to null in onDestroyView.
    // ButterKnife returns an Unbinder on the initial binding that has an unbind method to do this automatically.
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void setListeners(){
        final ChildFragmentListener listener = (ChildFragmentListener) getParentFragment();

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"Next Btn pushed");

                //Getting the date
                if(calendar!=null && !calendar.toString().equalsIgnoreCase("")){
                    Date date = calendar.getTime();
                    Log.d(TAG,"When Date: "+date.toString());
                    message.setWhenDate(date);
                    listener.nextQuestion(WhenContactFragment.this,message);
                }
                else{
                    Toast.makeText(getContext(),"You should select a date for your plan",Toast.LENGTH_LONG).show();
                }

            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.lastQuestion(WhenContactFragment.this);
            }
        });

        imgCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(v);
            }
        });
    }

    public void showDatePickerDialog(View v){
        DialogFragment dialogFragment;
        FragmentManager fm = getChildFragmentManager();

        if(calendar!=null){
            dialogFragment = DatePickerFragment.newInstance(String.valueOf(day),String.valueOf(month),
                    String.valueOf(year));
        }
        else{
            dialogFragment = new DatePickerFragment();
        }

        dialogFragment.setTargetFragment(WhenContactFragment.this,300);
        dialogFragment.show(fm, "datePicker");

        //InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        //imm.hideSoftInputFromWindow(v.getWindowToken(),0);

    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        calendar = Calendar.getInstance();
        calendar.set(year,month,day);
        date = calendar.getTime();
        this.year=year;
        month++;
        this.month=month;
        this.day=day;
        date = calendar.getTime();
        tvDate.setText(""+month+"/"+day+"/"+year);

    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

}
