package com.loycompany.hopesoninter.network;

public class URLs {
    private static final String env = "000WEBHOST";

    private static final String HOST_LOCAL = "http://192.168.153.58";
    private static final String PORT_LOCAL = "8081";

    public static String getApiAddress2(){
        return "https://hopesoninter.herokuapp.com/api";
    }

    public static String getApiAddress(){
        if (env.equals("HEROKU")){
            return "https://active-wind-testing.herokuapp.com/api";
        } else if (env.equals("000WEBHOST")){
            return "https://hopesoninter.000webhostapp.com/api";
        }

        return HOST_LOCAL + ":" + PORT_LOCAL + "/api";
    }

    public static String getStorageAddress(){
        if (env.equals("HEROKU")){
            return "https://activewindbucket.s3.eu-west-2.amazonaws.com";
        } else if (env.equals("000WEBHOST")){
            return "https://hopesoninter.000webhostapp.com/";
        }

        // https://activewindbucket.s3.eu-west-2.amazonaws.com/images/1_1_post_image.jpg
        // http://192.168.153.58:8081/storage/media/communities/1_0_community_media.png

        // public/images/2_1_post_image.jpg
        return HOST_LOCAL + ":" + PORT_LOCAL + "/";
    }
}