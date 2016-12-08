package com.codepathgroup5.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.codepathgroup5.listeners.ChildFragmentListener;
import com.codepathgroup5.models.Message;
import com.codepathgroup5.wanttoknow.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class GeneralQuestionFragment extends Fragment{
    private static final String TAG = GeneralQuestionFragment.class.getName();
    @BindView(R.id.etDescription) EditText etDescription;
    @BindView(R.id.tvCharacterCounter) TextView tvCharacterCounter;
    @BindView(R.id.btnNext) Button btnNext;
    @BindView(R.id.tvLabel) TextView tvLabel;
    @BindView(R.id.radioGroupPlanning)
    RadioGroup radioGroupPlanning;
    private Unbinder unbinder;
    private String title;
    private int page;
    private Message message;

    // newInstance constructor for creating fragment with arguments
    public static GeneralQuestionFragment newInstance(int page, String title) {
        GeneralQuestionFragment generalQuestionFragment = new GeneralQuestionFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        generalQuestionFragment.setArguments(args);
        return generalQuestionFragment;
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
        View view = inflater.inflate(R.layout.fragment_planning_for, container, false);
        unbinder = ButterKnife.bind(this,view);

        TextView tvLabel = (TextView) view.findViewById(R.id.tvLabel);
        tvLabel.setText(getTitle());

        return view;
    }

    // This event is triggered soon after onCreateView().
    // onViewCreated() is only called if the view returned from onCreateView() is non-null.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnNext.setEnabled(true);
        etDescription.setEnabled(true);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String purpouse = "";
                //Getting the value from the radio group
                int selectedId = radioGroupPlanning.getCheckedRadioButtonId();
                RadioButton radioButton = (RadioButton) radioGroupPlanning.findViewById(selectedId);
                String radioText = radioButton.getText().toString();
                purpouse = radioText;
                message.setPlanPurpouse(purpouse);
                //Getting the value from the EditText
                String description = etDescription.getText().toString();
                if(description!=null && !description.equalsIgnoreCase("")){
                    message.setDescription(description);
                    Log.d(TAG,"User has selected: "+radioText);
                    Log.d(TAG,"User has described: "+message.getDescription());
                    ChildFragmentListener listener = (ChildFragmentListener) getParentFragment();
                    listener.nextQuestion(GeneralQuestionFragment.this,message);
                }
                else{
                    Toast.makeText(getContext(),"Give a short description",Toast.LENGTH_SHORT).show();
                }

            }
        });


        etDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // this will show characters remaining
                int counter = 106 - s.toString().length();
                tvCharacterCounter.setText(Integer.toString(counter));
                if (counter < 0) {
                    tvCharacterCounter.setTextColor(Color.RED);
                    btnNext.setEnabled(false);
                }
                else {
                    btnNext.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_free:
                if (checked)
                    etDescription.setEnabled(true);
                    break;
            case R.id.radio_hangout:
                if (checked)
                    etDescription.setEnabled(false);
                    break;
            case R.id.radio_meetup:
                if (checked)
                    etDescription.setEnabled(false);
                    break;
            case R.id.radio_party:
                if (checked)
                    etDescription.setEnabled(false);
                    break;
        }
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
