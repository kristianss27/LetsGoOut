package com.codepathgroup5.fragments;

import android.content.Context;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codepathgroup5.activities.PersonalPlanActivity;
import com.codepathgroup5.adapters.FbFriendsAdapter;
import com.codepathgroup5.adapters.ItemTouchHelperAdapter;
import com.codepathgroup5.models.FbFriend;
import com.codepathgroup5.wanttoknow.FacebookClient;
import com.codepathgroup5.wanttoknow.R;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestAsyncTask;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class FbFriendsFragment extends Fragment implements FbFriendsAdapter.AdapterListener,ItemTouchHelperAdapter {
    private static final String TAG = FbFriendsFragment.class.getName();
    private Context context;
    private PersonalPlanActivity listener;
    List<FbFriend> listFriends;
    private FloatingActionButton btnRequest;
    private RecyclerView rvFbFriends;
    private FbFriendsAdapter adapter;
    private FacebookClient facebookClient;
    private String title;
    private int page;

    // newInstance constructor for creating fragment with arguments
    public static FbFriendsFragment newInstance(int page, String title) {
        FbFriendsFragment fbFriendsFragment = new FbFriendsFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        fbFriendsFragment.setArguments(args);
        return fbFriendsFragment;
    }

    /* This event fires 1st, before creation of fragment or any views
    // The onAttach method is called when the Fragment instance is associated with an Activity.
    // This does not mean the Activity is fully initialized.
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        if (context instanceof PersonalPlanActivity){
            this.listener = (PersonalPlanActivity) context;
        }
    }*/

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
        View view = inflater.inflate(R.layout.fragment_fbfriends_list,parent,false);
        return view;
    }

    // This event is triggered soon after onCreateView().
    // onViewCreated() is only called if the view returned from onCreateView() is non-null.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        TextView tvLabel = (TextView) view.findViewById(R.id.tvLabel);
        tvLabel.setText(getPage() + " -- " + getTitle());
        //Facebook client
        facebookClient = FacebookClient.facebookClient;

        btnRequest = (FloatingActionButton) view.findViewById(R.id.btnRequest);
        //Find the Recycler view
        rvFbFriends = (RecyclerView) view.findViewById(R.id.rvFbFriends);
        rvFbFriends.setHasFixedSize(true);

        //We create a LinearLayoutManager and associate this to our RecylerView
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvFbFriends.setLayoutManager(linearLayoutManager);

        if(facebookClient!=null) {
            listFriends = facebookClient.getFbFriendList();
        }

        btnRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                QuestionaryDialog questionaryDialog = QuestionaryDialog.newInstance(null);
                // SETS the target fragment for use later when sending results
                questionaryDialog.setTargetFragment(FbFriendsFragment.this, 300);
                questionaryDialog.show(fm,"Fragment layout");
            }
        });


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
                            Type fbFriendType = new TypeToken<List<FbFriend>>() {
                            }.getType();
                            Gson gson = gsonBuilder.create();
                            //In this case we need to get a Tweet List
                            List<FbFriend> fbFriendList = gson.fromJson(jsonArray.toString(), fbFriendType);
                            if(fbFriendList!=null && fbFriendList.size()>0) {
                                listFriends = new ArrayList<FbFriend>();
                                listFriends.addAll(fbFriendList);

                                //Get Contacts from phone
                                // Column data from cursor to bind views from
                                String[] uiBindFrom = { ContactsContract.Contacts.DISPLAY_NAME,
                                        ContactsContract.Contacts.PHOTO_URI };
                                // View IDs which will have the respective column data inserted
                                int[] uiBindTo = { R.id.tvUserName, R.id.ivUserImg };

                                adapter = new FbFriendsAdapter(listFriends,uiBindFrom,uiBindTo,FbFriendsFragment.this);
                                rvFbFriends.setAdapter(adapter);
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
        ).executeAsync();

    }

    @Override
    public void addToPersonalList(FbFriend friend) {

    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(listFriends, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(listFriends, i, i - 1);
            }
        }
        adapter.notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onItemDismiss(int position, int direction) {
        listFriends.remove(position);
        adapter.notifyItemRemoved(position);
        Log.d(TAG,"DIRECTION: "+direction);
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
