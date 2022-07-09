package com.loycompany.hopesoninter.classes;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class User {
    public int id;
    public String name;
    public String email;
    public String access;
    public String phoneNumbers;
    public int requestingMembershipCommunityID;
    public int communityID;
    public int meetingID;

    public List<UserMedia> userMedia;

    private String token;

    public JSONObject userJSONFormat;

    public User() {
        userJSONFormat = new JSONObject();
    }

    public User(JSONObject user) throws JSONException {
        userJSONFormat = user;

        id = !user.isNull("id") ? user.getInt("id") : 0;
        name = !user.isNull("name") ? user.getString("name") : "";
        email = !user.isNull("email") ? user.getString("email") : "";
        access = !user.isNull("access") ? user.getString("access") : "";
        phoneNumbers = !user.isNull("phone_numbers") ? user.getString("phone_numbers") : "";
        requestingMembershipCommunityID =  !user.isNull("requesting_membership_community_id") ? user.getInt("requesting_membership_community_id") : 0;

        communityID = !user.isNull("community_id") ? user.getInt("community_id") : 0;
        meetingID = !user.isNull("meeting_id") ? user.getInt("meeting_id") : 0;

        //setToken();
        if (user.has("token")){
            this.setToken(!user.isNull("token") ? user.getString("token") : "");
        }

        JSONArray jsonArray = !user.isNull("user_media") ? user.getJSONArray("user_media") : new JSONArray();
        userMedia = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++){
            userMedia.add(new UserMedia(jsonArray.getJSONObject(i)));
        }
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) throws JSONException {
        this.token = token;
        userJSONFormat.put("token", token);
    }
}
