package com.codepathgroup5.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

/**
 * Created by kristianss27
 */
@ParseClassName("Business")
public class BusinessPO extends ParseObject {

    public BusinessPO() {
        super();
    }

    public String yelpId() {return getString("yelp_id");}

    public void setYelp(String yelpId){
        put("yelp_id",yelpId);
    }

    public String getName() {return getString("name");}

    public void setName(String name){
        put("name",name);
    }

    public String getImgUrl() {return getString("img_url");}

    public void setImgUrl(String imgUrl){
        put("img_url",imgUrl);
    }

    public String getPlanTime() {return getString("plan_time");}

    public void setPlanTime(String planTime){
        put("plan_time",planTime);
    }

    public String getLocation(){return getString("location");}

    public void setLocation(String location) { put("location",location);}

    public ParseUser getUser(){
        return getParseUser("owner");
    }

    public void setOwner(ParseUser user){
        put("owner",user);
    }

    public void setYelpMobileUrl(String yelpMobileUrl) {
        put("yelp_mobile_url",yelpMobileUrl);
    }

    public String getYelpMobileUrl(){
        return getString("yelp_mobile_url");
    }



}
