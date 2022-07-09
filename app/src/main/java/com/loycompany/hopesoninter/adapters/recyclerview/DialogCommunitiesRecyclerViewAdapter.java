package com.loycompany.hopesoninter.adapters.recyclerview;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.loycompany.hopesoninter.DashboardActivity;
import com.loycompany.hopesoninter.R;
import com.loycompany.hopesoninter.classes.Community;

import java.util.List;

public class DialogCommunitiesRecyclerViewAdapter extends RecyclerView.Adapter<DialogCommunitiesRecyclerViewAdapter.ViewHolder>{

    public List<Community> communities;
    Context context;
    Dialog dialog;

    private String parentActivityName;

    public DialogCommunitiesRecyclerViewAdapter() {
    }

    public void setParentActivityName(String parentActivityName) {
        this.parentActivityName = parentActivityName;
    }

    public DialogCommunitiesRecyclerViewAdapter(Context context, List<Community> communities) {
        this.communities = communities;
        this.context = context;
    }

    public DialogCommunitiesRecyclerViewAdapter(Context context, List<Community> communities, Dialog dialog){
        this.dialog = dialog;
        this.communities = communities;
        this.context = context;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        // This is where stuff goes;
        holder.nameTextView.setText(communities.get(position).name);

        holder.nameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                switch (parentActivityName){
                    case "DashboardActivity":
                        DashboardActivity dashboardActivity = (DashboardActivity) context;
                        dashboardActivity.setSelectedCommunity(communities.get(holder.getAdapterPosition()));
                        break;
                    case "MyAccountActivity":
                    default:
                        break;
                }

                dialog.dismiss();
                Toast.makeText(context,"Dismissed.. with " + communities.get(holder.getAdapterPosition()).name, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @NonNull
    @Override
    public DialogCommunitiesRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View cardView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.card_dialog_community, parent, false);

        return new DialogCommunitiesRecyclerViewAdapter.ViewHolder(cardView);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView nameTextView;
        public ViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.name_text_view);
        }
    }

    @Override
    public int getItemCount(){
        return communities.size();
    }
}
