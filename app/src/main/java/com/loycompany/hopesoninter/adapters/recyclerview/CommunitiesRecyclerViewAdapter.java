package com.loycompany.hopesoninter.adapters.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.loycompany.hopesoninter.DashboardActivity;
import com.loycompany.hopesoninter.R;
import com.loycompany.hopesoninter.classes.Community;
import com.loycompany.hopesoninter.helpers.DataParser;
import com.loycompany.hopesoninter.network.URLs;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommunitiesRecyclerViewAdapter extends RecyclerView.Adapter<CommunitiesRecyclerViewAdapter.ViewHolder>{

    public List<Community> communities;
    Context context;

    public CommunitiesRecyclerViewAdapter() {
    }

    public CommunitiesRecyclerViewAdapter(Context context, List<Community> communities) {
        this.communities = communities;
        this.context = context;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        // This is where stuff goes;
        holder.nameTextView.setText(this.communities.get(position).name);

        //example = public/media/communities/1_0_community_media.png
        // http://192.168.153.58:8081/storage/media/communities/1_0_community_media.png

        if (communities.get(position).communityMedia.size() > 0){
            String path = communities.get(position).communityMedia.get(0).url;
            List<String> choppedPath = DataParser.javaexplode("/", path);
            choppedPath.set(0, "storage");
            String correctedPath = DataParser.javaimpload(choppedPath, "/");

            Picasso.get()
                    .load(path)
                    .fit()
                    .centerCrop()
                    .placeholder(R.drawable.ic_waiting)
                    .error(R.drawable.ic_error)
                    .into(holder.circleImageView);
        } else {
            Picasso.get()
                    .load("www.nothing.cc")
                    .fit()
                    .centerCrop()
                    .placeholder(R.drawable.ic_waiting)
                    .error(R.drawable.ic_error)
                    .into(holder.circleImageView);
        }

        holder.communityRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DashboardActivity dashboardActivity = (DashboardActivity) context;
                dashboardActivity.setSelectedCommunity(communities.get(holder.getAdapterPosition()));
            }
        });
    }

    @NonNull
    @Override
    public CommunitiesRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View cardView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.card_dashboard_communities_community, parent, false);

        return new CommunitiesRecyclerViewAdapter.ViewHolder(cardView);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView nameTextView, membersCountTextView;
        CircleImageView circleImageView;
        RelativeLayout communityRelativeLayout;

        public ViewHolder(View itemView) {
            super(itemView);

            nameTextView = itemView.findViewById(R.id.name_text_view);
            membersCountTextView = itemView.findViewById(R.id.members_count_text_view);
            communityRelativeLayout = itemView.findViewById(R.id.community_relative_layout);
            circleImageView = itemView.findViewById(R.id.circle_image_view);
        }
    }

    @Override
    public int getItemCount(){
        return communities.size();
    }
}
