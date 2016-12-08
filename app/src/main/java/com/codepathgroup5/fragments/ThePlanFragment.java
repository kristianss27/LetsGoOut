package com.codepathgroup5.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.codepathgroup5.models.BusinessPO;
import com.codepathgroup5.models.PlanPO;
import com.codepathgroup5.utilities.NetworkUtil;
import com.codepathgroup5.utilities.Utility;
import com.codepathgroup5.wanttoknow.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by kristianss27
 */

public class ThePlanFragment extends Fragment implements TimePickerFragment.MyTimePickerListener2 {
    private static final String TAG = ThePlanFragment.class.getName();
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvPlanDescription)
    TextView tvPlanDescription;
    @BindView(R.id.tvDate)
    TextView tvDate;
    @BindView(R.id.imgProfile) ImageView imgProfile;
    @BindView(R.id.rlYelpList) RelativeLayout rlYelpList;

    private Unbinder unbinder;
    private PlanPO planPO;
    private ParseUser parseUser;
    List<BusinessPO> listYelp;
    String facebookId;
    private SwipeRefreshLayout swipeContainer;
    private NetworkUtil networkUtil;

    // newInstance constructor for creating fragment with arguments
    public static ThePlanFragment newInstance(PlanPO planPO) {
        ThePlanFragment thePlanFragment = new ThePlanFragment();
        thePlanFragment.planPO = planPO;
        return thePlanFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_the_plan, container, false);
        unbinder = ButterKnife.bind(this, view);
        //NetworkUtil to check connection
        networkUtil = new NetworkUtil();

        //Searching the profile image
        String planUserId = planPO.getUser().getObjectId();
        parseUser = ParseUser.getCurrentUser();
        ParseQuery<ParseUser> userQuery = ParseQuery.getQuery("_User");
        userQuery.whereEqualTo("objectId",planUserId);
        userQuery.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> list, ParseException e) {
                if (e == null) {
                    final int size = list.size();
                    Log.d("LIST SIZE", " List size start in:" + size);
                    if(size>0){
                        facebookId = list.get(0).getString("facebook_id");
                        String pictureUrl="https://graph.facebook.com/"+facebookId+"/picture";
                        Glide.with(imgProfile.getContext())
                                .load(pictureUrl)
                                .asBitmap()
                                .placeholder(R.drawable.com_facebook_profile_picture_blank_portrait)
                                .centerCrop()
                                .error(R.drawable.com_facebook_profile_picture_blank_portrait)
                                .override(160, 160)
                                .into(new BitmapImageViewTarget(imgProfile) {
                                    @Override
                                    protected void setResource(Bitmap resource) {
                                        RoundedBitmapDrawable circularBitmapDrawable =
                                                RoundedBitmapDrawableFactory.create(imgProfile.getContext().getResources(), resource);
                                        circularBitmapDrawable.setCircular(true);
                                        imgProfile.setImageDrawable(circularBitmapDrawable);
                                    }
                                });
                    }
                    else {
                        Glide.with(imgProfile.getContext())
                                .load("")
                                .asBitmap()
                                .placeholder(R.drawable.com_facebook_profile_picture_blank_portrait)
                                .centerCrop()
                                .error(R.drawable.com_facebook_profile_picture_blank_portrait)
                                .override(160, 160)
                                .into(new BitmapImageViewTarget(imgProfile) {
                                    @Override
                                    protected void setResource(Bitmap resource) {
                                        RoundedBitmapDrawable circularBitmapDrawable =
                                                RoundedBitmapDrawableFactory.create(imgProfile.getContext().getResources(), resource);
                                        circularBitmapDrawable.setCircular(true);
                                        imgProfile.setImageDrawable(circularBitmapDrawable);
                                    }
                                });
                    }
                } else {
                    Log.e("message", "Error Loading Messages" + e);
                }
            }
        });

        //We get the Yelp List selected by the user
        listYelp = planPO.getBusinessPOList();

        if(parseUser.getObjectId().equalsIgnoreCase(planUserId)){
            tvTitle.setText(planPO.getPlanPurpouse());
        }
        else{
            tvTitle.setText(planPO.getPlanPurpouse());
        }

        tvPlanDescription.setText(planPO.getDescription());

        Date when = planPO.getWhenDate();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(planPO.getWhenDate());
        String date = new Utility().getFormatDate(calendar, true);
        tvDate.setText(date);

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.flList, PlanBusinessListFragment.newInstance(planPO));
        ft.commit();

        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainerThePlan);
        configTheSwipeRefreshLayout();

        return view;
    }

    //Method to set up our Swipe Refresh Layout
    public void configTheSwipeRefreshLayout() {
        if (networkUtil.connectionPermitted(getContext())) {
            swipeContainer.setEnabled(true);
            //Getting the SwipeRefreshLayou
            swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {

                    getFragmentManager().beginTransaction().remove(getTargetFragment());
                    // Your code to refresh the list here.
                    getFragmentManager().beginTransaction().
                            replace(R.id.frame_layout, ThePlanFragment.newInstance(planPO), "MYPLANS").
                            commit();

                    swipeContainer.setRefreshing(false);
                }

            });

            // Configure the refreshing colors
            swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                    android.R.color.holo_green_light,
                    android.R.color.holo_orange_light,
                    android.R.color.holo_red_light);
        } else {
            Toast.makeText(getContext(), "Connection Failed", Toast.LENGTH_LONG).show();
            swipeContainer.setEnabled(false);
        }
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




    public void setTimePicker(TextView textView){
        DialogFragment dialogFragment;
        dialogFragment = TimePickerFragment.newInstance2(textView, (TimePickerFragment.MyTimePickerListener2) this);

        dialogFragment.setTargetFragment(this,300);
        dialogFragment.show(getFragmentManager(), "timePicker");
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute, final TextView textView) {
        final String time = hourOfDay+":"+minute;

        BusinessPO businessPO = ParseObject.createWithoutData(BusinessPO.class,(String)textView.getTag());
        businessPO.setPlanTime(time);
        businessPO.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e==null){
                    textView.setText(time);
                }
                else{
                    Toast.makeText(getContext(),"The system could not update the time",Toast.LENGTH_SHORT);
                }
            }
        });
    }
}
