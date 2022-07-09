package com.loycompany.hopesoninter.fragments.dashboard;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.loycompany.hopesoninter.R;
import com.loycompany.hopesoninter.adapters.recyclerview.ImportantContactsRecyclerViewAdapter;
import com.loycompany.hopesoninter.classes.ImportantContact;

import java.util.ArrayList;
import java.util.List;

public class ImportantContactsFragment extends Fragment {

    private RecyclerView importantContactsRecyclerView;
    private ImportantContactsRecyclerViewAdapter importantContactsRecyclerViewAdapter;
    private List<ImportantContact> importantContacts = new ArrayList<>();

    public ImportantContactsFragment() {
        fakeAdd();
    }

    private void fakeAdd(){
        for (int i = 0; i < 5; i++){
            this.importantContacts.add(new ImportantContact());
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard_important_contacts, container, false);

        importantContactsRecyclerView = view.findViewById(R.id.important_contacts_recycler_view);
        importantContactsRecyclerViewAdapter = new ImportantContactsRecyclerViewAdapter(requireContext(), importantContacts);

        RecyclerView.LayoutManager linearLayoutManager = new LinearLayoutManager(requireContext(),
                LinearLayoutManager.VERTICAL,
                false);

        importantContactsRecyclerView.setLayoutManager(linearLayoutManager);
        importantContactsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        importantContactsRecyclerView.setAdapter(importantContactsRecyclerViewAdapter);

        return view;
    }
}