package com.codepathgroup5.utilities;

import android.app.IntentService;
import android.content.Intent;

import com.codepathgroup5.wanttoknow.FacebookClient;
import com.parse.FindCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.HashMap;
import java.util.List;

/**
 * Created by kristianss27
 */

public class NotificationService extends IntentService {
    private static final String TAG = NotificationService.class.getName();
    private FacebookClient facebookClient;

    // Must create a default constructor
    public NotificationService() {
        // Used to name the worker thread, important only for debugging.
        super("notification-servce");
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        final String planId = intent.getStringExtra("plan_id");
        String facebookId = intent.getStringExtra("facebook_id");

        //Push a notification
        ParseQuery<ParseUser> query = ParseQuery.getQuery(ParseUser.class);
        query.whereEqualTo("facebook_id", facebookId);
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if (objects != null) {
                    if (objects.get(0).getObjectId() != null) {
                        String channel = objects.get(0).getObjectId();
                        String name = objects.get(0).getString("name");

                        HashMap<String, String> payload = new HashMap<>();
                        payload.put("customData", ParseUser.getCurrentUser().getString("name"));
                        payload.put("channel", channel);
                        payload.put("owner", ParseUser.getCurrentUser().getObjectId());
                        payload.put("request", planId);
                        ParseCloud.callFunctionInBackground("pushChannelTest", payload);
                    }
                }


            }
        });
    }

}
