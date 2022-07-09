package com.loycompany.hopesoninter.fragments.dashboard;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.loycompany.hopesoninter.R;
import com.loycompany.hopesoninter.adapters.recyclerview.PostsEditRecyclerViewAdapter;
import com.loycompany.hopesoninter.classes.Post;
import com.loycompany.hopesoninter.classes.PostMedia;

import java.util.ArrayList;
import java.util.List;

public class PostsFragment extends Fragment {
    private RecyclerView postsRecyclerView;
    private PostsEditRecyclerViewAdapter postsEditRecyclerViewAdapter;
    private List<Post> posts = new ArrayList<>();

    public PostsFragment() {
        fakeAdd();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void fakeAdd(){
        for (int i = 0; i < 5; i++){
            if (i % 2 == 0){
                Post post = new Post();
                for(int j = 0; j < i; j++){
                    post.media.add(new PostMedia());
                }

                this.posts.add(post);
            } else {
                this.posts.add(new Post());
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard_posts, container, false);

        postsRecyclerView = view.findViewById(R.id.posts_recycler_view);
        postsEditRecyclerViewAdapter = new PostsEditRecyclerViewAdapter(requireContext(), posts, "NULL");

        RecyclerView.LayoutManager linearLayoutManager = new LinearLayoutManager(requireContext(),
                LinearLayoutManager.VERTICAL,
                false);

        postsRecyclerView.setLayoutManager(linearLayoutManager);
        postsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        postsRecyclerView.setAdapter(postsEditRecyclerViewAdapter);

        return view;
    }
}