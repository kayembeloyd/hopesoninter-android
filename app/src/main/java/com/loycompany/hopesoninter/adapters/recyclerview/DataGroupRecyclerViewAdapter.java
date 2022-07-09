package com.loycompany.hopesoninter.adapters.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.loycompany.hopesoninter.R;
import com.loycompany.hopesoninter.classes.CommunityDataGroup;

import java.util.List;

public class DataGroupRecyclerViewAdapter extends RecyclerView.Adapter<DataGroupRecyclerViewAdapter.ViewHolder>{

    public List<CommunityDataGroup> communityDataGroups;
    Context context;

    DataRecyclerViewAdapter dataRecyclerViewAdapter;

    public DataGroupRecyclerViewAdapter() {
    }

    public DataGroupRecyclerViewAdapter(Context context, List<CommunityDataGroup> communityDataGroups) {
        this.communityDataGroups = communityDataGroups;
        this.context = context;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        // This is where stuff goes;
        dataRecyclerViewAdapter = new DataRecyclerViewAdapter(context, communityDataGroups.get(position).communityData);

        RecyclerView.LayoutManager linearLayoutManager = new LinearLayoutManager(context,
                LinearLayoutManager.HORIZONTAL,
                false);

        holder.recyclerView.setLayoutManager(linearLayoutManager);
        holder.recyclerView.setItemAnimator(new DefaultItemAnimator());
        holder.recyclerView.setAdapter(dataRecyclerViewAdapter);
    }

    @NonNull
    @Override
    public DataGroupRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View cardView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.card_dashboard_overview_data_group, parent, false);

        return new DataGroupRecyclerViewAdapter.ViewHolder(cardView);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        RecyclerView recyclerView;
        public ViewHolder(View itemView) {
            super(itemView);
            recyclerView = itemView.findViewById(R.id.recycler_view);
        }
    }

    @Override
    public int getItemCount(){
        return communityDataGroups.size();
    }
}
