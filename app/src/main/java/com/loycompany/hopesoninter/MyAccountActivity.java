package com.loycompany.hopesoninter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loycompany.hopesoninter.adapters.recyclerview.DialogCommunitiesRecyclerViewAdapter;
import com.loycompany.hopesoninter.authentication.Authenticator;
import com.loycompany.hopesoninter.classes.Community;

import java.util.ArrayList;
import java.util.List;

public class MyAccountActivity extends AppCompatActivity {

    TextView nameTextView;
    private ImageView backImageView;

    private LinearLayout communityLinearLayout;
    private LinearLayout mobileLinearLayout;

    RecyclerView communitiesRecyclerView;
    private DialogCommunitiesRecyclerViewAdapter dialogCommunitiesRecyclerViewAdapter;
    private List<Community> communities = new ArrayList<>();

    private Dialog communityListDialog, mobileInputDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);

        nameTextView = findViewById(R.id.name_text_view);

        if (Authenticator.getAuthUser(MyAccountActivity.this) != null){
            nameTextView.setText(Authenticator.getAuthUser(MyAccountActivity.this).name);
        }

        backImageView = findViewById(R.id.back_image_view);

        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // call onBackPress
                onBackPressed();
            }
        });

        communityLinearLayout = findViewById(R.id.community_linear_layout);

        communityLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // custom dialog
                communityListDialog = new Dialog(MyAccountActivity.this);
                communityListDialog.setContentView(R.layout.dialog_list);

                communitiesRecyclerView = (RecyclerView) communityListDialog.findViewById(R.id.recycler_view);

                communities = new ArrayList<>();
                for (int i = 0; i < 10; i++){
                    communities.add(new Community());
                }

                dialogCommunitiesRecyclerViewAdapter = new DialogCommunitiesRecyclerViewAdapter(MyAccountActivity.this, communities, communityListDialog);

                RecyclerView.LayoutManager postsRecyclerViewLayoutManager = new LinearLayoutManager(MyAccountActivity.this,
                        LinearLayoutManager.VERTICAL,
                        false);

                communitiesRecyclerView.setLayoutManager(postsRecyclerViewLayoutManager);
                communitiesRecyclerView.setItemAnimator(new DefaultItemAnimator());
                communitiesRecyclerView.setAdapter(dialogCommunitiesRecyclerViewAdapter);

                communityListDialog.show();
            }
        });

        mobileLinearLayout = findViewById(R.id.mobile_linear_layout);

        mobileLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mobileInputDialog = new Dialog(MyAccountActivity.this);
                mobileInputDialog.setContentView(R.layout.dialog_input);

                TextView titleTextView = mobileInputDialog.findViewById(R.id.title_text_view);
                Button positiveButton = mobileInputDialog.findViewById(R.id.positive_button),
                        negativeButton = mobileInputDialog.findViewById(R.id.negative_button);

                titleTextView.setText(R.string.enter_mobile);

                positiveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mobileInputDialog.dismiss();
                    }
                });

                negativeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mobileInputDialog.dismiss();
                    }
                });

                mobileInputDialog.show();
            }
        });

    }
}