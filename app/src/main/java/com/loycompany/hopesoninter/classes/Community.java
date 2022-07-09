package com.loycompany.hopesoninter.classes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Community {
    public int ID;
    public String name;
    public String location;
    public int leaderID;
    public int forumID;

    public List<CommunityMedia> communityMedia;

    public Community(){

    }

    public Community(JSONObject jsonObject) throws JSONException {
        ID = !jsonObject.isNull("id") ? jsonObject.getInt("id") : 0;
        name = !jsonObject.isNull("name") ? jsonObject.getString("name") : "";
        location = !jsonObject.isNull("location") ? jsonObject.getString("location") : "";
        leaderID = !jsonObject.isNull("leader_id") ? jsonObject.getInt("leader_id") : 0;
        forumID = !jsonObject.isNull("forum_id") ? jsonObject.getInt("forum_id") : 0;

        JSONArray jsonArray = !jsonObject.isNull("community_media") ? jsonObject.getJSONArray("community_media") : new JSONArray();
        communityMedia = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++){
            communityMedia.add(new CommunityMedia(jsonArray.getJSONObject(i)));
        }

    }
    /*
    {
        "id": 1,
        "name": "Mulanje Community",
        "location": "Mulanle lat:12 lon:13",
        "leader_id": null,
        "forum_id": 1,
        "created_at": "2022-06-01T17:58:29.000000Z",
        "updated_at": "2022-06-01T17:58:29.000000Z",
        "community_media": [
            {
                "id": 1,
                "name": "community_profile_image",
                "url": "public/media/posts/1_0_post_media.png",
                "type": null,
                "community_id": 1,
                "created_at": "2022-06-02T03:36:18.000000Z",
                "updated_at": "2022-06-02T03:36:18.000000Z"
            },
            {
                "id": 2,
                "name": "community_profile_image",
                "url": "public/media/communities/1_0_community_media.png",
                "type": null,
                "community_id": 1,
                "created_at": "2022-06-03T03:17:10.000000Z",
                "updated_at": "2022-06-03T03:17:10.000000Z"
            }
       ]
    }
    */
}
