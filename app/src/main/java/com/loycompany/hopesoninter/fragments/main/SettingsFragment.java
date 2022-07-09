package com.loycompany.hopesoninter.fragments.main;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.loycompany.hopesoninter.MainActivity;
import com.loycompany.hopesoninter.R;
import com.loycompany.hopesoninter.SettingsActivity;

public class SettingsFragment extends Fragment {

    private ImageView backImageView;
    private String parentActivityName;

    public SettingsFragment() {
        // Required empty public constructor
    }

    public SettingsFragment(String parentActivityName) {
        this.parentActivityName = parentActivityName;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_settings, container, false);

        backImageView = view.findViewById(R.id.back_image_view);

        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // call onBackPress on parent activity
                if (parentActivityName.equals("MainActivity")){
                    MainActivity mainActivity = (MainActivity) requireActivity();
                    mainActivity.onBackPressed();
                } else if (parentActivityName.equals("SettingsActivity")){
                    SettingsActivity settingsActivity = (SettingsActivity)  requireActivity();
                    settingsActivity.onBackPressed();
                }
            }
        });

        return view;
    }
}