package com.loycompany.hopesoninter.adapters.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.loycompany.hopesoninter.R;
import com.loycompany.hopesoninter.classes.CommunityMedia;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AddCommunityMediaRecyclerViewAdapter extends RecyclerView.Adapter<AddCommunityMediaRecyclerViewAdapter.ViewHolder>{

    public List<CommunityMedia> communityMedia;
    Context mContext;

    public AddCommunityMediaRecyclerViewAdapter() {
    }

    public AddCommunityMediaRecyclerViewAdapter(Context context, List<CommunityMedia> communityMedia) {
        this.communityMedia = communityMedia;
        this.mContext = context;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        // This is where stuff goes;
        Picasso.get()
                .load(communityMedia.get(position).getUri())
                .fit()
                .centerCrop()
                .placeholder(R.drawable.ic_waiting)
                .error(R.drawable.ic_error)
                .into(holder.imageView);
    }

    @NonNull
    @Override
    public AddCommunityMediaRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View cardView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.card_add_media, parent, false);

        return new AddCommunityMediaRecyclerViewAdapter.ViewHolder(cardView);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view);
        }
    }

    @Override
    public int getItemCount(){
        return communityMedia.size();
    }
}
