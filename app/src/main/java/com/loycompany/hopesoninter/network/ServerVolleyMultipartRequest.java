package com.loycompany.hopesoninter.network;
import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public abstract class ServerVolleyMultipartRequest {
    private Map<String, String> headerParams = new HashMap<>();
    private Map<String, String> bodyParams = new HashMap<>();
    private Map<String, VolleyMultipartRequest.DataPart> dataPartParams = new HashMap<>();

    private int duration = 60000;

    public void addParam(String key, String value) {
        bodyParams.put(key, value);
    }
    public void addHeaderParam(String key, String value) {
        headerParams.put(key, value);
    }
    public void addDataPartParam(String key, VolleyMultipartRequest.DataPart dataPart) {
        dataPartParams.put(key, dataPart);
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public abstract void onServerResponse(String response);
    public abstract void onServerError(String error);

    private Context context;

    public ServerVolleyMultipartRequest(Context context){
        this.context = context;
    }

    public void sendRequest(String url){
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST,
                url,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        onServerResponse(new String(response.data));
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        onServerError(error.toString());
                    }
                }) {

            @Override
            protected Map<String, DataPart> getByteData() {
                return dataPartParams;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return bodyParams;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return headerParams;
            }
        };

        volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(duration, 5, 2f));

        //adding the request to volley
        Volley.newRequestQueue(context).add(volleyMultipartRequest);
    }
}