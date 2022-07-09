package com.loycompany.hopesoninter.fragments.main;

import android.content.Context;
import android.os.Bundle;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.loycompany.hopesoninter.DashboardActivity;
import com.loycompany.hopesoninter.R;
import com.loycompany.hopesoninter.adapters.recyclerview.PostsRecyclerViewAdapter;
import com.loycompany.hopesoninter.adapters.recyclerview.WebnarEventsRecyclerViewAdapter;
import com.loycompany.hopesoninter.authentication.Authenticator;
import com.loycompany.hopesoninter.classes.Community;
import com.loycompany.hopesoninter.classes.Post;
import com.loycompany.hopesoninter.classes.PostMedia;
import com.loycompany.hopesoninter.classes.WebnarEvent;
import com.loycompany.hopesoninter.network.ServerRequest;
import com.loycompany.hopesoninter.network.URLs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeFragment extends Fragment {

    private Context context;
    private DrawerLayout drawerLayout;

    private RecyclerView postsRecyclerView;
    private PostsRecyclerViewAdapter postsRecyclerViewAdapter;
    private List<Post> posts = new ArrayList<>();

    private RecyclerView webnarEventsRecyclerView;
    private WebnarEventsRecyclerViewAdapter webnarEventsRecyclerViewAdapter;
    private List<WebnarEvent> webnarEvents = new ArrayList<>();

    private ImageView menuImageView;

    private CircleImageView contextMenuCircleImageView;
    private View contextMenuRefView;
    private PopupMenu contextMenuPopup;

    private ProgressBar progressBar;
    private Handler handler;

    public HomeFragment() {
        // Required empty public constructor
        fakeAdd();
    }

    public HomeFragment(Context context, DrawerLayout drawerLayout) {
        this.context = context;
        this.drawerLayout = drawerLayout;

        this.posts = new ArrayList<>();
        this.webnarEvents = new ArrayList<>();

        // Fake add
        fakeAdd();
    }

    private void fakeAdd(){
        for (int i = 0; i < 5; i++){
            if (i % 2 == 0){
                // Post post = new Post();
                for(int j = 0; j < i; j++){
                    // post.media.add(new PostMedia());
                }

                // this.posts.add(post);
            } else {
                // this.posts.add(new Post());
                this.webnarEvents.add(new WebnarEvent());
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_home, container, false);

        handler = new Handler();

        progressBar = view.findViewById(R.id.progress_bar);

        menuImageView = view.findViewById(R.id.menu_image_view);
        menuImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawerLayout.isOpen()) {
                    drawerLayout.close();
                } else {
                    drawerLayout.open();
                }
            }
        });

        postsRecyclerView = view.findViewById(R.id.posts_recycler_view);
        posts = new ArrayList<>();
        postsRecyclerViewAdapter = new PostsRecyclerViewAdapter(requireContext(), posts, "MainActivity");

        RecyclerView.LayoutManager postsRecyclerViewLayoutManager = new LinearLayoutManager(requireContext(),
                LinearLayoutManager.VERTICAL,
                false);

        postsRecyclerView.setLayoutManager(postsRecyclerViewLayoutManager);
        postsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        postsRecyclerView.setAdapter(postsRecyclerViewAdapter);

        webnarEventsRecyclerView = view.findViewById(R.id.webnar_events_recycler_view);
        webnarEventsRecyclerViewAdapter = new WebnarEventsRecyclerViewAdapter(requireContext(), webnarEvents);

        RecyclerView.LayoutManager webnarEventsRecyclerViewLayoutManager = new LinearLayoutManager(requireContext(),
                LinearLayoutManager.HORIZONTAL,
                false);

        webnarEventsRecyclerView.setLayoutManager(webnarEventsRecyclerViewLayoutManager);
        webnarEventsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        webnarEventsRecyclerView.setAdapter(webnarEventsRecyclerViewAdapter);

        contextMenuCircleImageView = view.findViewById(R.id.context_menu_circle_image_view);
        contextMenuRefView = view.findViewById(R.id.context_menu_ref_view);

        if (Authenticator.getAuthUser(requireContext()) == null){
            contextMenuCircleImageView.setVisibility(View.GONE);
        }

        contextMenuCircleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contextMenuPopup = new PopupMenu(requireContext(), contextMenuRefView);
                MenuInflater inflater = contextMenuPopup.getMenuInflater();
                inflater.inflate(R.menu.main_context_menu_home_user, contextMenuPopup.getMenu());
                contextMenuPopup.show();

                contextMenuPopup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.refresh:
                                Toast.makeText(requireContext(), "refreshing", Toast.LENGTH_SHORT).show();
                                
                                return true;
                            case R.id.my_account:
                                Toast.makeText(requireContext(), "my account", Toast.LENGTH_SHORT).show();
                                return true;
                            default:
                                return false;
                        }
                    }
                });
            }
        });

        ServerRequest serverRequest = new ServerRequest(requireContext()) {
            @Override
            public void onServerResponse(String response) {
                progressBar.setVisibility(View.GONE);

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.has("data")){
                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++){
                            posts.add(new Post(jsonArray.getJSONObject(i)));
                        }

                        // update adapter
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                postsRecyclerViewAdapter.notifyDataSetChanged();
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
        serverRequest.sendRequest(URLs.getApiAddress() + "/posts", Request.Method.GET);
        progressBar.setVisibility(View.VISIBLE);


        return view;
    }
}