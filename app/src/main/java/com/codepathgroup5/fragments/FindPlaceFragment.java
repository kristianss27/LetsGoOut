package com.codepathgroup5.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.codepathgroup5.listeners.ChildFragmentListener;
import com.codepathgroup5.models.Message;
import com.codepathgroup5.wanttoknow.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by kristianss27
 */

public class FindPlaceFragment extends Fragment {
    private static final String TAG = FindPlaceFragment.class.getName();
    @BindView(R.id.tvLabel) TextView tvLabel;
    @BindView(R.id.etPostalCode)
    EditText etPostalCode;

    @BindView(R.id.btnNext) Button btnNext;
    @BindView(R.id.btnBack) Button btnBack;

    private Unbinder unbinder;
    private String title;
    private int page;
    private Message message;

    // newInstance constructor for creating fragment with arguments
    public static FindPlaceFragment newInstance(int page, String title) {
        FindPlaceFragment findPlaceFragment = new FindPlaceFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        findPlaceFragment.setArguments(args);
        return findPlaceFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("someInt", 0);
        title = getArguments().getString("someTitle");
        message = new Message();
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_where, container, false);
        unbinder = ButterKnife.bind(this,view);

        tvLabel.setText(getTitle());

        return view;
    }

    // This event is triggered soon after onCreateView().
    // onViewCreated() is only called if the view returned from onCreateView() is non-null.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String postalCode = etPostalCode.getText().toString();
                if(postalCode!=null && !postalCode.equalsIgnoreCase("")){
                    Log.d(TAG,"Adding a postal code: "+postalCode);
                    message.setPostalCode(postalCode);
                    ChildFragmentListener listener = (ChildFragmentListener) getParentFragment();
                    listener.nextQuestion(FindPlaceFragment.this,message);
                }
                else{
                    Toast.makeText(getContext(),"You need to add a postal code",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    // When binding a fragment in onCreateView, set the views to null in onDestroyView.
    // ButterKnife returns an Unbinder on the initial binding that has an unbind method to do this automatically.
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

}