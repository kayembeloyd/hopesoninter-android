package com.loycompany.hopesoninter.fragments.post;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.loycompany.hopesoninter.MainActivity;
import com.loycompany.hopesoninter.R;
import com.loycompany.hopesoninter.adapters.pager.PostMediaViewPagerAdapter;
import com.loycompany.hopesoninter.adapters.recyclerview.PostsRecyclerViewAdapter;
import com.loycompany.hopesoninter.classes.Post;
import com.loycompany.hopesoninter.classes.PostMedia;

import java.util.ArrayList;
import java.util.List;

public class PostViewFragment extends Fragment {

    private RecyclerView relatedPostsRecyclerView;
    private PostsRecyclerViewAdapter postsRecyclerViewAdapter;
    private List<Post> relatedPosts = new ArrayList<>();

    private ViewPager viewPager;

    private ImageView backImageView;

    private Post post;
    private String parentActivityName;

    public PostViewFragment() {
        // Required empty public constructor
        fakeAdd();
    }

    public PostViewFragment(Post post, String parentActivityName) {
        this.post = post;
        this.parentActivityName = parentActivityName;
        this.relatedPosts = new ArrayList<>();

        fakeAdd();
    }

    private void fakeAdd() {
        for (int i = 0; i < 3; i++){
            Post post = new Post();
            for (int j = 0; j < i; j++){
                post.media.add(new PostMedia());
            }
            this.relatedPosts.add(post);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_view, container, false);

        relatedPostsRecyclerView = view.findViewById(R.id.related_posts_recycler_view);
        postsRecyclerViewAdapter = new PostsRecyclerViewAdapter(requireContext(), relatedPosts, this.parentActivityName);

        RecyclerView.LayoutManager postsRecyclerViewLayoutManager = new LinearLayoutManager(requireContext(),
                LinearLayoutManager.VERTICAL,
                false);

        relatedPostsRecyclerView.setLayoutManager(postsRecyclerViewLayoutManager);
        relatedPostsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        relatedPostsRecyclerView.setAdapter(postsRecyclerViewAdapter);

        viewPager = view.findViewById(R.id.view_pager);

        PostMediaViewPagerAdapter postMediaViewPagerAdapter = new PostMediaViewPagerAdapter(requireContext(), this.post.media);
        viewPager.setAdapter(postMediaViewPagerAdapter);

        if (post.media.size() <= 0) viewPager.setVisibility(View.GONE);

        backImageView = view.findViewById(R.id.back_image_view);

        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // call onBackPress on parent activity
                if (parentActivityName.equals("MainActivity")){
                    MainActivity mainActivity = (MainActivity) requireActivity();
                    mainActivity.onBackPressed();
                }
            }
        });

        return view;
    }
}