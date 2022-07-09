package com.loycompany.hopesoninter.classes;

import android.net.Uri;

import org.json.JSONException;
import org.json.JSONObject;

public class PostMedia {
    public int ID;
    public String name;
    public String url;
    public String type;
    public int postID;

    Uri uri;

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public PostMedia(){

    }

    public PostMedia(JSONObject jsonObject) throws JSONException {
        ID = !jsonObject.isNull("id") ? jsonObject.getInt("id") : 0;
        name = !jsonObject.isNull("name") ? jsonObject.getString("name") : "";
        url = !jsonObject.isNull("url") ? jsonObject.getString("url") : "";
        type = !jsonObject.isNull("type") ? jsonObject.getString("type") : "";
        postID = !jsonObject.isNull("community_id") ? jsonObject.getInt("post_id") : 0;
    }
}
