package com.bitjini.my_twitter_app;

/**
 * Created by bitjini on 26/3/16.
 */


public class Search {


    private String DateCreated;


    private String Text;


    private String profile_image_url;


    private String name;






    public String getDateCreated() {
        return DateCreated;
    }

    public void setDateCreated(String dateCreated) {
        DateCreated = dateCreated;
    }


    public String getText() {
        return Text;
    }

    public void setText(String text) {
        Text = text;

    }

    public String getProfile_image_url() {
        return profile_image_url;
    }

    public void setProfile_image_url(String profile_image_url) {
        this.profile_image_url = profile_image_url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public String  toString(){
        return getText();
    }
}
