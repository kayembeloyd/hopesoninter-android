package com.loycompany.hopesoninter.adapters.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.loycompany.hopesoninter.R;
import com.loycompany.hopesoninter.classes.WebnarEvent;

import java.util.List;

public class CalendarWebnarEventsRecyclerViewAdapter extends RecyclerView.Adapter<CalendarWebnarEventsRecyclerViewAdapter.ViewHolder>{

    public List<WebnarEvent> webnarEvents;
    Context mContext;

    public CalendarWebnarEventsRecyclerViewAdapter() {
    }

    public CalendarWebnarEventsRecyclerViewAdapter(Context context, List<WebnarEvent> webnarEvents) {
        this.webnarEvents = webnarEvents;
        this.mContext = context;
    }

    @Override
    public void onBindViewHolder(@NonNull final CalendarWebnarEventsRecyclerViewAdapter.ViewHolder holder, final int position) {
        // This is where stuff goes;
    }


    @Override
    public CalendarWebnarEventsRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View cardView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.card_calendar_webnar_event, parent, false);

        return new CalendarWebnarEventsRecyclerViewAdapter.ViewHolder(cardView);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    @Override
    public int getItemCount(){
        return webnarEvents.size();
    }
}
