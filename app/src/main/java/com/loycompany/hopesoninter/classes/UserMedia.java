package com.loycompany.hopesoninter.classes;

import org.json.JSONException;
import org.json.JSONObject;

public class UserMedia {
    public String name;
    public String type;
    public String url;
    public int userID;
    public int id;

    public UserMedia(JSONObject jsonObject) throws JSONException {
        id = jsonObject.getInt("id");
        name = jsonObject.getString("name");
        type = jsonObject.getString("type");
        url = jsonObject.getString("url");
        userID = jsonObject.getInt("user_id");
    }
}
