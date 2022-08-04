package com.loycompany.hopesoninter.adapters.recyclerview;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.loycompany.hopesoninter.CommunityMemberViewActivity;
import com.loycompany.hopesoninter.R;
import com.loycompany.hopesoninter.authentication.Authenticator;
import com.loycompany.hopesoninter.classes.User;
import com.loycompany.hopesoninter.network.ServerRequest;
import com.loycompany.hopesoninter.network.URLs;

import java.util.List;
import java.util.Objects;

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

        if (users.get(position).requestingMembershipCommunityID == 0){
            holder.requestingMembershipOptionsLinearLayout.setVisibility(View.GONE);
            holder.requestingMembershipTextView.setVisibility(View.GONE);
        } else {

            if (Authenticator.getAuthUser(mContext).access.equals("admin")){
                holder.requestingMembershipOptionsLinearLayout.setVisibility(View.VISIBLE);
            } else if (Authenticator.getAuthUser(mContext).access.equals("community_leader")){
                if (Authenticator.getAuthUser(mContext).communityID == users.get(position).requestingMembershipCommunityID){
                    holder.requestingMembershipOptionsLinearLayout.setVisibility(View.VISIBLE);
                }
            } else {
                holder.requestingMembershipOptionsLinearLayout.setVisibility(View.GONE);
            }

            holder.requestingMembershipTextView.setVisibility(View.VISIBLE);

            holder.acceptButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Accept
                    ServerRequest serverRequest = new ServerRequest(mContext) {
                        @Override
                        public void onServerResponse(String response) {
                            Toast.makeText(mContext, "Server has responded: " + response, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onServerError(String error) {
                            Toast.makeText(mContext, "Error: " + error, Toast.LENGTH_SHORT).show();
                        }
                    };

                    serverRequest.addHeaderParam("Accept", "application/json");
                    serverRequest.addHeaderParam("Authorization",
                            "Bearer " + Objects.requireNonNull(
                                    Authenticator.getAuthUser(mContext)).getToken());

                    serverRequest.sendRequest(URLs.getApiAddress2() +
                            "/communities/" +
                            users.get(position).requestingMembershipCommunityID +
                            "/users/" +
                            users.get(position).id +
                            "/accept-membership", Request.Method.PUT);
                }
            });
        }
    }

    @NonNull
    @Override
    public UsersRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View cardView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.card_user, parent, false);

        return new UsersRecyclerViewAdapter.ViewHolder(cardView);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView nameTextView, requestingMembershipTextView;
        LinearLayout userLinearLayout, requestingMembershipOptionsLinearLayout;
        Button acceptButton;
        public ViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.name_text_view);
            userLinearLayout = itemView.findViewById(R.id.user_linear_layout);

            requestingMembershipTextView = itemView.findViewById(R.id.requesting_membership_textview);
            requestingMembershipOptionsLinearLayout = itemView.findViewById(R.id.requesting_membership_options_linear_layout);

            acceptButton = itemView.findViewById(R.id.accept_button);
        }
    }

    @Override
    public int getItemCount(){
        return users.size();
    }
}
