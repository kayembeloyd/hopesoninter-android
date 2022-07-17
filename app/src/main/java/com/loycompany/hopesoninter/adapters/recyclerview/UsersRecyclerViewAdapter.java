package com.loycompany.hopesoninter.adapters.recyclerview;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.loycompany.hopesoninter.CommunityMemberViewActivity;
import com.loycompany.hopesoninter.R;
import com.loycompany.hopesoninter.classes.User;

import java.util.List;

public class UsersRecyclerViewAdapter extends RecyclerView.Adapter<UsersRecyclerViewAdapter.ViewHolder>{

    public List<User> users;
    Context mContext;

    public UsersRecyclerViewAdapter() {
    }

    public UsersRecyclerViewAdapter(Context context, List<User> users) {
        this.users = users;
        this.mContext = context;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        // This is where stuff goes;
        holder.nameTextView.setText(this.users.get(position).name);
        holder.userLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, CommunityMemberViewActivity.class);
                mContext.startActivity(intent);
            }
        });
    }

    @NonNull
    @Override
    public UsersRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View cardView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.card_user, parent, false);

        return new UsersRecyclerViewAdapter.ViewHolder(cardView);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView nameTextView;
        LinearLayout userLinearLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.name_text_view);
            userLinearLayout = itemView.findViewById(R.id.user_linear_layout);
        }
    }

    @Override
    public int getItemCount(){
        return users.size();
    }
}
