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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.TimePicker;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.codepathgroup5.fragments.TimePickerFragment;
import com.codepathgroup5.wanttoknow.R;
import com.yelp.clientlib.entities.Business;

import java.util.List;


public class BusinessCardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements TimePickerFragment.MyTimePickerListener{
    public static final String LOG_TAG = BusinessCardAdapter.class.getSimpleName();
    private List<Business> businessList;
    private AdapterListener adapterListener;
    private FragmentManager fragmentManager;
    private Fragment fragment;
    private Context context;


    //Type of objects we might use
    private final int VIEWTYPE1 = 0, VIEWTYPE2 = 1, VIEWTYPE3 = 2;

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute,int position,BusinessViewHolder viewHolder) {
        String time = hourOfDay+":"+minute;
        viewHolder.tvTime.setText(time);
        adapterListener.updateList(position,time);
    }


    public interface AdapterListener{
        void addToPersonalList(Business business);
        void updateList(int position,String time);
    }

    //Constructor
    public BusinessCardAdapter(List<Business> businessList,AdapterListener adapterListener) {
        this.businessList = businessList;
        this.adapterListener = adapterListener;
    }
    //Constructor2
    public BusinessCardAdapter(Context context, List<Business> businessList, FragmentManager fragmentManager, Fragment fragment, AdapterListener adapterListener) {
        this.context = context;
        this.businessList = businessList;
        this.adapterListener = adapterListener;
        this.fragmentManager = fragmentManager;
        this.fragment = fragment;
    }

    public void deleteItem(int position){
        businessList.remove(position);
    }

    @Override
    public int getItemCount() {
        return businessList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(fragmentManager!=null){
            return VIEWTYPE3;
        }
        else {
            return businessList.get(position) == null ? VIEWTYPE1 : VIEWTYPE2;
        }
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        Business business = businessList.get(position);

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

    private void configureViewHolder2(final BusinessViewHolder businessViewHolder, int position) {
        final Business business = businessList.get(position);
        businessViewHolder.vName.setText(business.name());
        businessViewHolder.vPhone.setText(business.displayPhone());
        businessViewHolder.tvReviewCount.setText(String.valueOf(business.reviewCount()));
        Log.d("RATING","Business rating:"+business.rating());
        businessViewHolder.rbVoteAverage.setRating(Float.parseFloat(business.rating().toString()));


        if(business.location().address()!=null && business.location().address().size()>0) {
            businessViewHolder.vAddress.setText(business.location().city()+","+business.location().address().get(0));
        }
        else{
            businessViewHolder.vAddress.setText(R.string.no_address);
        }

        /*Picasso.with(businessViewHolder.vImage.getContext())
                .load(business.imageUrl())
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .resize(150, 150)
                .centerCrop()
                .into(businessViewHolder.vImage);*/

        Glide.with(businessViewHolder.vImage.getContext())
                .load(business.imageUrl())
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

    }

    private void configureViewHolder3(final BusinessViewHolder businessViewHolder, final int position) {
        final Business business = businessList.get(position);
        businessViewHolder.vName.setText(business.name());
        businessViewHolder.vPhone.setText(business.displayPhone());
        businessViewHolder.tvReviewCount.setText(String.valueOf(business.reviewCount()));
        Log.d("RATING","Business rating:"+business.rating());
        businessViewHolder.rbVoteAverage.setRating(Float.parseFloat(business.rating().toString()));

        if(business.location().address()!=null && business.location().address().size()>0) {
            businessViewHolder.vAddress.setText(business.location().city()+","+business.location().address().get(0));
        }
        else{
            businessViewHolder.vAddress.setText(R.string.no_address);
        }

        businessViewHolder.imgTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTimePicker(businessViewHolder,position);
            }
        });

        Glide.with(businessViewHolder.vImage.getContext())
                .load(business.imageUrl())
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
    }

    public void setTimePicker(BusinessViewHolder businessViewHolder,int position){
        DialogFragment dialogFragment;
        dialogFragment = TimePickerFragment.newInstance(businessViewHolder,position,this);

        dialogFragment.setTargetFragment(fragment,300);
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
                View v2 = inflater.inflate(R.layout.business_card, parent, false);
                viewHolder = new BusinessViewHolder(v2,VIEWTYPE2);
                break;
            case VIEWTYPE3:
                View v3 = inflater.inflate(R.layout.business_card_selected, parent, false);
                viewHolder = new BusinessViewHolder(v3,VIEWTYPE3);
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

    public class BusinessViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        protected ImageView vImage;
        protected TextView vName;
        protected TextView vPhone;
        protected TextView vAddress;
        protected TextView tvReviewCount;
        protected TextView tvRating;
        protected ImageView ivRatingImgUrl;
        protected Context context;
        protected RatingBar rbVoteAverage;
        protected ImageView imgTime;
        protected TextView tvTime;

        public BusinessViewHolder(View v,int type) {
            super(v);

            if(type == VIEWTYPE3){
                imgTime = (ImageView) v.findViewById(R.id.imgTime);
                tvTime = (TextView) v.findViewById(R.id.tvTime);
            }

            vName =  (TextView) v.findViewById(R.id.business_name);
            vPhone = (TextView)  v.findViewById(R.id.business_phone);
            vAddress = (TextView)  v.findViewById(R.id.business_addr);
            vImage = (ImageView) v.findViewById(R.id.business_thumb);
            tvReviewCount = (TextView) v.findViewById(R.id.tvReviewCount);
            rbVoteAverage = (RatingBar) v.findViewById(R.id.rbVoteAverage);
            context = v.getContext();
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Log.d(LOG_TAG, "onClick " + getAdapterPosition());
            Intent i = new Intent(Intent.ACTION_VIEW);
            String url = businessList.get(getAdapterPosition()).mobileUrl();
            Log.d(LOG_TAG,"Business url:"+url);
            if (!url.startsWith("https://") && !url.startsWith("http://")){
                url = "http://" + url;
            }
            i.setData(Uri.parse(url));
            view.getContext().startActivity(i);
            /*Intent intent = new Intent(view.getContext(), DetailActivity.class)
                    .putExtra(Intent.EXTRA_TEXT, businessList.get(getAdapterPosition()));
            view.getContext().startActivity(intent);*/
        }
    }
}

