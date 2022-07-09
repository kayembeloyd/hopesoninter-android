package com.loycompany.hopesoninter.adapters.pager;

import android.content.Context;
import android.media.metrics.Event;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;

import com.loycompany.hopesoninter.R;
import com.loycompany.hopesoninter.classes.PostMedia;
import com.loycompany.hopesoninter.fragments.dashboard.CommunitiesFragment;
import com.loycompany.hopesoninter.fragments.dashboard.EventsFragment;
import com.loycompany.hopesoninter.fragments.dashboard.ImportantContactsFragment;
import com.loycompany.hopesoninter.fragments.dashboard.MembersFragment;
import com.loycompany.hopesoninter.fragments.dashboard.OverviewFragment;
import com.loycompany.hopesoninter.fragments.dashboard.PostsFragment;

import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DashboardViewFragmentPagerAdapter extends FragmentPagerAdapter {

    List<String> fragments = new ArrayList<>();

    CommunitiesFragment communitiesFragment;
    OverviewFragment overviewFragment;
    PostsFragment postsFragment;
    MembersFragment membersFragment;
    ImportantContactsFragment importantContactsFragment;
    EventsFragment eventsFragment;

    public DashboardViewFragmentPagerAdapter(FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        initFragments();
    }

    public DashboardViewFragmentPagerAdapter(FragmentManager fm, List<String> fragments) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.fragments = fragments;

        initFragments();
    }

    private void initFragments() {
        communitiesFragment = new CommunitiesFragment();
        overviewFragment = new OverviewFragment();
        postsFragment = new PostsFragment();
        membersFragment = new MembersFragment();
        importantContactsFragment = new ImportantContactsFragment();
        eventsFragment = new EventsFragment();
    }

    @NonNull
    @Override
    public Fragment getItem(int i) {
        String fragmentName = fragments.get(i);

        switch (fragmentName) {
            case "Communities":
                return this.communitiesFragment;
            case "Events":
                return this.eventsFragment;
            case "Important Contacts":
                return this.importantContactsFragment;
            case "Members":
                return this.membersFragment;
            case "Overview":
                return this.overviewFragment;
            case "Posts":
                return this.postsFragment;
        }

        return this.overviewFragment;
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return fragments.get(position);
    }

    public void updateFragment(String fragmentName) {
        switch (fragmentName){
            case "OverviewFragment":
                overviewFragment.update();
        }
    }
}
