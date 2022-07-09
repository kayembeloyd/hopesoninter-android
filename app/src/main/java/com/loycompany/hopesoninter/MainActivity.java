package com.loycompany.hopesoninter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.loycompany.hopesoninter.authentication.Authenticator;
import com.loycompany.hopesoninter.classes.Post;
import com.loycompany.hopesoninter.classes.User;
import com.loycompany.hopesoninter.fragments.main.CalendarFragment;
import com.loycompany.hopesoninter.fragments.main.ChatFragment;
import com.loycompany.hopesoninter.fragments.main.HomeFragment;
import com.loycompany.hopesoninter.fragments.main.SettingsFragment;
import com.loycompany.hopesoninter.fragments.post.PostViewFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private User authenticatedUserCheck;

    private DrawerLayout drawerLayout;
    private NavigationView drawerNavigationView;
    private View drawerNavigationViewHeaderView;

    private BottomNavigationView bottomNavigationView;

    private TextView headerAccessTextView, headerNameTextView;
    private Button headerLoginButton;

    private String activeFragmentName;

    private HomeFragment homeFragment;
    private CalendarFragment calendarFragment;
    private ChatFragment chatFragment;
    private SettingsFragment settingsFragment;

    private PostViewFragment postViewFragment;
    private List<Post> postViewStack = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer_layout);
        drawerNavigationView = findViewById(R.id.drawer_navigation_view);

        drawerNavigationViewHeaderView = drawerNavigationView.getHeaderView(0);

        homeFragment = new HomeFragment(MainActivity.this, drawerLayout);
        calendarFragment = new CalendarFragment(MainActivity.this, drawerLayout);
        chatFragment = new ChatFragment(MainActivity.this, drawerLayout);
        settingsFragment = new SettingsFragment("MainActivity");

        bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        bottomNavigationView.setItemIconTintList(null);

        headerNameTextView = drawerNavigationViewHeaderView.findViewById(R.id.name_text_view);
        headerAccessTextView = drawerNavigationViewHeaderView.findViewById(R.id.access_text_view);
        headerLoginButton = drawerNavigationViewHeaderView.findViewById(R.id.login_button);

        headerNameTextView.setVisibility(View.GONE);
        headerAccessTextView.setVisibility(View.GONE);
        headerLoginButton.setVisibility(View.GONE);

        authenticatedUserCheck = Authenticator.getAuthUser(MainActivity.this);

        if (authenticatedUserCheck != null){
            headerNameTextView.setText(authenticatedUserCheck.name);
            headerNameTextView.setVisibility(View.VISIBLE);

            if (authenticatedUserCheck.access.equals("user")){
                drawerNavigationView.inflateMenu(R.menu.main_navigation_drawer_menu_user);
                bottomNavigationView.inflateMenu(R.menu.main_bottom_navigation_menu_user);
            } else if (authenticatedUserCheck.access.equals("community_leader")){
                drawerNavigationView.inflateMenu(R.menu.main_navigation_drawer_menu_user_community_leader);
                bottomNavigationView.inflateMenu(R.menu.main_bottom_navigation_menu_user);
                headerAccessTextView.setVisibility(View.VISIBLE);
                headerAccessTextView.setText(authenticatedUserCheck.access);
            } else if (authenticatedUserCheck.access.equals("admin")){
                drawerNavigationView.inflateMenu(R.menu.main_navigation_drawer_menu_user_admin);
                bottomNavigationView.inflateMenu(R.menu.main_bottom_navigation_menu_user);
                headerAccessTextView.setVisibility(View.VISIBLE);
                headerAccessTextView.setText(authenticatedUserCheck.access);
            }
        } else {
            drawerNavigationView.inflateMenu(R.menu.main_navigation_drawer_menu_guest);
            bottomNavigationView.inflateMenu(R.menu.main_bottom_navigation_menu_guest);
            headerLoginButton.setVisibility(View.VISIBLE);
        }

        headerLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle args = new Bundle();
                args.putString("FRAGMENT_NAME", "login");
                args.putBoolean("START_MAIN_ACTIVITY", false);

                Intent intent = new Intent(MainActivity.this, AuthActivity.class);
                intent.putExtras(args);

                startActivity(intent);
            }
        });

        drawerNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                drawerLayout.close();

                switch (item.getItemId()){
                    case R.id.home:
                        return true;
                    case R.id.settings:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, settingsFragment).commit();
                        activeFragmentName = "settingsFragment";
                        return true;
                    case R.id.my_account:
                        startActivity(new Intent(MainActivity.this, MyAccountActivity.class));
                        return true;
                    case R.id.admins_dashboard:
                        startActivity(new Intent(MainActivity.this, DashboardActivity.class));
                        return true;
                    case R.id.create_meeting:
                    case R.id.attend_meeting:
                        startActivity(new Intent(MainActivity.this, MeetingActivity.class));
                        return true;
                }

                return false;
            }
        });

        if (this.getIntent().getExtras() != null){
            if (this.getIntent().getExtras().containsKey("ACTIVE_FRAGMENT_NAME")){
                activeFragmentName = this.getIntent().getExtras().getString("ACTIVE_FRAGMENT_NAME");

                if (activeFragmentName.equals("calendarFragment")){
                    bottomNavigationView.setSelectedItemId(R.id.calendar);
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, calendarFragment).commit();
                    activeFragmentName = "calendarFragment";
                } else if (activeFragmentName.equals("chatFragment")){
                    bottomNavigationView.setSelectedItemId(R.id.chat);
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, chatFragment).commit();
                    activeFragmentName = "chatFragment";
                } else {
                    loadHomeFragment();
                }
            } else {
                loadHomeFragment();
            }
        } else {
            loadHomeFragment();
        }

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, homeFragment).commit();
                        activeFragmentName = "homeFragment";
                        return true;
                    case R.id.chat:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, chatFragment).commit();
                        activeFragmentName = "chatFragment";
                        return true;
                    case R.id.calendar:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, calendarFragment).commit();
                        activeFragmentName = "calendarFragment";
                        return true;
                    case R.id.settings:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, settingsFragment).commit();
                        activeFragmentName = "settingsFragment";
                        return true;
                }

                return false;
            }
        });


        BottomNavigationMenuView bottomNavigationMenuView =
                (BottomNavigationMenuView) bottomNavigationView.getChildAt(0);

        for (int i = 0; i < bottomNavigationMenuView.getChildCount(); i++){
            View view = bottomNavigationMenuView.getChildAt(i);
            BottomNavigationItemView itemView = (BottomNavigationItemView) view;

            if (itemView.getId() == R.id.chat){
                View cart_badge = LayoutInflater.from(this)
                        .inflate(R.layout.badge_chat_counter,
                                bottomNavigationMenuView, false);

                //// AND THAN SET THE COUNTER BADGE, AS FOLLOW
                ((TextView) cart_badge.findViewById(R.id.notificationsBadge)).setText("5");

                itemView.addView(cart_badge);
            }
        }
    }

    private void loadHomeFragment() {
        bottomNavigationView.setSelectedItemId(R.id.home);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, homeFragment).commit();
        activeFragmentName = "homeFragment";
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isOpen())
            drawerLayout.close();
        else{
            if (postViewStack.size() > 1){
                postViewStack.remove(postViewStack.size() - 1);
                postViewFragment = new PostViewFragment(postViewStack.get(postViewStack.size() - 1), "MainActivity");
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, postViewFragment).commit();
                activeFragmentName = "postViewFragment";
            } else{
                if (!activeFragmentName.equals("homeFragment")){
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, homeFragment).commit();
                    activeFragmentName = "homeFragment";
                    bottomNavigationView.setSelectedItemId(R.id.home);
                } else {
                    super.onBackPressed();
                }
            }
        }
    }

    public void switchToPostViewFragment(Post post){
        postViewFragment = new PostViewFragment(post, "MainActivity");
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, postViewFragment).commit();
        activeFragmentName = "postViewFragment";
        postViewStack.add(post);
    }
}