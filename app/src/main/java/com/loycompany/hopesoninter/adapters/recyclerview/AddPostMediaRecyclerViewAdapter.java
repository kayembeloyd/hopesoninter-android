package com.loycompany.hopesoninter.adapters.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.loycompany.hopesoninter.R;
import com.loycompany.hopesoninter.classes.PostMedia;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AddPostMediaRecyclerViewAdapter extends RecyclerView.Adapter<AddPostMediaRecyclerViewAdapter.ViewHolder>{

    public List<PostMedia> postMedia;
    Context mContext;

    public AddPostMediaRecyclerViewAdapter() {
    }

    public AddPostMediaRecyclerViewAdapter(Context context, List<PostMedia> postMedia) {
        this.postMedia = postMedia;
        this.mContext = context;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        // This is where stuff goes;
        Picasso.get()
                .load(postMedia.get(position).getUri())
                .fit()
                .centerCrop()
                .placeholder(R.drawable.ic_waiting)
                .error(R.drawable.ic_error)
                .into(holder.imageView);
    }

    @NonNull
    @Override
    public AddPostMediaRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View cardView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.card_add_media, parent, false);

        return new AddPostMediaRecyclerViewAdapter.ViewHolder(cardView);
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
        return postMedia.size();
    }
}
