package com.codepathgroup5.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.codepathgroup5.adapters.PlanAdapter;
import com.codepathgroup5.models.FbFriend;
import com.codepathgroup5.models.PlanPO;
import com.codepathgroup5.utilities.DividerItemDecorator;
import com.codepathgroup5.utilities.NetworkUtil;
import com.codepathgroup5.wanttoknow.R;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestAsyncTask;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

import static com.parse.ParseQuery.getQuery;

/**
 * Created by kristianss27
 */

public class MyPlansFragment extends Fragment implements PlanAdapter.AdapterListener{
    private static final String TAG = MyPlansFragment.class.getName();

    private SwipeRefreshLayout swipeContainer;
    private FloatingActionButton floatingActionButton;
    private PlanAdapter adapter;
    private LinkedList<PlanPO> listPlan;
    private RecyclerView rvRequest;
    RecyclerView.LayoutManager layoutManager;
    private final int REQUEST_CODE = 200;
    private NetworkUtil networkUtil;
    private
    long maxId = 0;
    long sinceId = 0;
    boolean refresh = false;
    Context context;
    List<String> fbFriendsId;
    boolean searchFriends = false;

    private MyPlansListener listener;
    private PlanAdapter.AdapterListener listener2;

    // newInstance constructor for creating fragment with arguments
    public MyPlansFragment(boolean searchFriends) {
        this.searchFriends = searchFriends;
    }

    @Override
    public void openThePlan(PlanPO planPO) {

    }

