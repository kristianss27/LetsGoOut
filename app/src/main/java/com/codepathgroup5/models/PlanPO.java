package com.codepathgroup5.models;

import android.util.Log;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by kristianss27
 */

@ParseClassName("Plan")
public class PlanPO extends ParseObject {

    public PlanPO() {
        super();
    }

    public String getPlanPurpouse() {return getString("purpouse");}

    public void setPurpouse(String purpouse){
        put("purpouse",purpouse);
    }

    public String getDescription() {
        return getString("description");
    }

    public void setDescription(String description) {
        put("description",description);
    }

    public Date getWhenDate() {
        return getDate("when_date");
    }

    public void setWhenDate(Date whenDate) {
        put("when_date",whenDate);
    }

    public void setListId(String listId) { put("list_id",listId);}

    public String getListId(){ return getString("list_id"); }

    public ParseUser getUser(){
        return getParseUser("owner");
    }

    public void setOwner(ParseUser user){
        put("owner",user);
    }

    public ArrayList<BusinessPO> getBusinessPOList(){
        return (ArrayList<BusinessPO>) get("yelp_list");
    }

    public List<String> getBusinessesList() {
        List<String> list = getList("business_list");
        if(list!=null){
            Log.d(PlanPO.class.getName(),"getBusinessesList() - List filled with "+list.size()+" results");
            return list;
        }
        Log.d(PlanPO.class.getName(),"getBusinessesList() - List empty");
        return new ArrayList<>();
    }

    public void setBusinessesList(List<String> businessesList) {
        addAllUnique("business_list", businessesList);
    }

    public String getOwnerName(){
        return getString("name");
    }

    public void setOwnerName(String ownerName) {
        put("owner_name",ownerName);
    }
}

