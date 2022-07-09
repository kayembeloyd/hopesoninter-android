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
import com.loycompany.hopesoninter.adapters.recyclerview.MembersRecyclerViewAdapter;
import com.loycompany.hopesoninter.classes.CommunityMember;

import java.util.ArrayList;
import java.util.List;

public class MembersFragment extends Fragment {

    private RecyclerView membersRecyclerView;
    private MembersRecyclerViewAdapter membersRecyclerViewAdapter;
    private List<CommunityMember> members = new ArrayList<>();

    public MembersFragment() {
        fakeAdd();
    }

    private void fakeAdd(){
        for (int i = 0; i < 5; i++){
            this.members.add(new CommunityMember());
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard_members, container, false);

        membersRecyclerView = view.findViewById(R.id.members_recycler_view);
        membersRecyclerViewAdapter = new MembersRecyclerViewAdapter(requireContext(), members);

        RecyclerView.LayoutManager linearLayoutManager = new LinearLayoutManager(requireContext(),
                LinearLayoutManager.VERTICAL,
                false);

        membersRecyclerView.setLayoutManager(linearLayoutManager);
        membersRecyclerView.setItemAnimator(new DefaultItemAnimator());
        membersRecyclerView.setAdapter(membersRecyclerViewAdapter);

        return view;
    }
}