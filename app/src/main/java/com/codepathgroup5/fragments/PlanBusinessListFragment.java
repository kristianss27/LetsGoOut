package com.codepathgroup5.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.codepathgroup5.activities.PlansActivity;
import com.codepathgroup5.adapters.BusinessesAdapter;
import com.codepathgroup5.adapters.ItemTouchHelperAdapter;
import com.codepathgroup5.adapters.ItemTouchHelperCallback;
import com.codepathgroup5.models.BusinessPO;
import com.codepathgroup5.models.PlanPO;
import com.codepathgroup5.wanttoknow.R;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;
import com.yelp.clientlib.entities.Business;

import java.util.Collections;
import java.util.List;


public class PlanBusinessListFragment extends Fragment implements BusinessesAdapter.AdapterListener, ItemTouchHelperAdapter {
    private static final String TAG = PlanBusinessListFragment.class.getName();
    private PlansActivity listener;
    private Context context;
    private List<BusinessPO> listBusinesses;
    private RecyclerView rvBusinesses;
    private BusinessesAdapter adapter;
    private PlanPO planPO;

    // newInstance constructor for creating fragment with arguments
    public static PlanBusinessListFragment newInstance(PlanPO planPO) {
        PlanBusinessListFragment planBusinessListFragment = new PlanBusinessListFragment();
        planBusinessListFragment.planPO = planPO;
        return planBusinessListFragment;
    }

    // This event fires 1st, before creation of fragment or any views
    // The onAttach method is called when the Fragment instance is associated with an Activity.
    // This does not mean the Activity is fully initialized.
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        if (context instanceof PlansActivity) {
            this.listener = (PlansActivity) context;
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
        View v = inflater.inflate(R.layout.fragment_the_plan_business_list, parent, false);
        return v;
    }

    // This event is triggered soon after onCreateView().
    // onViewCreated() is only called if the view returned from onCreateView() is non-null.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        listBusinesses = planPO.getBusinessPOList();

        //Find the Recycler view
        rvBusinesses = (RecyclerView) view.findViewById(R.id.rvBusinessList);
        rvBusinesses.setHasFixedSize(true);

        //We create a LinearLayoutManager and associate this to our RecylerView
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvBusinesses.setLayoutManager(linearLayoutManager);

        //Set the adapter
        adapter = new BusinessesAdapter(context,listBusinesses, getFragmentManager(), this, this);

        rvBusinesses.setAdapter(adapter);
        ItemTouchHelper.Callback callback = new ItemTouchHelperCallback(this, "left");
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(rvBusinesses);

    }

    @Override
    public void addToPersonalList(Business business) {

    }

    @Override
    public void updateList(int position, String time) {
        BusinessPO businessPOTemp = listBusinesses.get(position);

        BusinessPO businessPO = ParseObject.createWithoutData(BusinessPO.class,businessPOTemp.getObjectId());
        businessPO.setPlanTime(time);
        businessPO.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e==null){
                    Toast.makeText(getContext(),"Time updated",Toast.LENGTH_SHORT);
                }
                else{
                    Toast.makeText(getContext(),"The system could not update the time",Toast.LENGTH_SHORT);
                }
            }
        });
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
