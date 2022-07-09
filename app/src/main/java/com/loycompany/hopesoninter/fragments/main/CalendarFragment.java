package com.loycompany.hopesoninter.fragments.main;

import android.content.Context;
import android.os.Bundle;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.loycompany.hopesoninter.R;
import com.loycompany.hopesoninter.adapters.recyclerview.CalendarWebnarEventsRecyclerViewAdapter;
import com.loycompany.hopesoninter.authentication.Authenticator;
import com.loycompany.hopesoninter.classes.WebnarEvent;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CalendarFragment extends Fragment {
    private Context context;
    private DrawerLayout drawerLayout;

    private RecyclerView calenderWebnarEventsRecyclerView;
    private CalendarWebnarEventsRecyclerViewAdapter calendarWebnarEventsRecyclerViewAdapter;
    private List<WebnarEvent> webnarEvents = new ArrayList<>();

    private ImageView menuImageView;

    private CircleImageView contextMenuCircleImageView;
    private View contextMenuRefView;
    private PopupMenu contextMenuPopup;

    public CalendarFragment() {
        this.webnarEvents = new ArrayList<>();

        fakeAdd();
    }

    public CalendarFragment(Context context, DrawerLayout drawerLayout) {
        this.context = context;
        this.drawerLayout = drawerLayout;

        this.webnarEvents = new ArrayList<>();

        fakeAdd();
    }

    private void fakeAdd(){
        for (int i = 0; i < 5; i++){
            this.webnarEvents.add(new WebnarEvent());
        }
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_calendar, container, false);

        menuImageView = view.findViewById(R.id.menu_image_view);
        menuImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawerLayout.isOpen()) {
                    drawerLayout.close();
                } else {
                    drawerLayout.open();
                }
            }
        });

        calenderWebnarEventsRecyclerView = view.findViewById(R.id.calendar_webnar_events_recycler_view);
        calendarWebnarEventsRecyclerViewAdapter = new CalendarWebnarEventsRecyclerViewAdapter(requireContext(), webnarEvents);

        RecyclerView.LayoutManager calenderWebnarEventsRecyclerViewLayoutManager = new LinearLayoutManager(requireContext(),
                LinearLayoutManager.VERTICAL,
                false);

        calenderWebnarEventsRecyclerView.setLayoutManager(calenderWebnarEventsRecyclerViewLayoutManager);
        calenderWebnarEventsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        calenderWebnarEventsRecyclerView.setAdapter(calendarWebnarEventsRecyclerViewAdapter);

        contextMenuCircleImageView = view.findViewById(R.id.context_menu_circle_image_view);
        contextMenuRefView = view.findViewById(R.id.context_menu_ref_view);

        if (Authenticator.getAuthUser(requireContext()) == null){
            contextMenuCircleImageView.setVisibility(View.GONE);
        }

        contextMenuCircleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contextMenuPopup = new PopupMenu(requireContext(), contextMenuRefView);
                MenuInflater inflater = contextMenuPopup.getMenuInflater();
                inflater.inflate(R.menu.main_context_menu_calendar_user, contextMenuPopup.getMenu());
                contextMenuPopup.show();

                contextMenuPopup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.refresh:
                                Toast.makeText(requireContext(), "refreshing", Toast.LENGTH_SHORT).show();
                                return true;
                            case R.id.my_account:
                                Toast.makeText(requireContext(), "my account", Toast.LENGTH_SHORT).show();
                                return true;
                            default:
                                return false;
                        }
                    }
                });

            }
        });

        return view;
    }
}