package com.codepathgroup5.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.codepathgroup5.listeners.ChildFragmentListener;
import com.codepathgroup5.models.Message;
import com.codepathgroup5.wanttoknow.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class HowContactFragment extends Fragment {
    private static final String TAG = HowContactFragment.class.getName();

    @BindView(R.id.tvLabel)
    TextView tvLabel;
    @BindView(R.id.btnNext)
    Button btnNext;
    @BindView(R.id.etEmail)
    EditText etEmail;
    @BindView(R.id.etPhone) EditText etPhone;
    @BindView(R.id.cbAcceptCalls)
    CheckBox cbAcceptCalls;

    private Unbinder unbinder;
    private String title;
    private int page;
    private Message message;

    // newInstance constructor for creating fragment with arguments
    public static HowContactFragment newInstance(int page, String title) {
        HowContactFragment howContactFragment = new HowContactFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        howContactFragment.setArguments(args);
        return howContactFragment;
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
        View view = inflater.inflate(R.layout.fragment_how_contact, container, false);
        unbinder = ButterKnife.bind(this, view);

        TextView tvLabel = (TextView) view.findViewById(R.id.tvLabel);
        tvLabel.setText(getPage() + " -- " + getTitle());

        return view;
    }

    // This event is triggered soon after onCreateView().
    // onViewCreated() is only called if the view returned from onCreateView() is non-null.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnNext.setEnabled(true);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "On Click");
                ChildFragmentListener listener = (ChildFragmentListener) getParentFragment();

                String email = etEmail.getText().toString();
                String phone = etPhone.getText().toString();
                boolean acceptCall = cbAcceptCalls.isChecked();

                message.setContactEmail(email);
                message.setContactPhone(phone);
                message.setPermitCalls(acceptCall);

                listener.nextQuestion(HowContactFragment.this, message);
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
