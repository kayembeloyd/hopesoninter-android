package com.loycompany.hopesoninter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;

import com.loycompany.hopesoninter.authentication.Authenticator;

public class SplashScreenActivity extends AppCompatActivity {

    Handler handler;

    TextView welcomeTextView, skipTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        handler = new Handler();
        welcomeTextView = findViewById(R.id.welcome_text_view);
        skipTextView = findViewById(R.id.skip_text_view);

        welcomeTextView.setVisibility(View.GONE);
        skipTextView.setVisibility(View.GONE);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isFirstRun()){
                    welcomeTextView.setVisibility(View.VISIBLE);
                    skipTextView.setVisibility(View.VISIBLE);

                    skipTextView.setText(R.string.next);

                    skipTextView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Bundle args = new Bundle();
                            args.putString("FRAGMENT_NAME", "register");
                            args.putBoolean("START_MAIN_ACTIVITY", true);

                            Intent intent = new Intent(SplashScreenActivity.this, AuthActivity.class);
                            intent.putExtras(args);

                            startActivity(intent);
                            SplashScreenActivity.this.finish();
                        }
                    });
                } else {
                    welcomeTextView.setVisibility(View.VISIBLE);
                    String welcomeText = getString(R.string.welcome) + ", Guest";

                    if (Authenticator.getAuthUser(SplashScreenActivity.this) != null)
                        welcomeText = getString(R.string.welcome) + ", " + Authenticator.getAuthUser(SplashScreenActivity.this).name;

                    welcomeTextView.setText(welcomeText);

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
                            SplashScreenActivity.this.finish();
                        }
                    }, 2000);
                }
            }
        }, 2000);
    }

    private boolean isFirstRun() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(SplashScreenActivity.this);
        boolean isFirstUse = sharedPreferences.getBoolean("is_first_use", true);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("is_first_use", false);
        editor.apply(); // Can use editor.commit()

        return isFirstUse;
    }
}