package com.bitjini.my_twitter_app;

import android.util.Base64;
import android.util.Log;

import com.google.gson.Gson;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

public class TwitterAPI {

    private String twitterApiKey;
    private String twitterAPISecret;
    final static String TWITTER_TOKEN_URL = "https://api.twitter.com/oauth2/token";
    final static String TWITTER_STREAM_URL = "https://api.twitter.com/1.1/search/tweets.json?q=";

    public TwitterAPI(String twitterAPIKey, String twitterApiSecret){
        this.twitterApiKey = twitterAPIKey;
        this.twitterAPISecret = twitterApiSecret;
    }

    public ArrayList<Search> getTwitterTweets(String screenName) {
        ArrayList<Search> twitterTweetArrayList = null;
        try {
            String twitterUrlApiKey = URLEncoder.encode(twitterApiKey, "UTF-8");
            String twitterUrlApiSecret = URLEncoder.encode(twitterAPISecret, "UTF-8");
            String twitterKeySecret = twitterUrlApiKey + ":" + twitterUrlApiSecret;
            String twitterKeyBase64 = Base64.encodeToString(twitterKeySecret.getBytes(), Base64.NO_WRAP);

            TwitterAuthToken twitterAuthToken = getTwitterAuthToken(twitterKeyBase64);

            twitterTweetArrayList = getTwitterTweets(screenName, twitterAuthToken);

        } catch (UnsupportedEncodingException | IllegalStateException ex) {
            ex.printStackTrace();
        }
        return twitterTweetArrayList;
    }

    public ArrayList<Search> getTwitterTweets(String screenName,
                                              TwitterAuthToken twitterAuthToken) {
        ArrayList<Search> twitterTweetArrayList =new  ArrayList<Search>();

        if (twitterAuthToken != null && twitterAuthToken.token_type.equals("bearer")) {
            HttpGet httpGet = new HttpGet(TWITTER_STREAM_URL + screenName);
            httpGet.setHeader("Authorization", "Bearer " + twitterAuthToken.access_token);
            httpGet.setHeader("Content-Type", "application/json");
            HttpUtil httpUtil = new HttpUtil();
            String twitterTweets = httpUtil.getHttpResponse(httpGet);
            twitterTweetArrayList = convertJsonToTwitterTweet(twitterTweets);


        }
        return twitterTweetArrayList;
    }

    public TwitterAuthToken getTwitterAuthToken(String twitterKeyBase64) throws UnsupportedEncodingException {
        HttpPost httpPost = new HttpPost(TWITTER_TOKEN_URL);
        httpPost.setHeader("Authorization", "Basic " + twitterKeyBase64);
        httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");

        httpPost.setEntity(new StringEntity("grant_type=client_credentials"));

        HttpUtil httpUtil = new HttpUtil();
        String twitterJsonResponse = httpUtil.getHttpResponse(httpPost);
        return convertJsonToTwitterAuthToken(twitterJsonResponse);
    }

    private TwitterAuthToken convertJsonToTwitterAuthToken(String jsonAuth) {
        TwitterAuthToken twitterAuthToken = null;
        if (jsonAuth != null && jsonAuth.length() > 0) {
            try {
                Gson gson = new Gson();
                twitterAuthToken = gson.fromJson(jsonAuth, TwitterAuthToken.class);
            } catch (IllegalStateException ex) {
                ex.printStackTrace();
            }
        }
        return twitterAuthToken;
    }

    private ArrayList<Search> convertJsonToTwitterTweet(String twitterTweets) {
        ArrayList<Search> twitterTweetArrayList = new ArrayList<Search>();
        if (twitterTweets != null && twitterTweets.length() > 0) {
            try {
                try {

                    JSONObject jsonObject = new JSONObject(twitterTweets);


                    String status = jsonObject.getString("statuses");
                    Log.e("status..:",""+status);

                    // Status is Json Array
                    JSONArray arr = jsonObject.getJSONArray("statuses");

                    for(int i=0;i<arr.length();i++){
                        JSONObject c = arr.getJSONObject(i);

                        String date=arr.getJSONObject(i).getString("created_at");
                        String text=arr.getJSONObject(i).getString("text");

                        // User is jsonObject
                        JSONObject user = c.getJSONObject("user");

                        String name = user.getString("name");
                        String profile_image_url = user.getString("profile_image_url");
                        Log.e("profile_image_url..:",""+profile_image_url);

                        Search s=new Search();

                        s.setDateCreated(date);
                        s.setText(text);
                        s.setName(name);
                        s.setProfile_image_url(profile_image_url);
//
//                        twitterTweetArrayList.clear();
                        twitterTweetArrayList.add(s);

//                        Log.e("array list:",""+twitterTweetArrayList);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();}

            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }
        return twitterTweetArrayList;
    }
    private class TwitterAuthToken {
        String token_type;
        String access_token;
    }
}