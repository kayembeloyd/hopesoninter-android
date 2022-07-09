package com.loycompany.hopesoninter.adapters.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.loycompany.hopesoninter.R;
import com.loycompany.hopesoninter.classes.CommunityData;

import java.util.List;

public class DataRecyclerViewAdapter extends RecyclerView.Adapter<DataRecyclerViewAdapter.ViewHolder>{

    public List<CommunityData> communityData;
    Context context;

    public DataRecyclerViewAdapter() {
    }

    public DataRecyclerViewAdapter(Context context, List<CommunityData> communityData) {
        this.communityData = communityData;
        this.context = context;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        // This is where stuff goes;
    }

    @NonNull
    @Override
    public DataRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View cardView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.card_dashboard_overview_data, parent, false);

        return new DataRecyclerViewAdapter.ViewHolder(cardView);
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
