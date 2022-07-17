package com.loycompany.hopesoninter.fragments.dashboard;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.loycompany.hopesoninter.DashboardActivity;
import com.loycompany.hopesoninter.R;
import com.loycompany.hopesoninter.adapters.recyclerview.MembersRecyclerViewAdapter;
import com.loycompany.hopesoninter.authentication.Authenticator;
import com.loycompany.hopesoninter.classes.CommunityMember;
import com.loycompany.hopesoninter.classes.User;
import com.loycompany.hopesoninter.network.ServerRequest;
import com.loycompany.hopesoninter.network.URLs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MembersFragment extends Fragment {

    private RecyclerView membersRecyclerView;
    private MembersRecyclerViewAdapter membersRecyclerViewAdapter;
    private List<User> members = new ArrayList<>();

    private DashboardActivity dashboardActivity;

    public MembersFragment() {
    }

    private void fakeAdd(){
        for (int i = 0; i < 5; i++){
            this.members.add(new User());
        }
    }

    private ProgressBar progressBar;

    Handler handler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dashboardActivity = (DashboardActivity) requireActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard_members, container, false);

        handler = new Handler();

        progressBar = view.findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);

        membersRecyclerView = view.findViewById(R.id.members_recycler_view);
        membersRecyclerViewAdapter = new MembersRecyclerViewAdapter(requireContext(), members);

        RecyclerView.LayoutManager linearLayoutManager = new LinearLayoutManager(requireContext(),
                LinearLayoutManager.VERTICAL,
                false);

        membersRecyclerView.setLayoutManager(linearLayoutManager);
        membersRecyclerView.setItemAnimator(new DefaultItemAnimator());
        membersRecyclerView.setAdapter(membersRecyclerViewAdapter);

        // Call server
        ServerRequest serverRequest = new ServerRequest(requireContext()) {
            @Override
            public void onServerResponse(String response) {
                int jk=0;

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.has("data")){
                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++){
                            User user = new User(jsonArray.getJSONObject(i));

                            if (user.id != Authenticator.getAuthUser(requireContext()).id){
                                members.add(new User(jsonArray.getJSONObject(i)));
                            }
                        }

                        // update adapter
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                membersRecyclerViewAdapter.notifyDataSetChanged();
                            }
                        });
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        ServerRequest serverRequest1 = new ServerRequest(requireContext()) {
                            @Override
                            public void onServerResponse(String response) {
                                int j=0;
                                progressBar.setVisibility(View.GONE);

                                try {
                                    JSONObject jsonObject = new JSONObject(response);

                                    if (jsonObject.has("data")){
                                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                                        for (int i = 0; i < jsonArray.length(); i++){
                                            User user = new User(jsonArray.getJSONObject(i));

                                            if (user.id != Authenticator.getAuthUser(requireContext()).id){
                                                members.add(new User(jsonArray.getJSONObject(i)));
                                            }
                                        }

                                        // update adapter
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                membersRecyclerViewAdapter.notifyDataSetChanged();
                                            }
                                        });
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                // Update the adapters

                            }

                            @Override
                            public void onServerError(String error) {
                                Toast.makeText(requireContext(), "Error: " + error, Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                            }
                        };

                        serverRequest1.addHeaderParam("Accept", "application/json");
                        serverRequest1.addHeaderParam("Authorization",
                                "Bearer " + Objects.requireNonNull(
                                        Authenticator.getAuthUser(requireContext())).getToken());

                        serverRequest1.sendRequest(URLs.getApiAddress2() + "/communities/" + dashboardActivity.getSelectedCommunity().ID + "/membership-requests", Request.Method.GET);
                    }
                });
            }

            @Override
            public void onServerError(String error) {
                Toast.makeText(requireContext(), "Error: " + error, Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        };

        serverRequest.addHeaderParam("Accept", "application/json");
        serverRequest.addHeaderParam("Authorization",
                "Bearer " + Objects.requireNonNull(
                        Authenticator.getAuthUser(requireContext())).getToken());

        int f = dashboardActivity.getSelectedCommunity().ID;

        serverRequest.sendRequest(URLs.getApiAddress2() + "/communities/" + dashboardActivity.getSelectedCommunity().ID + "/users", Request.Method.GET);
        progressBar.setVisibility(View.VISIBLE);

        return view;
    }
}