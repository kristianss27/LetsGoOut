package com.codepathgroup5.adapters;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.codepathgroup5.activities.DetailActivity;
import com.codepathgroup5.models.FbFriend;
import com.codepathgroup5.wanttoknow.R;
import com.squareup.picasso.Picasso;

import java.util.List;


public class FbFriendsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final String LOG_TAG = FbFriendsAdapter.class.getSimpleName();
    private List<FbFriend> fbFriendsList;
    private FbFriendsAdapter.AdapterListener adapterListener;
    private FragmentManager fragmentManager;
    private Fragment fragment;


    //Type of objects we might use
    private final int VIEWTYPE1 = 0, VIEWTYPE2 = 1;


    public interface AdapterListener{
        void addToPersonalList(FbFriend fbFriend);
    }

    //Constructor
    public FbFriendsAdapter(List<FbFriend> fbFriendsList,String[] uiBindFrom,int[] uiBindTo,FbFriendsAdapter.AdapterListener adapterListener) {
        this.fbFriendsList = fbFriendsList;
        this.adapterListener = adapterListener;
    }
    //Constructor2
    public FbFriendsAdapter(List<FbFriend> fbFriendsList, FragmentManager fragmentManager, Fragment fragment, FbFriendsAdapter.AdapterListener adapterListener) {
        this.fbFriendsList = fbFriendsList;
        this.adapterListener = adapterListener;
        this.fragmentManager = fragmentManager;
        this.fragment = fragment;
    }


    @Override
    public int getItemCount() {
        return fbFriendsList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return fbFriendsList.get(position)==null?VIEWTYPE1:VIEWTYPE2;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        FbFriend fbFriend = fbFriendsList.get(position);

        switch (viewHolder.getItemViewType()) {
            case VIEWTYPE1:
                FbFriendsAdapter.LoadingViewHolder vh1 = (FbFriendsAdapter.LoadingViewHolder) viewHolder;
                configureViewHolder1(vh1, position);
                break;
            case VIEWTYPE2:
                FbFriendsAdapter.FbFriendViewHolder vh2 = (FbFriendsAdapter.FbFriendViewHolder) viewHolder;
                configureViewHolder2(vh2, position);
                break;
            default:
                FbFriendsAdapter.LoadingViewHolder vh3 = (FbFriendsAdapter.LoadingViewHolder) viewHolder;
                configureViewHolder1(vh3, position);
                break;
        }


    }

    private void configureViewHolder1(FbFriendsAdapter.LoadingViewHolder loadingViewHolder, int position) {
        loadingViewHolder.progressBar.setIndeterminate(true);
    }

    private void configureViewHolder2(FbFriendsAdapter.FbFriendViewHolder fbFriendViewHolder, int position) {
        final FbFriend fbFriend = fbFriendsList.get(position);
        fbFriendViewHolder.ivName.setText(fbFriend.getName());
        String photoUrl="https://graph.facebook.com/"+fbFriend.getId()+"/picture";
        Picasso.with(fbFriendViewHolder.ivImage.getContext())
                .load(photoUrl)
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .resize(150, 150)
                .centerCrop()
                .into(fbFriendViewHolder.ivImage);
    }





    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case VIEWTYPE1:
                View v1 = inflater.inflate(R.layout.footer_progress, parent, false);
                viewHolder = new FbFriendsAdapter.LoadingViewHolder(v1);
                break;
            case VIEWTYPE2:
                View v2 = inflater.inflate(R.layout.facebook_user_card, parent, false);
                viewHolder = new FbFriendsAdapter.FbFriendViewHolder(v2);
                break;
            default:
                View v3 = inflater.inflate(R.layout.footer_progress, parent, false);
                viewHolder = new FbFriendsAdapter.LoadingViewHolder(v3);
                break;
        }

        return viewHolder;
    }

    public class LoadingViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public LoadingViewHolder(View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.pbFooterLoading);
        }
    }

    public class FbFriendViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        protected ImageView ivImage;
        protected TextView ivName;

        public FbFriendViewHolder(View v) {
            super(v);
            ivName =  (TextView) v.findViewById(R.id.tvUserName);
            ivImage = (ImageView) v.findViewById(R.id.ivUserImg);
            //v.setOnClickListener(this);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Log.d(LOG_TAG, "onClick " + getAdapterPosition());
            Intent intent = new Intent(view.getContext(), DetailActivity.class);
            view.getContext().startActivity(intent);
        }
    }



}
