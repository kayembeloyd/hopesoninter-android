package com.loycompany.hopesoninter.classes;

import android.net.Uri;

import org.json.JSONException;
import org.json.JSONObject;

public class CommunityMedia {
    public int ID;
    public String name;
    public String url;
    public String type;
    public int communityID;

    Uri uri;

    public CommunityMedia(JSONObject jsonObject) throws JSONException {
        ID = !jsonObject.isNull("id") ? jsonObject.getInt("id") : 0;
        name = !jsonObject.isNull("name") ? jsonObject.getString("name") : "";
        url = !jsonObject.isNull("url") ? jsonObject.getString("url") : "";
        type = !jsonObject.isNull("type") ? jsonObject.getString("type") : "";
        communityID = !jsonObject.isNull("community_id") ? jsonObject.getInt("community_id") : 0;
    }

    public CommunityMedia(){

    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public Uri getUri() {
        return uri;
    }
}
