package com.codepathgroup5.utilities;

import com.parse.ParseUser;

import java.util.Calendar;


public class Utility {
    //Yelp API keys
    public static final String CONSUMER_KEY = "jb2ewcmX-nDKzUZ3pgqfFQ";
    public static final String CONSUMER_SECRET= "yS1-dm5oeWefh5-6SFi5cEOthJQ";
    public static final String TOKEN = "YNYKJqiBLIE7n5-FMcGf1CNupBZNBES_";
    public static final String TOKEN_SECRET = "l7wh4z0_iXBV3_0qR33dpMNKqhU";

    public String getFormatDate(Calendar calendar, boolean sumMonth){
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        if(sumMonth){
            month++;
        }
        int year = calendar.get(Calendar.YEAR);
        String date = month+"/"+day+"/"+year;
        return date;
    }

    //Parse method to Log out an user
    public void logOut(){
        ParseUser.logOut();
    }
}
