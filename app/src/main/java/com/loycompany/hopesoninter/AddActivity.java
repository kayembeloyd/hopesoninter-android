package com.loycompany.hopesoninter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.loycompany.hopesoninter.fragments.adds.AddCommunityFragment;
import com.loycompany.hopesoninter.fragments.adds.AddDataFragment;
import com.loycompany.hopesoninter.fragments.adds.AddEventFragment;
import com.loycompany.hopesoninter.fragments.adds.AddImportantContactFragment;
import com.loycompany.hopesoninter.fragments.adds.AddMemberFragment;
import com.loycompany.hopesoninter.fragments.adds.AddPostFragment;

public class AddActivity extends AppCompatActivity {

    String fragmentName;
    int selectedCommunityID;

    AddCommunityFragment addCommunityFragment = new AddCommunityFragment();
    AddPostFragment addPostFragment = new AddPostFragment();
    AddDataFragment addDataFragment = new AddDataFragment();
    AddEventFragment addEventFragment = new AddEventFragment();
    AddImportantContactFragment addImportantContactFragment = new AddImportantContactFragment();
    AddMemberFragment addMemberFragment = new AddMemberFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        fragmentName = getIntent().getExtras().getString("FRAGMENT_NAME");
        selectedCommunityID = getIntent().getExtras().getInt("SELECTED_COMMUNITY_ID");

        if (fragmentName.equals("addCommunityFragment")){
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, addCommunityFragment).commit();
        } else if (fragmentName.equals("addMemberFragment")){
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, addMemberFragment).commit();
        } else if (fragmentName.equals("addImportantContactFragment")){
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, addImportantContactFragment).commit();
        } else if (fragmentName.equals("addEventFragment")){
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, addEventFragment).commit();
        } else if (fragmentName.equals("addDataFragment")){
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, addDataFragment).commit();
        } else if (fragmentName.equals("addPostFragment")){
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, addPostFragment).commit();
        }
    }

    public int getSelectedCommunityID() {
        return selectedCommunityID;
    }
}