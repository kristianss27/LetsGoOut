package com.codepathgroup5.models;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
public class FbFriend {

    @SerializedName("name")
    String name;
    @SerializedName("id")
    String id;
    @SerializedName("picture")
    String picture;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
