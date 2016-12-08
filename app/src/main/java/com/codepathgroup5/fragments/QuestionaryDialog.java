package com.codepathgroup5.fragments;

import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.ToxicBakery.viewpager.transforms.CubeOutTransformer;
import com.astuetz.PagerSlidingTabStrip;
import com.codepathgroup5.listeners.ChildFragmentListener;
import com.codepathgroup5.models.Message;
import com.codepathgroup5.models.PlanPO;
import com.codepathgroup5.utilities.CustomViewPager;
import com.codepathgroup5.wanttoknow.R;
import com.parse.FindCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;
import com.yelp.clientlib.entities.Business;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class QuestionaryDialog extends DialogFragment implements ChildFragmentListener{
    private static final String TAG = QuestionaryDialog.class.getName();
    private Message message;
    private String personalListId;
    private List<Business> listBusiness = new ArrayList<Business>();
    private PlanPO planPO;
    @BindView(R.id.btnSend) Button btnSend;
    private Unbinder unbinder;
    private CustomViewPager viewPager;

    public QuestionaryDialog(){

    }

    public static QuestionaryDialog newInstance(List<Business> listBusiness) {
        QuestionaryDialog questionaryDialog = new QuestionaryDialog();
        questionaryDialog.listBusiness.addAll(listBusiness);
        //questionaryDialog.personalListId = personalListId;
        /*Bundle args = new Bundle();
        args.putString("tweet", tweet);
        frag.setArguments(args);*/
        return questionaryDialog;
    }

    public interface QuestionaryDialogListener {
        void sendDescription();
    }

    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_questionary_1, container);
        unbinder = ButterKnife.bind(this,view);
        message = new Message();
        planPO = new PlanPO();

        //Get the view pager and set its PageAdapter
        viewPager = (CustomViewPager) view.findViewById(R.id.viewPagerQuestionary);
        // Find all the effects in the web page below
        // https://github.com/ToxicBakery/ViewPagerTransforms/tree/master/library/src/main/java/com/ToxicBakery/viewpager/transforms
        viewPager.setPageTransformer(true, new CubeOutTransformer());
        //Set The listener of my View Pager
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            //This method allow to setup the direction permitted to every fragment.Also it will be invoked when a new page becomes selected.
            @Override
            public void onPageSelected(int position) {
                if (position==0){
                    viewPager.setAllowedSwipeDirection(CustomViewPager.SwipeDirection.all);
                }
                else if(position==1){
                    viewPager.setAllowedSwipeDirection(CustomViewPager.SwipeDirection.all);
                }
                else if(position==2){
                    viewPager.setAllowedSwipeDirection(CustomViewPager.SwipeDirection.all);
                }
            }

            // This method will be invoked when the current page is scrolled
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // Code goes here
            }

            // Called when the scroll state changes:
            // SCROLL_STATE_IDLE, SCROLL_STATE_DRAGGING, SCROLL_STATE_SETTLING
            @Override
            public void onPageScrollStateChanged(int state) {
                // Code goes here
            }
        });


        viewPager.setAdapter(new QuestionaryDialog.DialogPagerAdapter(getChildFragmentManager()));
        //Give the PagerSlidingTabStrip the ViewPager
        PagerSlidingTabStrip pagerSlidingTabStrip = (PagerSlidingTabStrip) view.findViewById(R.id.tabsQuestionary);
        pagerSlidingTabStrip.setViewPager(viewPager);


        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnSend.setEnabled(true);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"List Id: "+personalListId);
                /*QuestionaryDialogListener listener = (QuestionaryDialogListener) getTargetFragment();
                listener.sendDescription();
                dismiss();*/
            }
        });

        getDialog().setCancelable(true);
        getDialog().setTitle("Are you interested in?");
    }

    @Override
    public void onResume() {
        // Store access variables for window and blank point
        Window window = getDialog().getWindow();
        Point size = new Point();
        // Store dimensions of the screen in `size`
        Display display = window.getWindowManager().getDefaultDisplay();
        display.getSize(size);
        // Set the width of the dialog proportional to 75% of the screen width
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        window.setTitle("Plan");
        // Call super onResume after sizing
        super.onResume();
    }

    // When binding a fragment in onCreateView, set the views to null in onDestroyView.
    // ButterKnife returns an Unbinder on the initial binding that has an unbind method to do this automatically.
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        dismiss();
        /*Intent intent = new Intent(getContext(), PlansActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        getContext().startActivity(intent);*/
    }

    @Override
    public void nextQuestion(Fragment fragment,Message message) {
        if(fragment instanceof GeneralQuestionFragment){
            Log.d(TAG,"Setting the description: "+message.getDescription());
            planPO.setDescription(message.getDescription());
            viewPager.setAllowedSwipeDirection(CustomViewPager.SwipeDirection.left);
        }
        else if(fragment instanceof WhenContactFragment){
            planPO.setWhenDate(message.getWhenDate());
            viewPager.setAllowedSwipeDirection(CustomViewPager.SwipeDirection.left);
        }
        else if(fragment instanceof HowContactFragment){
            Log.d(TAG,"HowContactFragment");
            //Set the message object
            saveMessage();

        }

        viewPager.setCurrentItem(viewPager.getCurrentItem()+1,true);


    }

    private void saveMessage() {
        List<String> listId = new ArrayList<String>();
        for (int i = 0; i < listBusiness.size(); i++) {
            listId.add(i, listBusiness.get(i).id());
            Business businessYelp = listBusiness.get(i);
            final ParseObject business = new ParseObject("BusinessPO");
            business.put("id_yelp",businessYelp.id());
            business.put("name",businessYelp.name());
            business.put("confirmation",false);

            // Construct query to execute
            ParseQuery<ParseObject> query = ParseQuery.getQuery("BusinessPO");
            query.whereEqualTo("id_yelp",businessYelp.id());
            query.findInBackground(new FindCallback<ParseObject>() {

                public void done(List<ParseObject> listBusinesses, ParseException e) {

                    if (e == null) {
                        if(listBusinesses==null || listBusinesses.size()==0){

                            business.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if(e==null){
                                        Log.d(TAG, "Business created in db with id:"+business.getObjectId());
                                        //Initializes the ParseUser Object using its properties
                                        ParseUser user = new ParseUser();
                                        //User name is required
                                        user.setUsername(business.getString("id_yelp"));
                                        //Password is required on signup
                                        user.setPassword(business.getString("id_yelp"));

                                        //other fields can be set just like with ParseObject
                                        user.put("type","yelp_business");

                                        user.signUpInBackground(new SignUpCallback() {
                                            public void done(ParseException e) {
                                                if (e == null) {
                                                    //Connection successful
                                                    Log.d(TAG,"Connection successful");

                                                } else {
                                                    // Getting error message from the exception
                                                    String errorMessage = e.getMessage();
                                                    Log.d(TAG,"ERROR"+e.getMessage());
                                                }
                                            }
                                        });
                                    }else{
                                        Log.d(TAG, "Error creating business"+e.toString());
                                    }
                                }
                            });
                        }

                    } else {

                        Log.e("message", "Error Loading Messages" + e);

                    }

                }

            });

        }//End For loop

        //Save request

        planPO.setBusinessesList(listId);
        planPO.setOwner(ParseUser.getCurrentUser());
        planPO.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d("PlanPO","Message id: "+ planPO.getObjectId()+" saved correctly");
                    for (String businessString : planPO.getBusinessesList()) {
                        final ParseObject business_requests = new ParseObject("BusinessRequestPO");
                        business_requests.put("request_id", planPO.getObjectId());
                        business_requests.put("business_name",businessString);
                        business_requests.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if(e==null){
                                    Log.d("BusinessRequestPO","BusinessRequestPO Id: "+business_requests.getObjectId()+" saved correctly");
                                    //Toast.makeText(getContext(),"Request sended succesfully",Toast.LENGTH_LONG).show();
                                    dismiss();
                                }else{
                                    String errorMessage = e.getMessage();
                                    Log.d("PlanPO","ERROR"+e.getMessage());
                                }
                            }
                        });

                        //Push a notification
                        ParseQuery<ParseUser> query = ParseQuery.getQuery(ParseUser.class);
                        query.whereEqualTo("username",businessString);
                        query.findInBackground(new FindCallback<ParseUser>() {
                            @Override
                            public void done(List<ParseUser> objects, ParseException e) {
                                for(int i=0;i<objects.size();i++){
                                    if(objects.get(i).getObjectId()!=null) {
                                        String channel = objects.get(i).getObjectId();
                                        HashMap<String, String> payload = new HashMap<>();
                                        payload.put("customData", "My message");
                                        payload.put("channel", channel);
                                        payload.put("owner", ParseUser.getCurrentUser().getObjectId());
                                        payload.put("request", planPO.getObjectId());
                                        ParseCloud.callFunctionInBackground("pushChannelTest", payload);
                                }
                                }
                            }
                        });

                    }
                } else {
                    // Getting error message from the exception
                    String errorMessage = e.getMessage();
                    Log.d("PlanPO","ERROR"+e.getMessage());
                }
            }
        });


    }

    @Override
    public void lastQuestion(Fragment fragment) {
        viewPager.setCurrentItem(viewPager.getCurrentItem()-1,true);
    }



    //PageAdapter for the DialogFragment
    //Returns the fragment according to the tab name in the view pager
    public class DialogPagerAdapter extends FragmentPagerAdapter {
        private String tabTitles[] = new String[] { "1", "2","3"};

        //We should create the constructor to get the FragmentManager
        public DialogPagerAdapter(FragmentManager fragmentManager){
            super(fragmentManager);
        }

        //And Override the methods that allow us to get how many tabs we have and wich Fragment will show off with them

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0: // Fragment # 0 - This will show FirstFragment
                    //return FbFriendsFragment.newInstance(1, getResources().getString(R.string.planning));
                    return GeneralQuestionFragment.newInstance(1, getResources().getString(R.string.planning));
                case 1: // Fragment # 1 - This will show SecondFragment different title
                    return WhenContactFragment.newInstance(2, getResources().getString(R.string.when));
                case 2: // Fragment # 2 - This will show Last fragment
                    return HowContactFragment.newInstance(3, getResources().getString(R.string.how_get_answer));
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return tabTitles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            // Generate title based on item position
            return tabTitles[position];
        }


    }

}