    public interface MyPlansListener {
        void addPlan();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MyPlansListener){
            this.listener = (MyPlansListener) context;
        }
        if (context instanceof PlanAdapter.AdapterListener){
            this.listener2 = (PlanAdapter.AdapterListener) context;
        }
        else {
        throw new ClassCastException(context.toString()
                + " must implement MyPlansFragment.addPlan");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_myplans, container, false);
        return view;
    }

    // This event is triggered soon after onCreateView().
    // onViewCreated() is only called if the view returned from onCreateView() is non-null.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Declare de list
        listPlan = new LinkedList<PlanPO>();
        //This class allows us to check the internet conection and connecting to the db
        networkUtil = new NetworkUtil();
        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.btnAdd);

        //Find the Recycler view
        rvRequest = (RecyclerView) view.findViewById(R.id.rvRequest);
        //We create our linear layout manager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        //Set the Layout Manager in the Recycler View
        rvRequest.setLayoutManager(linearLayoutManager);
        //Creation of ItemDecoration to draw the division line between the tweets
        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecorator(getContext(), DividerItemDecorator.VERTICAL_LIST);
        rvRequest.addItemDecoration(itemDecoration);

        // Create and Set up the adapter into de RecyclerView
        adapter = new PlanAdapter(listPlan, this, listener2,searchFriends);
        rvRequest.setAdapter(adapter);
        //SwipeRefresh Layout necesary to refresh the Recycler View with new tweets
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        configTheSwipeRefreshLayout();

        //Check if there is connection
        if (networkUtil.connectionPermitted(getContext())) {
            populateRecyclerView();
        } else {
            Toast.makeText(getContext(), "You are offline!", Toast.LENGTH_LONG).show();
            /*networkUtil.openDataBase(this);
            Query<TweetEntity> query = networkUtil.getTweetDao().queryBuilder().build();
            listTweetsDb = query.list();
            createList(listTweetsDb);
            adapter.notifyItemRangeInserted(adapter.getItemCount(),listTweets.size()-1);
            networkUtil.closeDataBase();*/
        }

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.addPlan();
                /*Intent intent = new Intent(getContext(), NotificationService.class);
                // Add extras to the bundle
                intent.putExtra("plan_id", "21");
                intent.putExtra("facebook_id","22");
                // Start the service
                getContext().startService(intent);*/
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.listener = null;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    //Method to set up our Swipe Refresh Layout
    public void configTheSwipeRefreshLayout() {
        if (networkUtil.connectionPermitted(getContext())) {
            swipeContainer.setEnabled(true);
            //Getting the SwipeRefreshLayou
            swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    // Your code to refresh the list here.
                    refresh = true;
                    populateRecyclerView();
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


    private void populateRecyclerView(){
        // Construct query to execute
        final ParseQuery<PlanPO> planQuery = ParseQuery.getQuery(PlanPO.class);

        if(searchFriends){

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
                                        ParseQuery<ParseUser> query = ParseQuery.getQuery(ParseUser.class);
                                        query.whereEqualTo("facebook_id", facebookId);
                                        query.findInBackground(new FindCallback<ParseUser>() {
                                            @Override
                                            public void done(List<ParseUser> objects, ParseException e) {
                                                if (objects != null) {
                                                    if (objects.get(0).getObjectId() != null) {
                                                        //String userId = objects.get(0).getObjectId();
                                                        planQuery.whereEqualTo("owner",objects.get(0));
                                                        planQuery.orderByAscending("when_date");
                                                        planQuery.include("yelp_list");
                                                        planQuery.findInBackground(new FindCallback<PlanPO>() {
                                                            @Override
                                                            public void done(List<PlanPO> list, ParseException e) {
                                                                if (e == null) {
                                                                    final int size = list.size();
                                                                    Log.d("LIST SIZE", " List size start in:" + size);

                                                                    if (refresh) {
                                                                        refresh = false;
                                                                        if (size > 0) {
                                                                            listPlan.clear();
                                                                            listPlan.addAll(list);
                                                                            adapter.notifyDataSetChanged();
                                                                        }
                                                                        swipeContainer.setRefreshing(refresh);

                                                                    } else {
                                                                        refresh = false;
                                                                        listPlan.addAll(list);
                                                                        adapter.notifyDataSetChanged();
                                                                        swipeContainer.setRefreshing(refresh);
                                                                    }

                                                                } else {
                                                                    Log.e("message", "Error Loading Messages" + e);
                                                                }
                                                            }
                                                        });












                                                    }
                                                }


                                            }
                                        });




                                    //ENDDDD
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
            ).executeAsync();

        }
        else{
            planQuery.whereEqualTo("owner",ParseUser.getCurrentUser());
            planQuery.orderByAscending("when_date");
            planQuery.include("yelp_list");
            planQuery.findInBackground(new FindCallback<PlanPO>() {
                @Override
                public void done(List<PlanPO> list, ParseException e) {
                    if (e == null) {
                        final int size = list.size();
                        Log.d("LIST SIZE", " List size start in:" + size);

                        if (refresh) {
                            refresh = false;
                            if (size > 0) {
                                listPlan.clear();
                                listPlan.addAll(list);
                                adapter.notifyDataSetChanged();
                            }
                            swipeContainer.setRefreshing(refresh);

                        } else {
                            refresh = false;
                            listPlan.addAll(list);
                            adapter.notifyDataSetChanged();
                            swipeContainer.setRefreshing(refresh);
                        }

                    } else {
                        Log.e("message", "Error Loading Messages" + e);
                    }
                }
            });
        }


    }

    private void populateRecyclerView2() {

        //this condition allows us to know if We have network connection.
        if (networkUtil.connectionPermitted(getContext())) {
            if(ParseUser.getCurrentUser().get("type")!=null && ParseUser.getCurrentUser().get("type").toString().equalsIgnoreCase("yelp_business")){
                ParseQuery<ParseObject> businessRequestQuery = getQuery("BusinessRequestPO");
                businessRequestQuery.whereEqualTo("business_name",ParseUser.getCurrentUser().getString("username"));
                businessRequestQuery.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> listObjects, ParseException e) {
                        if(listObjects!=null && listObjects.size()>0){
                            for (ParseObject object : listObjects) {
                                Log.d(TAG,"request_id:"+object.getString("request_id"));
                                String requestId = object.getString("request_id");
                                //Get Message Request
                                ParseQuery<PlanPO> query = getQuery(PlanPO.class);
                                query.orderByDescending("createdAt");
                                query.whereContains("objectId", requestId);
                                //Execute query
                                query.findInBackground(new FindCallback<PlanPO>() {
                                    public void done(List<PlanPO> listObject, ParseException e) {
                                        if (e == null) {
                                            List<PlanPO> newArray = listObject;
                                            final int size = newArray.size();
                                            Log.d("LIST SIZE", " List size start in:" + size);

                                            if (refresh) {
                                                refresh = false;
                                                if (size > 0) {
                                                    listPlan.clear();
                                                    listPlan.addAll(newArray);
                                                    adapter.notifyDataSetChanged();
                                                }
                                                swipeContainer.setRefreshing(refresh);

                                            } else {
                                                refresh = false;
                                                listPlan.addAll(newArray);
                                                adapter.notifyDataSetChanged();
                                                swipeContainer.setRefreshing(refresh);
                                            }

                                        } else {
                                            Log.e("message", "Error Loading Messages" + e);
                                        }

                                    }

                                });
                            }
                        }
                        else{

                        }
                    }
                });

            }
            else {
                ParseQuery<PlanPO> query = getQuery(PlanPO.class);
                query.orderByDescending("createdAt");
                query.whereEqualTo("owner", ParseUser.getCurrentUser());
                //Execute query
                query.findInBackground(new FindCallback<PlanPO>() {
                    public void done(List<PlanPO> listObject, ParseException e) {
                        if (e == null) {
                            List<PlanPO> newArray = listObject;
                            final int size = newArray.size();
                            Log.d("LIST SIZE", " List size start in:" + size);

                            if (refresh) {
                                refresh = false;
                                if (size > 0) {
                                    listPlan.clear();
                                    listPlan.addAll(newArray);
                                    adapter.notifyDataSetChanged();
                                }
                                swipeContainer.setRefreshing(refresh);

                            } else {
                                refresh = false;
                                listPlan.addAll(newArray);
                                adapter.notifyDataSetChanged();
                                swipeContainer.setRefreshing(refresh);
                            }

                        } else {
                            Log.e("message", "Error Loading Messages" + e);
                        }

                    }

                });
            }
        } else {
            Toast.makeText(getContext(), "The device has not connection", Toast.LENGTH_SHORT).show();
        }
    }

}
