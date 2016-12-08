package com.codepathgroup5.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.codepathgroup5.models.BusinessPO;
import com.codepathgroup5.models.PlanPO;
import com.codepathgroup5.utilities.Utility;
import com.codepathgroup5.wanttoknow.R;
import com.parse.ParseObject;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by kristianss27
 */
public class PlanAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final String TAG = PlanAdapter.class.getSimpleName();
    private boolean friendSearch=false;
    private List<PlanPO> requestList;
    private PlanAdapter.AdapterListener adapterListener;
    private PlanAdapter.AdapterListener adapterListener2;


    //Type of objects we might use
    private final int VIEWTYPE1 = 0, VIEWTYPE2 = 1;


    public interface AdapterListener {
        void openThePlan(PlanPO planPO);
    }

    //Constructor
    public PlanAdapter(List<PlanPO> requestList, PlanAdapter.AdapterListener adapterListener,PlanAdapter.AdapterListener adapterListener2,boolean friendSearch) {
        this.requestList = requestList;
        this.adapterListener = adapterListener;
        this.adapterListener2 = adapterListener2;
        this.friendSearch = friendSearch;
    }

    @Override
    public int getItemCount() {
        return requestList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return requestList.get(position) == null ? VIEWTYPE1 : VIEWTYPE2;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        ParseObject parseObject = requestList.get(position);

        switch (viewHolder.getItemViewType()) {
            case VIEWTYPE1:
                LoadingViewHolder vh1 = (LoadingViewHolder) viewHolder;
                configureViewHolder1(vh1, position);
                break;
            case VIEWTYPE2:
                RequestViewHolder vh2 = (RequestViewHolder) viewHolder;
                configureViewHolder2(vh2, position);
                break;
            default:
                LoadingViewHolder vh3 = (LoadingViewHolder) viewHolder;
                configureViewHolder1(vh3, position);
                break;
        }


    }

    private void configureViewHolder1(LoadingViewHolder loadingViewHolder, int position) {
        loadingViewHolder.progressBar.setIndeterminate(true);
    }

    private void configureViewHolder2(final RequestViewHolder requestViewHolder, int position) {
        PlanPO planPO = requestList.get(position);
        requestViewHolder.tvPlanTitle.setText(planPO.getPlanPurpouse());
        requestViewHolder.tvPlanDescription.setText(planPO.getDescription());
        Date when = planPO.getWhenDate();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(when);
        String date = new Utility().getFormatDate(calendar, true);
        requestViewHolder.tvDate.setText(date);
        List<BusinessPO> listYelp = planPO.getBusinessPOList();
        int imageId=0;
        int timeImageId=0;
        int textId=0;
        int textTimeId=0;

        if(friendSearch){
            String[] name = planPO.getUser().getString("name").split(" ");
            String screenName = name[0] + " "+name[1].substring(0,1)+". said: ";
            requestViewHolder.tvPlanDescription.setText(screenName+planPO.getDescription());

            String facebookId = planPO.getUser().getString("facebook_id");
            String pictureUrl="https://graph.facebook.com/"+facebookId+"/picture";

            Glide.with(requestViewHolder.imagePlan.getContext())
                    .load(pictureUrl)
                    .asBitmap()
                    .placeholder(R.drawable.com_facebook_profile_picture_blank_portrait)
                    .centerCrop()
                    .error(R.drawable.com_facebook_profile_picture_blank_portrait)
                    .override(160, 160)
                    .into(new BitmapImageViewTarget(requestViewHolder.imagePlan) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable circularBitmapDrawable =
                                    RoundedBitmapDrawableFactory.create(requestViewHolder.imagePlan.getContext().getResources(), resource);
                            circularBitmapDrawable.setCircular(true);
                            requestViewHolder.imagePlan.setImageDrawable(circularBitmapDrawable);
                        }
                    });
        }


        //All this block permit to draw every yelp place the user add to the plan
        for (int i = 0; i < listYelp.size(); i++) {
            String timeDef = listYelp.get(i).getPlanTime()!=null?"at "+listYelp.get(i).getPlanTime():"Not defined yet";
            final ImageView imageview = new ImageView(requestViewHolder.context);
            if(i==0){

                RelativeLayout.LayoutParams imageParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
                imageParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);


                imageview.setImageResource(R.drawable.ic_plan);
                imageview.setLayoutParams(imageParams);
                imageview.setPadding(2, 2, 2, 2);
                imageId = imageview.generateViewId();
                imageview.setId(imageId);

                RelativeLayout.LayoutParams textParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                textParams.addRule(RelativeLayout.RIGHT_OF,imageId);

                TextView textView = new TextView(requestViewHolder.context);
                textView.setTextSize(14);
                textView.setText(listYelp.get(i).getName());
                textView.setLayoutParams(textParams);
                textId = textView.generateViewId();
                textView.setId(textId);

                requestViewHolder.relativeLayoutYelpList.addView(imageview);
                requestViewHolder.relativeLayoutYelpList.addView(textView);

                Glide.with(imageview.getContext())
                        .load(listYelp.get(i).getImgUrl())
                        .asBitmap()
                        .placeholder(R.mipmap.ic_location)
                        .centerCrop()
                        .error(R.mipmap.ic_location)
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

                RelativeLayout.LayoutParams timeParams = new RelativeLayout.LayoutParams(75,75);
                timeParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

                final ImageView timeImg = new ImageView(requestViewHolder.context);
                timeImg.setImageResource(R.drawable.ic_time);
                timeImg.setLayoutParams(timeParams);
                timeImageId = timeImg.generateViewId();
                timeImg.setId(timeImageId);

                RelativeLayout.LayoutParams textTimeParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                textTimeParams.addRule(RelativeLayout.LEFT_OF,timeImageId);

                TextView textTimeView = new TextView(requestViewHolder.context);
                textTimeView.setTextSize(14);
                textTimeView.setText(timeDef);
                textTimeView.setLayoutParams(textTimeParams);
                textTimeId = textTimeView.generateViewId();
                textTimeView.setId(textTimeId);

                    requestViewHolder.relativeLayoutYelpList.addView(timeImg);
                    requestViewHolder.relativeLayoutYelpList.addView(textTimeView);

            }
            else{
                RelativeLayout.LayoutParams imageParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                imageParams.addRule(RelativeLayout.BELOW,imageId);

                RelativeLayout.LayoutParams timeParams = new RelativeLayout.LayoutParams(75,75);
                timeParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                timeParams.addRule(RelativeLayout.BELOW,imageId);
                RelativeLayout.LayoutParams textTimeParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                textTimeParams.addRule(RelativeLayout.BELOW,imageId);

                imageview.setImageResource(R.drawable.ic_plan);
                imageview.setLayoutParams(imageParams);
                imageview.setPadding(2, 2, 2, 2);

                RelativeLayout.LayoutParams textParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                textParams.addRule(RelativeLayout.BELOW,imageId);

                imageId = imageview.generateViewId();
                imageview.setId(imageId);

                textParams.addRule(RelativeLayout.END_OF,imageId);

                TextView textView = new TextView(requestViewHolder.context);
                textView.setTextSize(14);
                textView.setText(listYelp.get(i).getName());
                textView.setLayoutParams(textParams);
                textId = textView.generateViewId();
                textView.setId(textId);

                requestViewHolder.relativeLayoutYelpList.addView(imageview);
                requestViewHolder.relativeLayoutYelpList.addView(textView);

                Glide.with(imageview.getContext())
                        .load(listYelp.get(i).getImgUrl())
                        .asBitmap()
                        .placeholder(R.mipmap.ic_location)
                        .centerCrop()
                        .error(R.mipmap.ic_location)
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

                final ImageView timeImg = new ImageView(requestViewHolder.context);
                timeImg.setImageResource(R.drawable.ic_time);
                timeImg.setLayoutParams(timeParams);
                timeImageId = timeImg.generateViewId();
                timeImg.setId(timeImageId);

                textTimeParams.addRule(RelativeLayout.LEFT_OF,timeImageId);

                TextView textTimeView = new TextView(requestViewHolder.context);
                textTimeView.setTextSize(14);
                textTimeView.setText(timeDef);
                textTimeView.setLayoutParams(textTimeParams);
                textTimeId = textTimeView.generateViewId();
                textTimeView.setId(textTimeId);

                    requestViewHolder.relativeLayoutYelpList.addView(timeImg);
                    requestViewHolder.relativeLayoutYelpList.addView(textTimeView);

            }

        }
    }


        /*final ImageView imageView = new ImageView(requestViewHolder.linearLayoutYelp.getContext());
        imageView.setMaxWidth(50);
        imageView.setMaxHeight(50);

        Glide.with(imageView.getContext())
                .load(planPO.getBusinessPOList().get(0).getImgUrl())
                .asBitmap()
                .centerCrop()
                .into(new BitmapImageViewTarget(imageView) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(imageView.getContext().getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        imageView.setImageDrawable(circularBitmapDrawable);
                    }
                });

*/

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case VIEWTYPE1:
                View v1 = inflater.inflate(R.layout.footer_progress, parent, false);
                viewHolder = new PlanAdapter.LoadingViewHolder(v1);
                break;
            case VIEWTYPE2:
                View v2 = inflater.inflate(R.layout.plan_item, parent, false);
                viewHolder = new PlanAdapter.RequestViewHolder(v2);
                break;
            default:
                View v3 = inflater.inflate(R.layout.footer_progress, parent, false);
                viewHolder = new PlanAdapter.LoadingViewHolder(v3);
                break;
        }

        return viewHolder;
    }

    class LoadingViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public LoadingViewHolder(View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.pbFooterLoading);
        }
    }

    public class RequestViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        protected TextView tvPlanTitle;
        protected TextView tvPlanDescription;
        protected TextView tvDate;
        protected Context context;
        protected RelativeLayout relativeLayoutYelpList;
        protected ImageView imagePlan;
        //protected RelativeLayout rlPanelRight;

        public RequestViewHolder(View v) {
            super(v);
            context = v.getContext();
            tvPlanTitle = (TextView) v.findViewById(R.id.tvPlanTitle);
            tvPlanDescription = (TextView) v.findViewById(R.id.tvPlanDescription);
            tvDate = (TextView) v.findViewById(R.id.tvDate);
            relativeLayoutYelpList = (RelativeLayout) v.findViewById(R.id.rlYelpList);
            imagePlan = (ImageView) v.findViewById(R.id.imgPlan);
            //rlPanelRight = (RelativeLayout) v.findViewById(rlPanelRight);
            //v.setOnClickListener(this);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition(); // gets item position
            if (position != RecyclerView.NO_POSITION) { // Check if an item was deleted, but the user clicked it before the UI removed it
                PlanPO planPO = requestList.get(position);
                adapterListener2.openThePlan(planPO);
            }
        }
    }
}