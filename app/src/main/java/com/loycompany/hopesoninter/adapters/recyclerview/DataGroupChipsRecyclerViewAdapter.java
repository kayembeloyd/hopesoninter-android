package com.loycompany.hopesoninter.adapters.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.loycompany.hopesoninter.R;
import com.loycompany.hopesoninter.classes.CommunityDataGroup;

import java.util.List;

public class DataGroupChipsRecyclerViewAdapter extends RecyclerView.Adapter<DataGroupChipsRecyclerViewAdapter.ViewHolder>{

    public List<CommunityDataGroup> communityData;
    Context context;

    public DataGroupChipsRecyclerViewAdapter() {
    }

    public DataGroupChipsRecyclerViewAdapter(Context context, List<CommunityDataGroup> communityData) {
        this.communityData = communityData;
        this.context = context;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        // This is where stuff goes;
    }

    @NonNull
    @Override
    public DataGroupChipsRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View cardView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.card_dashboard_overview_data_group_chip, parent, false);

        return new DataGroupChipsRecyclerViewAdapter.ViewHolder(cardView);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    @Override
    public int getItemCount(){
        return communityData.size();
    }
}
