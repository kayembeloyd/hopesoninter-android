package com.loycompany.hopesoninter.network;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public abstract class ServerRequest {
    private Map<String, String> headerParams = new HashMap<>();
    private Map<String, String> bodyParams = new HashMap<>();
    private String contentType = "null";
    private JSONObject rawBody = new JSONObject();

    private String requestUrl;
    private int duration = 60000;
    Context context;

    public String realUrl;

    public ServerRequest(Context context){
        this.context = context;
    }

    public abstract void onServerResponse(String response);
    public abstract void onServerError(String error);

    public void sendRequest(String url, int method) {
        setRequestUrl(url);

        StringRequest stringRequest = new StringRequest(method, requestUrl,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        onServerResponse(response);
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        onServerError(error.toString());
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError{
                return bodyParams;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return headerParams;
            }

            /*
            @Override
            public byte[] getBody() throws AuthFailureError {
                return rawBody.toString().getBytes();
            }*/

            @Override
            public String getBodyContentType() {
                if (contentType.equals("null")){
                    return "application/x-www-form-urlencoded; charset=UTF-8";
                }

                return contentType;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(duration, 5, 2f));

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);

        realUrl = stringRequest.getUrl();
    }



    public String getRequestUrl() {
        return requestUrl;
    }
    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public int getDuration() {
        return duration;
    }
    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void addParam(String key, String value) {
        bodyParams.put(key, value);
    }
    public void addHeaderParam(String key, String value) {
        headerParams.put(key, value);
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public void setRawBody(JSONObject rawBody) {
        this.rawBody = rawBody;
    }
}
