package com.codepathgroup5.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.codepathgroup5.parse.UserParse;
import com.codepathgroup5.wanttoknow.FacebookClient;
import com.codepathgroup5.wanttoknow.R;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StartActivity extends FragmentActivity {
    private static final String TAG = "StartActivity";
    @BindView(R.id.etEmail) EditText etEmail;
    @BindView(R.id.etPassword) EditText etPassword;
    @BindView(R.id.login_button) Button loginButton;
    FacebookClient facebookClient;
    private UserParse userParse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        ButterKnife.bind(this);
        facebookClient = FacebookClient.newInstance();
        userParse = new UserParse();
    }

    public void signUp(View view) {

        if(!etEmail.getText().toString().equalsIgnoreCase("") && !etPassword.getText().toString().equalsIgnoreCase("")){
            String userName = etEmail.getText().toString().toLowerCase();
            String email = userName;
            String password = etPassword.getText().toString().toLowerCase();

            //Declare a variable to get the response
            final String[] result = new String[2];
            //Initializes the ParseUser Object using its properties
            final ParseUser user = new ParseUser();
            //User name is required
            user.setUsername(userName);
            //Password is required on signup
            user.setPassword(password);
            //The emaill user is optional
            user.setEmail(email);
            //User type (Default:customer)
            user.put("type","customer");
            //other fields can be set just like with ParseObject
            //user.put("phone", getPhone());

            user.signUpInBackground(new SignUpCallback() {
                public void done(ParseException e) {
                    if (e == null) {
                        //Connection successful
                        Log.d(TAG,"Connection successful");
                        //Lets add a channel
                        //Save the current Installation to Parse.
                        ParseInstallation parseInstallation = ParseInstallation.getCurrentInstallation();
                        parseInstallation.put("channel",ParseUser.getCurrentUser().getObjectId());
                        parseInstallation.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null) {
                                    //Connection successful
                                    Log.d("ParseInstallation","Parse Installation successful");
                                } else {
                                    // Getting error message from the exception
                                    String errorMessage = e.getMessage();
                                    Log.d("ParseInstallation","ERROR"+e.getMessage());
                                }
                            }
                        });
                        Toast.makeText(StartActivity.this,"It was simple. Now enjoy it!",Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(StartActivity.this, PlansActivity.class);
                        startActivity(intent);
                    } else {
                        // Getting error message from the exception
                        String errorMessage = e.getMessage();
                        Log.d(TAG,"ERROR"+e.getMessage());
                        Toast.makeText(getApplicationContext(),errorMessage,Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
        else{
            Toast.makeText(this,R.string.warning_signup,Toast.LENGTH_LONG).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("Facebook Log out","Results: "+requestCode+","+resultCode+","+data);
        facebookClient.getCallBackManager().onActivityResult(requestCode, resultCode, data);
        facebookClient.stopAccessTokenTracking();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("Facebook Log out","Token Traking Destroyed");
        facebookClient.stopAccessTokenTracking();
    }
}