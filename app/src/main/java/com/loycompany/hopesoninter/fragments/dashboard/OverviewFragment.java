package com.loycompany.hopesoninter.fragments.dashboard;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.loycompany.hopesoninter.DashboardActivity;
import com.loycompany.hopesoninter.R;
import com.loycompany.hopesoninter.adapters.recyclerview.DataGroupChipsRecyclerViewAdapter;
import com.loycompany.hopesoninter.adapters.recyclerview.DataGroupRecyclerViewAdapter;
import com.loycompany.hopesoninter.classes.CommunityData;
import com.loycompany.hopesoninter.classes.CommunityDataGroup;

import java.util.ArrayList;
import java.util.List;


public class OverviewFragment extends Fragment {

    private RecyclerView overviewDataChipsRecyclerView;
    private DataGroupChipsRecyclerViewAdapter dataGroupChipsRecyclerViewAdapter;
    private List<CommunityDataGroup> communityDataGroups = new ArrayList<>();

    private RecyclerView overviewDataGroupRecyclerView;
    private DataGroupRecyclerViewAdapter dataGroupRecyclerViewAdapter;

    private TextView textView11;

    private DashboardActivity dashboardActivity;

    public OverviewFragment() {
        // Required empty public constructor
        fakeAdd();
    }

    private void fakeAdd() {
        for (int i = 0; i < 10; i++){
            CommunityDataGroup communityDataGroup = new CommunityDataGroup();
            communityDataGroup.communityData = new ArrayList<>();
            for (int j = 0; j < i; j++){
                communityDataGroup.communityData.add(new CommunityData());
            }
            communityDataGroups.add(communityDataGroup);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dashboardActivity = (DashboardActivity) requireActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard_overview, container, false);

        overviewDataChipsRecyclerView = view.findViewById(R.id.overview_data_chips_recycler_view);
        dataGroupChipsRecyclerViewAdapter = new DataGroupChipsRecyclerViewAdapter(requireContext(), communityDataGroups);
        RecyclerView.LayoutManager linearLayoutManager = new LinearLayoutManager(requireContext(),
                LinearLayoutManager.HORIZONTAL,
                false);
        overviewDataChipsRecyclerView.setLayoutManager(linearLayoutManager);
        overviewDataChipsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        overviewDataChipsRecyclerView.setAdapter(dataGroupChipsRecyclerViewAdapter);



        overviewDataGroupRecyclerView = view.findViewById(R.id.overview_data_group_recycler_view);
        dataGroupRecyclerViewAdapter = new DataGroupRecyclerViewAdapter(requireContext(), communityDataGroups);
        RecyclerView.LayoutManager linearLayoutManager1 = new LinearLayoutManager(requireContext(),
                LinearLayoutManager.VERTICAL,
                false);
        overviewDataGroupRecyclerView.setLayoutManager(linearLayoutManager1);
        overviewDataGroupRecyclerView.setItemAnimator(new DefaultItemAnimator());
        overviewDataGroupRecyclerView.setAdapter(dataGroupRecyclerViewAdapter);

        textView11 = view.findViewById(R.id.textView11);

        update();

        return view;
    }

    public void update() {
        if (dashboardActivity.getSelectedCommunity() != null){
            textView11.setText(dashboardActivity.getSelectedCommunity().name);
        } else {
            textView11.setText("NuLL");
        }
    }
}