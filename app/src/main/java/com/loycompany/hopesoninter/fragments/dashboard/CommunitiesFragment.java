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
import com.loycompany.hopesoninter.adapters.recyclerview.CommunitiesRecyclerViewAdapter;
import com.loycompany.hopesoninter.classes.Community;
import com.loycompany.hopesoninter.network.ServerRequest;
import com.loycompany.hopesoninter.network.URLs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class CommunitiesFragment extends Fragment {

    private RecyclerView communitiesDashboardRecyclerView;
    private CommunitiesRecyclerViewAdapter communitiesRecyclerViewAdapter;
    private List<Community> communities = new ArrayList<>();

    private ProgressBar progressBar;

    Handler handler;

    public CommunitiesFragment() {
        // Required empty public constructor
        // fakeAdd();
    }

    private void fakeAdd() {
        for (int i = 0; i < 20; i++){
            communities.add(new Community());
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard_communities, container, false);

        handler = new Handler();
        communities = new ArrayList<>();

        progressBar = view.findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);

        communitiesDashboardRecyclerView = view.findViewById(R.id.communities_dashboard_recycler_view);
        communitiesRecyclerViewAdapter = new CommunitiesRecyclerViewAdapter(requireContext(), communities);

        RecyclerView.LayoutManager communitiesDashboardRecyclerViewLayoutManager = new LinearLayoutManager(requireContext(),
                LinearLayoutManager.VERTICAL,
                false);

        communitiesDashboardRecyclerView.setLayoutManager(communitiesDashboardRecyclerViewLayoutManager);
        communitiesDashboardRecyclerView.setItemAnimator(new DefaultItemAnimator());
        communitiesDashboardRecyclerView.setAdapter(communitiesRecyclerViewAdapter);

        ServerRequest serverRequest = new ServerRequest(requireContext()) {
            @Override
            public void onServerResponse(String response) {
                progressBar.setVisibility(View.GONE);

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.has("data")){
                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++){
                            communities.add(new Community(jsonArray.getJSONObject(i)));
                        }

                        // update adapter
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                communitiesRecyclerViewAdapter.notifyDataSetChanged();
                                if (communities.size() > 0){
                                    ((DashboardActivity) requireContext()).setSelectedCommunity(communities.get(0));
                                }
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onServerError(String error) {
                Toast.makeText(requireContext(), "Error: " + error, Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        };

        serverRequest.addHeaderParam("Accept", "application/json");
        serverRequest.sendRequest(URLs.getApiAddress2() + "/communities", Request.Method.GET);
        progressBar.setVisibility(View.VISIBLE);

        return view;
    }
}