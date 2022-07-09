package com.loycompany.hopesoninter.adapters.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.loycompany.hopesoninter.R;
import com.loycompany.hopesoninter.classes.ImportantContact;

import java.util.List;

public class ImportantContactsRecyclerViewAdapter extends RecyclerView.Adapter<ImportantContactsRecyclerViewAdapter.ViewHolder>{

    public List<ImportantContact> importantContacts;
    Context mContext;

    public ImportantContactsRecyclerViewAdapter() {
    }

    public ImportantContactsRecyclerViewAdapter(Context context, List<ImportantContact> importantContacts) {
        this.importantContacts = importantContacts;
        this.mContext = context;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        // This is where stuff goes;
    }

    @NonNull
    @Override
    public ImportantContactsRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View cardView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.card_dashboard_important_contacts_contact, parent, false);

        return new ImportantContactsRecyclerViewAdapter.ViewHolder(cardView);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    @Override
    public int getItemCount(){
        return importantContacts.size();
    }
}
