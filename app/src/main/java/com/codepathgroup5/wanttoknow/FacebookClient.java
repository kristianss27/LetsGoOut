package com.codepathgroup5.wanttoknow;

import com.codepathgroup5.models.FbFriend;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;

import java.util.List;


public class FacebookClient {
    public static final String TAG = FacebookClient.class.getSimpleName();
    public static FacebookClient facebookClient;
    private AccessToken accessToken;
    private AccessTokenTracker accessTokenTracker;
    private CallbackManager callBackManager;
    private List<FbFriend> fbFriendList;

    public static FacebookClient newInstance() {
        if(facebookClient==null){
            facebookClient = new FacebookClient();
        }
        facebookClient.processAccessToken();
        return facebookClient;
    }

    public void initializeFacebookClient(){
        callBackManager = WantToKnowApplication.getCallBackManager();
    }

    public void processAccessToken(){
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(
                    AccessToken oldAccessToken,
                    AccessToken currentAccessToken) {
                accessToken = currentAccessToken;
            }
        };
        accessTokenTracker.startTracking();
    }

    public CallbackManager getCallBackManager() {
        return callBackManager;
    }

    public void setCallBackManager(CallbackManager callBackManager) {
        this.callBackManager = callBackManager;
    }

    public void stopAccessTokenTracking(){
        accessTokenTracker.stopTracking();
    }

    public boolean isTracking(){
        return accessTokenTracker.isTracking();
    }

    public AccessTokenTracker getAccessTokenTracker() {
        return accessTokenTracker;
    }

    public void setAccessTokenTracker(AccessTokenTracker accessTokenTracker) {
        this.accessTokenTracker = accessTokenTracker;
    }

    public AccessToken getAccessToken() {
        return accessToken;
    }

    public List<FbFriend> getFbFriendList() {
        return fbFriendList;
    }

    public void setFbFriendList(List<FbFriend> fbFriendList) {
        this.fbFriendList = fbFriendList;
    }

    public void setAccessToken(AccessToken accessToken) {
        this.accessToken = accessToken;
    }

    public String getCurrentUserId(){
        return accessToken.getUserId();
    }


}
