package com.codepathgroup5.models;

import android.util.Log;

import com.parse.GetCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by kristianss27
 */

@ParseClassName("PersonalListPO")
public class PersonalListPO extends ParseObject {

    private String idGenerated;

    public PersonalListPO(){
        super();
    }

    public String getIdGenerated(){ return idGenerated; }

    public void setIdGenerated(String idGenerated){ this.idGenerated = idGenerated; }

    public String getListName() {
        return getString("list_name");
    }

    public void setListName(String listName) {
        put("list_name",listName);
    }

    public List<String> getBusinessesList() {
        List<String> list = getList("business_list");
        if(list!=null){
            Log.d(PersonalListPO.class.getName(),"getBusinessesList() - List filled with "+list.size()+" results");
            return list;
        }
        Log.d(PersonalListPO.class.getName(),"getBusinessesList() - List empty");
        return new ArrayList<>();
    }

    public void setBusinessesList(List<String> businessesList) {
            addAllUnique("business_list", businessesList);
    }

    public void setBusiness(String businessId){
        add("business_list",businessId);
    }

    public ParseUser getUser(){
        return getParseUser("owner");
    }

    public void setOwner(ParseUser user){
        put("owner",user);
    }

    public void saveInBackGroundPO(){
        ParseUser parseUser = ParseUser.getCurrentUser();
        if(parseUser!=null){
            Log.d(PersonalListPO.class.getName(),"User registered before saveInBackGround");
        }else{
            Log.d(PersonalListPO.class.getName(),"User no registered before saveInBackGround");
        }
        // Set the current user, assuming a user is signed in
        setOwner(parseUser);
        // Immediately save the data asynchronously
        pinInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d(PersonalListPO.class.getName(),"Personal List registered on Local Data Store DB");
                }
                else{
                    e.printStackTrace();
                }
            }
        });
        saveInBackground(new SaveCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    Log.d(PersonalListPO.class.getName(),"Personal List registered on Heroku DB");
                }
                else{
                    e.printStackTrace();
                }
            }
        });
    }

    public void pinInBackGroundPO(){
        ParseUser parseUser = ParseUser.getCurrentUser();
        if(parseUser!=null){
            Log.d(PersonalListPO.class.getName(),"User registered before saveInBackGround");
        }else{
            Log.d(PersonalListPO.class.getName(),"User no registered before saveInBackGround");
        }
        // Set the current user, assuming a user is signed in
        setOwner(parseUser);
        // Immediately save the data asynchronously
        pinInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d(PersonalListPO.class.getName(),"Personal List registered on Local Data Store");
                    setIdGenerated(getObjectId());
                }
                else{
                    e.printStackTrace();
                }
            }
        }
        );
    }

    public void unPinObject(){
        unpinInBackground();
    }

    public void getPersonalListPOTest(String objectId){
        ParseQuery<PersonalListPO> query = ParseQuery.getQuery(PersonalListPO.class);
        // Specify the object id
        query.getInBackground(objectId, new GetCallback<PersonalListPO>() {
            public void done(PersonalListPO item, ParseException e) {
                if (e == null) {
                    // Access data using the `get` methods for the object
                    String listName = item.getListName();
                    // Access special values that are built-in to each object
                    String objectId = item.getObjectId();
                    Date updatedAt = item.getUpdatedAt();
                    Date createdAt = item.getCreatedAt();

                    Log.d(PersonalListPO.class.getName(),"Getting PersonalListPO. ObjectId: "+objectId);

                } else {
                    e.printStackTrace();
                    Log.d(PersonalListPO.class.getName(),"Getting PersonalListPO. Exception: "+e.toString());
                }
            }
        });
    }

    public PersonalListPO createWithoutData(){
        PersonalListPO personalListReference = ParseObject.createWithoutData(PersonalListPO.class, getObjectId());
        return  personalListReference;
    }

}

