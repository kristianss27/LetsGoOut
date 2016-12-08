package com.codepathgroup5.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepathgroup5.activities.PersonalPlanActivity;
import com.codepathgroup5.activities.PlansActivity;
import com.codepathgroup5.adapters.BusinessCardAdapter;
import com.codepathgroup5.adapters.ItemTouchHelperAdapter;
import com.codepathgroup5.adapters.ItemTouchHelperCallback;
import com.codepathgroup5.models.BusinessPO;
import com.codepathgroup5.models.FbFriend;
import com.codepathgroup5.models.Message;
import com.codepathgroup5.models.PlanPO;
import com.codepathgroup5.utilities.NotificationService;
import com.codepathgroup5.wanttoknow.R;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestAsyncTask;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.yelp.clientlib.entities.Business;

import org.json.JSONArray;
import org.json.JSONException;
import org.parceler.Parcels;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


public class PersonalListFragment extends Fragment implements BusinessCardAdapter.AdapterListener, ItemTouchHelperAdapter {
    private static final String TAG = PersonalListFragment.class.getName();
    private PersonalPlanActivity listener;
    private Context context;
    private List<Business> listBusinesses;
    private LinkedList<BusinessPO> listBusinessDb;
    private FloatingActionButton btnRequest;
    private FloatingActionButton btnSaveList;
    private RecyclerView rvBusinesses;
    private BusinessCardAdapter adapter;
    private Message message;
    private PlanPO planPO;


    // This event fires 1st, before creation of fragment or any views
    // The onAttach method is called when the Fragment instance is associated with an Activity.
    // This does not mean the Activity is fully initialized.
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        if (context instanceof PersonalPlanActivity) {
            this.listener = (PersonalPlanActivity) context;
        }
        if (context instanceof PlansActivity) {

        }

    }

    // This event fires 2nd, before views are created for the fragment
    // The onCreate method is called when the Fragment instance is being created, or re-created.
    // Use onCreate for any standard setup that does not require the activity to be fully created
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_personal_list, parent, false);
        return v;
    }

    // This event is triggered soon after onCreateView().
    // onViewCreated() is only called if the view returned from onCreateView() is non-null.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        //Time to initialize our plan
        planPO = new PlanPO();
        //We get all the values sended from the SearchActivity
        message = Parcels.unwrap(listener.getIntent().getParcelableExtra("message"));
        Log.d(TAG, "Getting info: " + this.message.toString());
        listBusinesses = (ArrayList<Business>) listener.getIntent().getSerializableExtra("list");

        //List to storage in db
        listBusinessDb = new LinkedList<BusinessPO>();
        prepareDataToBeStorage();

        btnRequest = (FloatingActionButton) view.findViewById(R.id.btnRequest);
        btnSaveList = (FloatingActionButton) view.findViewById(R.id.btnSaveList);
        //Find the Recycler view
        rvBusinesses = (RecyclerView) view.findViewById(R.id.rvBusinesses);
        rvBusinesses.setHasFixedSize(true);

        //We create a LinearLayoutManager and associate this to our RecylerView
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvBusinesses.setLayoutManager(linearLayoutManager);

        //Create an extra list to use it as a parameter in the adapter constructor
        List<Business> listAux = new ArrayList<Business>();
        listAux.addAll(listBusinesses);
        adapter = new BusinessCardAdapter(context, listAux, getFragmentManager(), this, this);

        rvBusinesses.setAdapter(adapter);
        ItemTouchHelper.Callback callback = new ItemTouchHelperCallback(this, "left");
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(rvBusinesses);

        btnSaveList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                QuestionaryDialog questionaryDialog = QuestionaryDialog.newInstance(listBusinesses);
                // SETS the target fragment for use later when sending results
                questionaryDialog.setTargetFragment(PersonalListFragment.this, 300);
                questionaryDialog.show(fm, "Fragment layout");
            }
        });

        btnRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpYelpList();
                planPO.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            final String planId = planPO.getObjectId();
                            GraphRequestAsyncTask graphRequestAsyncTask = new GraphRequest(
                                    AccessToken.getCurrentAccessToken()
                                    , "/me/friends/",
                                    null,
                                    HttpMethod.GET,
                                    new GraphRequest.Callback() {
                                        public void onCompleted(GraphResponse response) {
                                            try {
                                                Log.d(TAG, "response: " + response.toString());
                                                JSONArray jsonArray = response.getJSONObject().getJSONArray("data");
                                                Log.d(TAG, "jsonArray: " + jsonArray.toString());
                                                GsonBuilder gsonBuilder = new GsonBuilder();
                                                Type fbFriendType = new TypeToken<List<FbFriend>>() {}.getType();
                                                Gson gson = gsonBuilder.create();
                                                //In this case we need to get a list
                                                List<FbFriend> fbFriendList = gson.fromJson(jsonArray.toString(), fbFriendType);
                                                if (jsonArray.length()>0) {
                                                    for(int i=0;i<jsonArray.length();i++){
                                                        String facebookId = jsonArray.getJSONObject(i).getString("id");
                                                        //Launch service to send notification
                                                        Intent intent = new Intent(getContext(), NotificationService.class);
                                                        // Add extras to the bundle
                                                        intent.putExtra("plan_id", planId);
                                                        intent.putExtra("facebook_id",facebookId);
                                                        // Start the service
                                                        getContext().startService(intent);
                                                    }

                                                    Intent intent = new Intent(getContext(),PlansActivity.class);
                                                    startActivity(intent);
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                            ).executeAsync();

                        } else {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    private void prepareDataToBeStorage() {
        //Organizing the businesses selected by the user in a pare object
        for (Business business : listBusinesses) {
            BusinessPO businessPO = new BusinessPO();
            businessPO.setYelp(business.id());
            businessPO.setName(business.name());
            businessPO.setImgUrl(business.imageUrl());
            businessPO.setYelpMobileUrl(business.mobileUrl());
            businessPO.setOwner(ParseUser.getCurrentUser());
            if (business.location() != null && business.location().address() != null && business.location().address().size() > 0) {
                String city = business.location().city() != null ? business.location().city() + "," : "";
                businessPO.setLocation(city + business.location().address().get(0));
            } else {
                businessPO.setLocation(getResources().getString(R.string.no_address));
            }
            listBusinessDb.add(businessPO);
        }

        //SettingUp Plan parse object
        planPO.setOwner(ParseUser.getCurrentUser());
        planPO.setPurpouse(message.getPlanPurpouse());
        planPO.setDescription(message.getDescription());
        planPO.setWhenDate(message.getWhenDate());
        planPO.setOwnerName(ParseUser.getCurrentUser().getString("name"));
    }

    public void setUpYelpList() {
        //Here we need to include the yelp list in our PlanPO
        //One PlanPO could have one or many businessPO linked - Creating the relation one to many
        ArrayList<BusinessPO> businessesPO = new ArrayList<BusinessPO>();
        for (BusinessPO businessPO : listBusinessDb) {
            businessesPO.add(businessPO);
        }
        planPO.put("yelp_list", businessesPO);
    }

    @Override
    public void addToPersonalList(Business business) {

    }

    @Override
    public void updateList(int position, String time) {
        listBusinessDb.get(position).setPlanTime(time);
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(listBusinesses, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(listBusinesses, i, i - 1);
            }
        }
        adapter.notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onItemDismiss(final int position, int direction) {
        listBusinesses.remove(position);
        listBusinessDb.remove(position);
        adapter.deleteItem(position);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.notifyItemRemoved(position);
                adapter.notifyItemRangeChanged(position, listBusinesses.size() - position);
            }
        });
    }
}
