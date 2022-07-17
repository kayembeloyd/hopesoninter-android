package com.loycompany.hopesoninter.adapters.recyclerview;

import android.content.Context;
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
import com.loycompany.hopesoninter.R;
import com.loycompany.hopesoninter.authentication.Authenticator;
import com.loycompany.hopesoninter.classes.CommunityMember;
import com.loycompany.hopesoninter.classes.User;
import com.loycompany.hopesoninter.network.ServerRequest;
import com.loycompany.hopesoninter.network.URLs;

import java.util.List;
import java.util.Objects;

public class MembersRecyclerViewAdapter extends RecyclerView.Adapter<MembersRecyclerViewAdapter.ViewHolder>{

    public List<User> communityMembers;
    Context mContext;

    public MembersRecyclerViewAdapter() {
    }

    public MembersRecyclerViewAdapter(Context context, List<User> communityMembers) {
        this.communityMembers = communityMembers;
        this.mContext = context;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        // This is where stuff goes;
        holder.nameTextView.setText(communityMembers.get(position).name);

        if (communityMembers.get(position).requestingMembershipCommunityID == 0){
            holder.buttonsLinearLayout.setVisibility(View.GONE);
            holder.infoTextView.setVisibility(View.GONE);
        }

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
                        communityMembers.get(position).requestingMembershipCommunityID +
                        "/users/" +
                        communityMembers.get(position).id +
                        "/accept-membership", Request.Method.PUT);
            }
        });

        holder.rejectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


    }

    @NonNull
    @Override
    public MembersRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View cardView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.card_dashboard_members_member, parent, false);

        return new MembersRecyclerViewAdapter.ViewHolder(cardView);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView nameTextView, infoTextView;
        LinearLayout buttonsLinearLayout;
        Button acceptButton, rejectButton;

        public ViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.name_text_view);
            infoTextView = itemView.findViewById(R.id.info_text_view);

            buttonsLinearLayout = itemView.findViewById(R.id.buttons_linear_layout);
            acceptButton = itemView.findViewById(R.id.accept_button);
            rejectButton = itemView.findViewById(R.id.reject_button);
        }
    }

    @Override
    public int getItemCount(){
        return communityMembers.size();
    }
}
