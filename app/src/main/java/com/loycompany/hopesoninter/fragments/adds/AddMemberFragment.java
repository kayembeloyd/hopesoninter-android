package com.loycompany.hopesoninter.fragments.adds;

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
import com.loycompany.hopesoninter.adapters.recyclerview.UsersRecyclerViewAdapter;
import com.loycompany.hopesoninter.authentication.Authenticator;
import com.loycompany.hopesoninter.classes.Community;
import com.loycompany.hopesoninter.classes.User;
import com.loycompany.hopesoninter.network.ServerRequest;
import com.loycompany.hopesoninter.network.URLs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AddMemberFragment extends Fragment {

    private RecyclerView recyclerView;
    private UsersRecyclerViewAdapter usersRecyclerViewAdapter;
    private List<User> users = new ArrayList<>();

    private ProgressBar progressBar;

    Handler handler;

    public AddMemberFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_member, container, false);

        handler = new Handler();

        progressBar = view.findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);

        recyclerView = view.findViewById(R.id.recycler_view);
        usersRecyclerViewAdapter = new UsersRecyclerViewAdapter(requireContext(), users);

        RecyclerView.LayoutManager linearLayoutManager = new LinearLayoutManager(requireContext(),
                LinearLayoutManager.VERTICAL,
                false);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(usersRecyclerViewAdapter);


        ServerRequest serverRequest = new ServerRequest(requireContext()) {
            @Override
            public void onServerResponse(String response) {
                progressBar.setVisibility(View.GONE);

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.has("data")){
                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++){
                            User user = new User(jsonArray.getJSONObject(i));

                            if (user.id != Authenticator.getAuthUser(requireContext()).id){
                                users.add(new User(jsonArray.getJSONObject(i)));
                            }
                        }

                        // update adapter
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                usersRecyclerViewAdapter.notifyDataSetChanged();
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
        serverRequest.addHeaderParam("Authorization",
                "Bearer " + Objects.requireNonNull(
                        Authenticator.getAuthUser(requireContext())).getToken());

        serverRequest.sendRequest(URLs.getApiAddress2() + "/users", Request.Method.GET);
        progressBar.setVisibility(View.VISIBLE);

        return view;
    }
}