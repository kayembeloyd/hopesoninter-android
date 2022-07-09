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
import com.loycompany.hopesoninter.adapters.recyclerview.WebnarEventsEditRecyclerViewAdapter;
import com.loycompany.hopesoninter.classes.WebnarEvent;

import java.util.ArrayList;
import java.util.List;

public class EventsFragment extends Fragment {

    private RecyclerView eventsRecyclerView;
    private WebnarEventsEditRecyclerViewAdapter webnarEventsEditRecyclerViewAdapter;
    private List<WebnarEvent> events = new ArrayList<>();

    public EventsFragment() {
        fakeAdd();
    }

    private void fakeAdd(){
        for (int i = 0; i < 5; i++){
            this.events.add(new WebnarEvent());
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard_events, container, false);

        eventsRecyclerView = view.findViewById(R.id.events_recycler_view);
        webnarEventsEditRecyclerViewAdapter = new WebnarEventsEditRecyclerViewAdapter(requireContext(), events);

        RecyclerView.LayoutManager linearLayoutManager = new LinearLayoutManager(requireContext(),
                LinearLayoutManager.VERTICAL,
                false);

        eventsRecyclerView.setLayoutManager(linearLayoutManager);
        eventsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        eventsRecyclerView.setAdapter(webnarEventsEditRecyclerViewAdapter);

        return view;
    }
}