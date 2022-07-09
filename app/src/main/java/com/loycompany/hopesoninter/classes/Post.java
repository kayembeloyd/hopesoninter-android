package com.loycompany.hopesoninter.classes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Post {
    public List<PostMedia> media;

    public Post() {
        media = new ArrayList<>();
    }

    public JSONObject postJSONFormat;

    public String title,
            shortDescription,
            longDescription;

    public int userID, id, communityID;

    public Post(JSONObject post) throws JSONException {
        postJSONFormat = post;

        id = !post.isNull("id") ? post.getInt("id") : 0;
        title = !post.isNull("title") ? post.getString("title") : "";
        shortDescription = !post.isNull("short_description") ? post.getString("short_description") : "";
        longDescription = !post.isNull("long_description") ? post.getString("long_description") : "";
        communityID = !post.isNull("community_id") ? post.getInt("community_id") : 0;

        JSONArray jsonArray = !post.isNull("post_media") ? post.getJSONArray("post_media") : new JSONArray();
        media = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++){
            media.add(new PostMedia(jsonArray.getJSONObject(i)));
        }
    }
}
