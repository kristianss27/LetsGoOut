package com.codepathgroup5.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;

import com.ToxicBakery.viewpager.transforms.ForegroundToBackgroundTransformer;
import com.astuetz.PagerSlidingTabStrip;
import com.codepathgroup5.activities.SearchActivity;
import com.codepathgroup5.listeners.ChildFragmentListener;
import com.codepathgroup5.models.Message;
import com.codepathgroup5.models.PlanPO;
import com.codepathgroup5.utilities.CustomViewPager;
import com.codepathgroup5.wanttoknow.R;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by kristianss27
 */

public class QuestionaryFragment extends Fragment implements ChildFragmentListener {
    private static final String TAG = QuestionaryFragment.class.getName();
    private Message message;
    private String personalListId;
    private PlanPO planPO;
    @BindView(R.id.btnSend)
    Button btnSend;
    private Unbinder unbinder;
    private CustomViewPager viewPager;
    private QuestionaryFragmentListener listener;

    public QuestionaryFragment(){

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof QuestionaryFragmentListener){
            this.listener = (QuestionaryFragmentListener) context;
        }
        else {
            throw new ClassCastException(context.toString()
                    + " must implement MyPlansFragment.addPlan");
        }
    }

    public static QuestionaryFragment newInstance() {
        QuestionaryFragment questionaryFragment = new QuestionaryFragment();
        questionaryFragment.message = new Message();
        return questionaryFragment;
    }

    public interface QuestionaryFragmentListener {
        void sendDescription();
        void addPlan();
    }

    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        View view = inflater.inflate(R.layout.fragment_questionary, container,false);
        unbinder = ButterKnife.bind(this,view);
        message = new Message();
        planPO = new PlanPO();

        //Get the view pager and set its PageAdapter
        viewPager = (CustomViewPager) view.findViewById(R.id.viewPagerQuestionary);
        // Find all the effects in the web page below
        // https://github.com/ToxicBakery/ViewPagerTransforms/tree/master/library/src/main/java/com/ToxicBakery/viewpager/transforms
        viewPager.setPageTransformer(true, new ForegroundToBackgroundTransformer());
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


        viewPager.setAdapter(new QuestionaryFragment.DialogPagerAdapter(getChildFragmentManager()));
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

    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    // When binding a fragment in onCreateView, set the views to null in onDestroyView.
    // ButterKnife returns an Unbinder on the initial binding that has an unbind method to do this automatically.
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        this.listener = null;
        unbinder.unbind();
    }

    @Override
    public void nextQuestion(Fragment fragment, Message message) {
        if(fragment instanceof GeneralQuestionFragment){
            Log.d(TAG,"Setting the plan purpouse and description: "+message.getPlanPurpouse()+"-"+message.getDescription());
            this.message.setPlanPurpouse(message.getPlanPurpouse());
            if(message.getDescription()!=null && !message.getDescription().equalsIgnoreCase("")){
                this.message.setDescription(message.getDescription());
            }
            else{
                this.message.setDescription("");
            }
            viewPager.setAllowedSwipeDirection(CustomViewPager.SwipeDirection.left);
        }
        else if(fragment instanceof WhenContactFragment){
            this.message.setWhenDate(message.getWhenDate());
            viewPager.setAllowedSwipeDirection(CustomViewPager.SwipeDirection.left);
        }
        else if(fragment instanceof HowContactFragment){
            Log.d(TAG,"HowContactFragment");
            //Set the message object
            this.message.setContactEmail(message.getContactEmail());
            this.message.setContactPhone(message.getContactPhone());
            this.message.setPermitCalls(message.isPermitCalls());
            saveMessage();

        }
        else if(fragment instanceof FindPlaceFragment){
            Log.d(TAG,"FindPlaceFragment");
            this.message.setPostalCode(message.getPostalCode());
            Log.d(TAG,"Sending info: "+this.message.toString());

            Intent intent = new Intent(getActivity(), SearchActivity.class);
            intent.putExtra("postal_code",message.getPostalCode());
            intent.putExtra("message", Parcels.wrap(this.message));
            startActivity(intent);
        }

        viewPager.setCurrentItem(viewPager.getCurrentItem()+1,true);


    }

    private void saveMessage() {
        /*List<String> listId = new ArrayList<String>();
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
                    Log.d("PlanPO","Message id: "+planPO.getObjectId()+" saved correctly");
                    for (String businessString : planPO.getBusinessesList()) {
                        final ParseObject business_requests = new ParseObject("BusinessRequestPO");
                        business_requests.put("request_id",planPO.getObjectId());
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

*/
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
                    return FindPlaceFragment.newInstance(3, getResources().getString(R.string.where));
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
