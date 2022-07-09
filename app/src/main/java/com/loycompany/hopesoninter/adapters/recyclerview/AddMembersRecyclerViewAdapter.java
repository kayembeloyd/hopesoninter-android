package com.loycompany.hopesoninter.adapters.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.loycompany.hopesoninter.R;
import com.loycompany.hopesoninter.classes.User;

import java.util.List;

public class AddMembersRecyclerViewAdapter extends RecyclerView.Adapter<AddMembersRecyclerViewAdapter.ViewHolder>{

    public List<User> users;
    Context mContext;

    public AddMembersRecyclerViewAdapter() {
    }

    public AddMembersRecyclerViewAdapter(Context context, List<User> users) {
        this.users = users;
        this.mContext = context;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        // This is where stuff goes;
    }

    @NonNull
    @Override
    public AddMembersRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View cardView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.card_add_members_user, parent, false);

        return new AddMembersRecyclerViewAdapter.ViewHolder(cardView);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    @Override
    public int getItemCount(){
        return users.size();
    }
}
