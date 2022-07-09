package com.loycompany.hopesoninter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.loycompany.hopesoninter.adapters.pager.DashboardViewFragmentPagerAdapter;
import com.loycompany.hopesoninter.adapters.recyclerview.DialogCommunitiesRecyclerViewAdapter;
import com.loycompany.hopesoninter.authentication.Authenticator;
import com.loycompany.hopesoninter.classes.Community;
import com.loycompany.hopesoninter.classes.User;
import com.loycompany.hopesoninter.network.ServerRequest;
import com.loycompany.hopesoninter.network.URLs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class DashboardActivity extends AppCompatActivity {
    private User authenticatedUserCheck;

    private DrawerLayout drawerLayout;
    private NavigationView drawerNavigationView;
    private View drawerNavigationViewHeaderView;

    private TextView headerAccessTextView, headerNameTextView;
    private Button headerLoginButton;

    private ImageView menuImageView;

    private CircleImageView contextMenuCircleImageView;
    private View contextMenuRefView;
    private PopupMenu contextMenuPopup;

    private DashboardViewFragmentPagerAdapter dashboardViewFragmentPagerAdapter;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    private List<String> fragments = new ArrayList<>();

    private String activeFragmentName;
    private FloatingActionButton floatingActionButton;

    private LinearLayout selectCommunityLinearLayout;
    private TextView selectedCommunityNameTextView;
    private Dialog communityListDialog;

    private RecyclerView communitiesRecyclerView;
    private DialogCommunitiesRecyclerViewAdapter dialogCommunitiesRecyclerViewAdapter;
    private List<Community> communities = new ArrayList<>();

    private Community selectedCommunity;

    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        handler = new Handler();

        drawerLayout = findViewById(R.id.drawer_layout);
        drawerNavigationView = findViewById(R.id.drawer_navigation_view);

        drawerNavigationViewHeaderView = drawerNavigationView.getHeaderView(0);

        headerNameTextView = drawerNavigationViewHeaderView.findViewById(R.id.name_text_view);
        headerAccessTextView = drawerNavigationViewHeaderView.findViewById(R.id.access_text_view);
        headerLoginButton = drawerNavigationViewHeaderView.findViewById(R.id.login_button);

        headerNameTextView.setVisibility(View.GONE);
        headerAccessTextView.setVisibility(View.GONE);
        headerLoginButton.setVisibility(View.GONE);

        authenticatedUserCheck = Authenticator.getAuthUser(DashboardActivity.this);

        if (authenticatedUserCheck != null){
            headerNameTextView.setText(authenticatedUserCheck.name);
            headerNameTextView.setVisibility(View.VISIBLE);

            if (authenticatedUserCheck.access.equals("user")){
                drawerNavigationView.inflateMenu(R.menu.main_navigation_drawer_menu_user);
            } else if (authenticatedUserCheck.access.equals("community_leader")){
                fragments = new ArrayList<>();

                fragments.add("Overview");
                fragments.add("Posts");
                fragments.add("Members");
                fragments.add("Important Contacts");
                fragments.add("Events");

                drawerNavigationView.inflateMenu(R.menu.main_navigation_drawer_menu_user_community_leader);
                headerAccessTextView.setVisibility(View.VISIBLE);
                headerAccessTextView.setText(authenticatedUserCheck.access);
            } else if (authenticatedUserCheck.access.equals("admin")){
                fragments = new ArrayList<>();

                fragments.add("Communities");
                fragments.add("Overview");
                fragments.add("Posts");
                fragments.add("Members");
                fragments.add("Important Contacts");
                fragments.add("Events");

                drawerNavigationView.inflateMenu(R.menu.main_navigation_drawer_menu_user_admin);
                headerAccessTextView.setVisibility(View.VISIBLE);
                headerAccessTextView.setText(authenticatedUserCheck.access);
            }
        } else {
            onBackPressed();
        }

        drawerNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                drawerLayout.close();

                switch (item.getItemId()){
                    case R.id.home:
                        startActivity(new Intent(DashboardActivity.this, MainActivity.class));
                        DashboardActivity.this.finishAffinity();
                        return true;
                    case R.id.settings:
                        startActivity(new Intent(DashboardActivity.this, SettingsActivity.class));
                        return true;
                    case R.id.my_account:
                        startActivity(new Intent(DashboardActivity.this, MyAccountActivity.class));
                        return true;
                    case R.id.admins_dashboard:
                        return true;
                    case R.id.calendar:
                        Bundle args = new Bundle();
                        args.putString("ACTIVE_FRAGMENT_NAME", "calendarFragment");

                        Intent intent = new Intent(DashboardActivity.this, MainActivity.class);
                        intent.putExtras(args);

                        startActivity(intent);
                        DashboardActivity.this.finishAffinity();
                        return true;
                    case R.id.chat:
                        Bundle args2 = new Bundle();
                        args2.putString("ACTIVE_FRAGMENT_NAME", "chatFragment");

                        Intent intent2 = new Intent(DashboardActivity.this, MainActivity.class);
                        intent2.putExtras(args2);

                        startActivity(intent2);
                        DashboardActivity.this.finishAffinity();
                        return true;
                }

                return false;
            }
        });

        menuImageView = findViewById(R.id.menu_image_view);
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

        contextMenuCircleImageView = findViewById(R.id.context_menu_circle_image_view);
        contextMenuRefView = findViewById(R.id.context_menu_ref_view);

        if (Authenticator.getAuthUser(DashboardActivity.this) == null){
            contextMenuCircleImageView.setVisibility(View.GONE);
        }

        contextMenuCircleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contextMenuPopup = new PopupMenu(DashboardActivity.this, contextMenuRefView);
                MenuInflater inflater = contextMenuPopup.getMenuInflater();
                inflater.inflate(R.menu.main_context_menu_home_user, contextMenuPopup.getMenu());
                contextMenuPopup.show();

                contextMenuPopup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.refresh:
                                Toast.makeText(DashboardActivity.this, "refreshing", Toast.LENGTH_SHORT).show();
                                return true;
                            case R.id.my_account:
                                Toast.makeText(DashboardActivity.this, "my account", Toast.LENGTH_SHORT).show();
                                return true;
                            default:
                                return false;
                        }
                    }
                });
            }
        });

        dashboardViewFragmentPagerAdapter = new DashboardViewFragmentPagerAdapter(getSupportFragmentManager(), fragments);
        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(dashboardViewFragmentPagerAdapter);

        tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);

        activeFragmentName = "Communities";

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                activeFragmentName = tab.getText().toString();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        floatingActionButton = findViewById(R.id.fab);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle args = new Bundle();

                if (activeFragmentName.toLowerCase().equals("Communities".toLowerCase())){
                    Toast.makeText(getApplicationContext(), "Adding Communities", Toast.LENGTH_SHORT).show();
                    args.putString("FRAGMENT_NAME", "addCommunityFragment");

                } else if (activeFragmentName.toLowerCase().equals("Posts".toLowerCase())){
                    Toast.makeText(getApplicationContext(), "Adding Posts", Toast.LENGTH_SHORT).show();
                    args.putString("FRAGMENT_NAME", "addPostFragment");
                    args.putInt("SELECTED_COMMUNITY_ID", getSelectedCommunity().ID);

                } else if (activeFragmentName.toLowerCase().equals("Overview".toLowerCase())){
                    Toast.makeText(getApplicationContext(), "Adding data", Toast.LENGTH_SHORT).show();
                    args.putString("FRAGMENT_NAME", "addDataFragment");

                } else if (activeFragmentName.toLowerCase().equals("Members".toLowerCase())){
                    Toast.makeText(getApplicationContext(), "Adding Members", Toast.LENGTH_SHORT).show();
                    args.putString("FRAGMENT_NAME", "addMemberFragment");

                } else if (activeFragmentName.toLowerCase().equals("Important Contacts".toLowerCase())){
                    Toast.makeText(getApplicationContext(), "Adding Important Contacts", Toast.LENGTH_SHORT).show();
                    args.putString("FRAGMENT_NAME", "addImportantContactFragment");

                } else if (activeFragmentName.toLowerCase().equals("Events".toLowerCase())){
                    Toast.makeText(getApplicationContext(), "Adding Events", Toast.LENGTH_SHORT).show();
                    args.putString("FRAGMENT_NAME", "addEventFragment");

                }

                Intent intent = new Intent(DashboardActivity.this, AddActivity.class);
                intent.putExtras(args);

                startActivity(intent);
            }
        });

        selectedCommunityNameTextView = findViewById(R.id.selected_community_name_text_view);
        selectCommunityLinearLayout = findViewById(R.id.select_community_linear_layout);
        selectCommunityLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                communityListDialog = new Dialog(DashboardActivity.this);
                communityListDialog.setContentView(R.layout.dialog_list);

                TextView titleTextView = communityListDialog.findViewById(R.id.title_text_view);
                titleTextView.setText(R.string.select_community);
                communitiesRecyclerView = (RecyclerView) communityListDialog.findViewById(R.id.recycler_view);

                ProgressBar communityListProgressBarDialog = communityListDialog.findViewById(R.id.progress_bar);
                communityListProgressBarDialog.setVisibility(View.GONE);
                communities = new ArrayList<>();

                dialogCommunitiesRecyclerViewAdapter = new DialogCommunitiesRecyclerViewAdapter(DashboardActivity.this, communities, communityListDialog);
                dialogCommunitiesRecyclerViewAdapter.setParentActivityName("DashboardActivity");

                RecyclerView.LayoutManager postsRecyclerViewLayoutManager = new LinearLayoutManager(DashboardActivity.this,
                        LinearLayoutManager.VERTICAL,
                        false);

                communitiesRecyclerView.setLayoutManager(postsRecyclerViewLayoutManager);
                communitiesRecyclerView.setItemAnimator(new DefaultItemAnimator());
                communitiesRecyclerView.setAdapter(dialogCommunitiesRecyclerViewAdapter);

                communityListDialog.show();

                ServerRequest serverRequest = new ServerRequest(DashboardActivity.this) {
                    @Override
                    public void onServerResponse(String response) {
                        communityListProgressBarDialog.setVisibility(View.GONE);

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.has("data")){
                                JSONArray jsonArray = jsonObject.getJSONArray("data");

                                for (int i = 0; i < jsonArray.length(); i++){
                                    communities.add(new Community(jsonArray.getJSONObject(i)));
                                }

                                // update adapter
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        dialogCommunitiesRecyclerViewAdapter.notifyDataSetChanged();
                                    }
                                });
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onServerError(String error) {
                        Toast.makeText(getApplicationContext(), "Error: " + error, Toast.LENGTH_SHORT).show();
                        communityListProgressBarDialog.setVisibility(View.GONE);
                    }
                };

                serverRequest.addHeaderParam("Accept", "application/json");
                serverRequest.sendRequest(URLs.getApiAddress() + "/communities", Request.Method.GET);
                communityListProgressBarDialog.setVisibility(View.VISIBLE);
            }
        });
    }

    public void setSelectedCommunity(Community community){
        this.selectedCommunity = community;

        if (selectedCommunityNameTextView != null){
            selectedCommunityNameTextView.setText(selectedCommunity.name);
        }

        dashboardViewFragmentPagerAdapter.updateFragment("OverviewFragment");
    }

    public Community getSelectedCommunity() {
        return this.selectedCommunity;
    }
}