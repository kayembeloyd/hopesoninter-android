package com.loycompany.hopesoninter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.loycompany.hopesoninter.fragments.auth.LoginFragment;
import com.loycompany.hopesoninter.fragments.auth.RegisterFragment;

public class AuthActivity extends AppCompatActivity {

    private LoginFragment loginFragment;
    private RegisterFragment registerFragment;
    private boolean startMainActivity = true;

    View registerLineView, loginLineView;
    TextView registerTextView, loginTextView, messageTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        registerLineView = findViewById(R.id.register_line_linear_layout);
        loginLineView = findViewById(R.id.login_line_linear_layout);

        registerTextView = findViewById(R.id.register_text_view);
        loginTextView = findViewById(R.id.login_text_view);
        messageTextView = findViewById(R.id.message_text_view);

        registerLineView.setVisibility(View.GONE);
        loginLineView.setVisibility(View.GONE);

        messageTextView.setVisibility(View.GONE);

        startMainActivity = this.getIntent().getExtras().getBoolean("START_MAIN_ACTIVITY");
        switchFragment(this.getIntent().getExtras().getString("FRAGMENT_NAME"));

        loginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchFragment("login");
            }
        });

        registerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchFragment("register");
            }
        });
    }

    public void switchFragment(String fragmentName){
        if (fragmentName.equals("login")){
            registerLineView.setVisibility(View.GONE);
            loginLineView.setVisibility(View.VISIBLE);

            loginFragment = new LoginFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, loginFragment).commit();
        } else if (fragmentName.equals("register")){
            loginLineView.setVisibility(View.GONE);
            registerLineView.setVisibility(View.VISIBLE);

            registerFragment = new RegisterFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, registerFragment).commit();
        }
    }

    public void setMessage(String message){
        messageTextView.setVisibility(View.VISIBLE);
        messageTextView.setText(message);
    }

    @Override
    public void onBackPressed() {
        if (startMainActivity){
            Intent intent = new Intent(AuthActivity.this, MainActivity.class);
            startActivity(intent);
        }

        super.onBackPressed();
        AuthActivity.this.finish();
    }
}