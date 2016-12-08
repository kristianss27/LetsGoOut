package com.codepathgroup5.adapters;

//import com.yelp.clientlib.entities.Business;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.codepathgroup5.fragments.TimePickerFragment;
import com.codepathgroup5.models.BusinessPO;
import com.codepathgroup5.wanttoknow.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.yelp.clientlib.entities.Business;

import java.util.ArrayList;
import java.util.List;


public class BusinessesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements TimePickerFragment.MyTimePickerListener3 {
    public static final String TAG = BusinessesAdapter.class.getSimpleName();
    private List<BusinessPO> businessList;
    private AdapterListener adapterListener;
    private FragmentManager fragmentManager;
    private Fragment fragment;
    private Context context;


    //Type of objects we might use
    private final int VIEWTYPE1 = 0, VIEWTYPE2 = 1, VIEWTYPE3 = 2;

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute, int position, BusinessesAdapter.BusinessViewHolder viewHolder) {
        String time = hourOfDay + ":" + minute;
        viewHolder.tvTime.setText(time);
        adapterListener.updateList(position, time);
    }


    public interface AdapterListener {
        void addToPersonalList(Business business);

        void updateList(int position, String time);
    }

    //Constructor
    public BusinessesAdapter(List<BusinessPO> businessList, AdapterListener adapterListener) {
        this.businessList = businessList;
        this.adapterListener = adapterListener;
    }

    //Constructor2
    public BusinessesAdapter(Context context, List<BusinessPO> businessList, FragmentManager fragmentManager, Fragment fragment, AdapterListener adapterListener) {
        this.context = context;
        this.businessList = businessList;
        this.adapterListener = adapterListener;
        this.fragmentManager = fragmentManager;
        this.fragment = fragment;
    }

    public void deleteItem(int position) {
        businessList.remove(position);
    }

    @Override
    public int getItemCount() {
        return businessList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (fragmentManager != null) {
            if (businessList.get(0).getUser() == ParseUser.getCurrentUser()) {
                return VIEWTYPE3;
            } else {
                return VIEWTYPE2;
            }

        } else {
            return businessList.get(position) == null ? VIEWTYPE1 : VIEWTYPE2;
        }
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        BusinessPO business = businessList.get(position);

        switch (viewHolder.getItemViewType()) {
            case VIEWTYPE1:
                LoadingViewHolder vh1 = (LoadingViewHolder) viewHolder;
                configureViewHolder1(vh1, position);
                break;
            case VIEWTYPE2:
                BusinessViewHolder vh2 = (BusinessViewHolder) viewHolder;
                configureViewHolder2(vh2, position);
                break;
            case VIEWTYPE3:
                BusinessViewHolder vh3 = (BusinessViewHolder) viewHolder;
                configureViewHolder3(vh3, position);
                break;
            default:
                LoadingViewHolder vh4 = (LoadingViewHolder) viewHolder;
                configureViewHolder1(vh4, position);
                break;
        }


    }

    private void configureViewHolder1(LoadingViewHolder loadingViewHolder, int position) {
        loadingViewHolder.progressBar.setIndeterminate(true);
    }

    private void configureViewHolder2(final BusinessViewHolder businessViewHolder, final int position) {
        final BusinessPO business = businessList.get(position);
        businessViewHolder.vName.setText(business.getName());
        String timeDef = business.getPlanTime() != null ? "at " + business.getPlanTime() : "Not defined yet";

        businessViewHolder.tvTime.setText(timeDef);

        if (business.getLocation() != null) {
            businessViewHolder.vAddress.setText(business.getLocation());
        } else {
            businessViewHolder.vAddress.setText(R.string.no_address);
        }


        Glide.with(businessViewHolder.vImage.getContext())
                .load(business.getImgUrl())
                .asBitmap()
                .centerCrop()
                .into(new BitmapImageViewTarget(businessViewHolder.vImage) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(businessViewHolder.vImage.getContext().getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        businessViewHolder.vImage.setImageDrawable(circularBitmapDrawable);
                    }
                });

        businessViewHolder.btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ArrayList<ParseUser> usersJoin = new ArrayList<ParseUser>();
                usersJoin.add(ParseUser.getCurrentUser());
                final BusinessPO businessUpdate = ParseObject.createWithoutData(BusinessPO.class,business.getObjectId());
                businessUpdate.put("users_join",usersJoin);
                businessUpdate.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e==null){
                            Log.d(TAG,"Updating business: "+businessUpdate.getObjectId());
                            Toast.makeText(context,"Yay! You are part of this plan",Toast.LENGTH_LONG).show();
                            businessViewHolder.btnJoin.setVisibility(View.INVISIBLE);
                            businessViewHolder.btnReject.setVisibility(View.VISIBLE);
                        }
                        else {

                        }
                    }
                });

            }
        });

        businessViewHolder.btnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                businessViewHolder.btnJoin.setVisibility(View.VISIBLE);
                businessViewHolder.btnReject.setVisibility(View.INVISIBLE);
            }
        });

    }

    private void configureViewHolder3(final BusinessViewHolder businessViewHolder, final int position) {
        final BusinessPO business = businessList.get(position);
        businessViewHolder.vName.setText(business.getName());
        String timeDef = business.getPlanTime() != null ? "at " + business.getPlanTime() : "Not defined yet";

        businessViewHolder.tvTime.setText(timeDef);

        if (business.getLocation() != null) {
            businessViewHolder.vAddress.setText(business.getLocation());
        } else {
            businessViewHolder.vAddress.setText(R.string.no_address);
        }

        businessViewHolder.imgTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTimePicker(businessViewHolder, position);
            }
        });

        Glide.with(businessViewHolder.vImage.getContext())
                .load(business.getImgUrl())
                .asBitmap()
                .centerCrop()
                .into(new BitmapImageViewTarget(businessViewHolder.vImage) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(businessViewHolder.vImage.getContext().getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        businessViewHolder.vImage.setImageDrawable(circularBitmapDrawable);
                    }
                });


        if(business.get("users_join")!=null){
            ArrayList<ParseUser> usersJoin = (ArrayList<ParseUser>) business.get("users_join");
            int i = 0;
            for (ParseUser parseUser : usersJoin) {
                if(i==0){
                    TextView textView = new TextView(businessViewHolder.context);
                    LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    textParams.setMargins(10,20,4,4);
                    textView.setTextSize(14);
                    textView.setText("Friends on board: ");
                    textView.setLayoutParams(textParams);
                    int textId = textView.generateViewId();
                    textView.setId(textId);
                    businessViewHolder.llUsersJoin.addView(textView);
                }

                LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                final ImageView imageview = new ImageView(businessViewHolder.context);
                imageview.setImageResource(R.drawable.ic_time);
                imageview.setLayoutParams(imageParams);
                imageParams.setMargins(2,0,2,10);
                imageview.setPadding(2, 2, 2, 2);
                int imageId = imageview.generateViewId();
                imageview.setId(imageId);

                businessViewHolder.llUsersJoin.addView(imageview);

                ParseQuery<ParseUser> userQuery = ParseQuery.getQuery("_User");
                userQuery.whereEqualTo("objectId",parseUser.getObjectId());
                userQuery.findInBackground(new FindCallback<ParseUser>() {
                    @Override
                    public void done(List<ParseUser> list, ParseException e) {
                        if (e == null) {
                            final int size = list.size();
                            Log.d("LIST SIZE", " List size start in:" + size);
                            if(size>0){
                                String facebookId = list.get(0).getString("facebook_id");
                                String pictureUrl="https://graph.facebook.com/"+facebookId+"/picture";
                                Glide.with(imageview.getContext())
                                        .load(pictureUrl)
                                        .asBitmap()
                                        .placeholder(R.drawable.com_facebook_profile_picture_blank_portrait)
                                        .centerCrop()
                                        .error(R.drawable.com_facebook_profile_picture_blank_portrait)
                                        .override(160, 160)
                                        .into(new BitmapImageViewTarget(imageview) {
                                            @Override
                                            protected void setResource(Bitmap resource) {
                                                RoundedBitmapDrawable circularBitmapDrawable =
                                                        RoundedBitmapDrawableFactory.create(imageview.getContext().getResources(), resource);
                                                circularBitmapDrawable.setCircular(true);
                                                imageview.setImageDrawable(circularBitmapDrawable);
                                            }
                                        });
                            }
                            else {
                                Glide.with(imageview.getContext())
                                        .load("")
                                        .asBitmap()
                                        .placeholder(R.drawable.com_facebook_profile_picture_blank_portrait)
                                        .centerCrop()
                                        .error(R.drawable.com_facebook_profile_picture_blank_portrait)
                                        .override(160, 160)
                                        .into(new BitmapImageViewTarget(imageview) {
                                            @Override
                                            protected void setResource(Bitmap resource) {
                                                RoundedBitmapDrawable circularBitmapDrawable =
                                                        RoundedBitmapDrawableFactory.create(imageview.getContext().getResources(), resource);
                                                circularBitmapDrawable.setCircular(true);
                                                imageview.setImageDrawable(circularBitmapDrawable);
                                            }
                                        });
                            }
                        } else {
                            Log.e("message", "Error Loading Messages" + e);
                        }
                    }
                });

            }
        }
    }

    public void setTimePicker(BusinessViewHolder businessViewHolder, int position) {
        DialogFragment dialogFragment;
        dialogFragment = TimePickerFragment.newInstance(businessViewHolder, position, this);

        dialogFragment.setTargetFragment(fragment, 300);
        dialogFragment.show(fragmentManager, "timePicker");
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case VIEWTYPE1:
                View v1 = inflater.inflate(R.layout.footer_progress, parent, false);
                viewHolder = new LoadingViewHolder(v1);
                break;
            case VIEWTYPE2:
                View v2 = inflater.inflate(R.layout.business_card_selected_by_others, parent, false);
                viewHolder = new BusinessViewHolder(v2, VIEWTYPE2);
                break;
            case VIEWTYPE3:
                View v3 = inflater.inflate(R.layout.business_selected_by_me, parent, false);
                viewHolder = new BusinessViewHolder(v3, VIEWTYPE3);
                break;
            default:
                View v4 = inflater.inflate(R.layout.footer_progress, parent, false);
                viewHolder = new LoadingViewHolder(v4);
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

    public class BusinessViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        protected ImageView vImage;
        protected TextView vName;
        protected TextView vAddress;
        protected Context context;
        protected ImageView imgTime;
        protected TextView tvTime;
        protected Button btnJoin;
        protected Button btnReject;
        protected LinearLayout llUsersJoin;

        public BusinessViewHolder(View v, int type) {
            super(v);


            imgTime = (ImageView) v.findViewById(R.id.imgTime);
            tvTime = (TextView) v.findViewById(R.id.tvTime);
            vName = (TextView) v.findViewById(R.id.business_name);
            vAddress = (TextView) v.findViewById(R.id.business_addr);
            vImage = (ImageView) v.findViewById(R.id.business_thumb);

            if(type==VIEWTYPE2){
                btnJoin = (Button) v.findViewById(R.id.btnJoin);
                btnReject = (Button) v.findViewById(R.id.btnReject);
            }
            llUsersJoin = (LinearLayout) v.findViewById(R.id.llUsersJoin);

            context = v.getContext();
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Log.d(TAG, "onClick " + getAdapterPosition());
            Intent i = new Intent(Intent.ACTION_VIEW);
            String url = businessList.get(getAdapterPosition()).getYelpMobileUrl();
            Log.d(TAG, "Business url:" + url);
            if (!url.startsWith("https://") && !url.startsWith("http://")) {
                url = "http://" + url;
            }
            i.setData(Uri.parse(url));
            view.getContext().startActivity(i);
        }
    }
}

