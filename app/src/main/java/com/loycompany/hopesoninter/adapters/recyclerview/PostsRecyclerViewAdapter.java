package com.loycompany.hopesoninter.adapters.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.loycompany.hopesoninter.MainActivity;
import com.loycompany.hopesoninter.R;
import com.loycompany.hopesoninter.adapters.pager.PostMediaViewPagerAdapter;
import com.loycompany.hopesoninter.classes.Post;

import java.util.List;

public class PostsRecyclerViewAdapter extends RecyclerView.Adapter<PostsRecyclerViewAdapter.ViewHolder>{

    public List<Post> posts;
    String parentActivityName;
    Context mContext;

    public PostsRecyclerViewAdapter() {
    }

    public PostsRecyclerViewAdapter(Context context, List<Post> posts, String parentActivityName) {
        this.posts = posts;
        this.mContext = context;
        this.parentActivityName = parentActivityName;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        // This is where stuff goes;
        PostMediaViewPagerAdapter postImagesViewPagerAdapter = new PostMediaViewPagerAdapter(this.mContext, this.posts.get(position).media);
        holder.viewPager.setAdapter(postImagesViewPagerAdapter);

        if (this.posts.get(position).media.size() <= 0) holder.viewPager.setVisibility(View.GONE);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // switch fragment
                if (parentActivityName.equals("MainActivity")){
                    MainActivity mainActivity = (MainActivity) mContext;
                    mainActivity.switchToPostViewFragment(posts.get(holder.getAdapterPosition()));
                } else if (parentActivityName.equals("SomeOtherActivity")){
                    //
                }
            }
        });

        holder.viewPager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "clicked image", Toast.LENGTH_SHORT).show();
            }
        });

        holder.postTitle.setText(posts.get(position).title);
        holder.postShortDescription.setText(posts.get(position).shortDescription);
        holder.postCommunity.setText(String.valueOf(posts.get(position).communityID));
    }

    @NonNull
    @Override
    public PostsRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View cardView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.card_post, parent, false);

        return new PostsRecyclerViewAdapter.ViewHolder(cardView);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ViewPager viewPager;
        CardView cardView;

        TextView postTitle, postShortDescription, postCommunity;
        public ViewHolder(View itemView) {
            super(itemView);
            viewPager = itemView.findViewById(R.id.view_pager);
            cardView = itemView.findViewById(R.id.card_view);

            postTitle = itemView.findViewById(R.id.post_title);
            postShortDescription = itemView.findViewById(R.id.post_short_description);
            postCommunity = itemView.findViewById(R.id.post_community);
        }
    }

    @Override
    public int getItemCount(){
        return posts.size();
    }
}
