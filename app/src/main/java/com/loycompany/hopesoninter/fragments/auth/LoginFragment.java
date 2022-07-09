package com.loycompany.hopesoninter.fragments.auth;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.loycompany.hopesoninter.AuthActivity;
import com.loycompany.hopesoninter.MainActivity;
import com.loycompany.hopesoninter.R;
import com.loycompany.hopesoninter.authentication.Authenticator;
import com.loycompany.hopesoninter.classes.User;
import com.loycompany.hopesoninter.network.ServerRequest;
import com.loycompany.hopesoninter.network.URLs;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginFragment extends Fragment {

    Handler handler;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        handler = new Handler();

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_auth_login, container, false);

        EditText emailEditText = view.findViewById(R.id.email_edit_text),
                passwordEditText = view.findViewById(R.id.password_edit_text);

        Button loginButton = view.findViewById(R.id.login_button);

        ProgressBar progressBar = view.findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email, password;

                email = emailEditText.getText().toString();
                password = passwordEditText.getText().toString();

                if (email.equals("")) {
                    emailEditText.setError(getString(R.string.please_enter_email));
                    return;
                }

                if (password.equals("")){
                    passwordEditText.setError(getString(R.string.please_enter_password));
                    return;
                }

                ServerRequest serverRequest = new ServerRequest(requireContext()) {
                    @Override
                    public void onServerResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if  (jsonObject.has("status")){
                                if (jsonObject.getString("status").equals("success")){
                                    User user = new User(jsonObject.getJSONObject("user"));
                                    user.setToken(jsonObject.getString("token"));
                                    Authenticator.saveAuthUser(user, requireContext());

                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            AuthActivity authActivity = (AuthActivity) LoginFragment.this.getActivity();
                                            if (authActivity != null){
                                                authActivity.setMessage("login successful, wait!!!");

                                                handler.postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        authActivity.startActivity(new Intent(authActivity, MainActivity.class));
                                                        authActivity.finish();
                                                    }
                                                }, 1000);
                                            } else {
                                                showErrorWithHandler("AuthActivity is null", "", requireContext());
                                            }
                                        }
                                    });
                                } else {
                                    showErrorWithHandler("Failed to login", "wrong password or email", requireContext());
                                }
                            } else {
                                showErrorWithHandler("JSON Does not have success", "", requireContext());
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            showErrorWithHandler("JSON Exception ", e.toString(), requireContext());
                        }
                    }

                    @Override
                    public void onServerError(String error) {
                        showErrorWithHandler("Server Error ", error, requireContext());
                    }
                };

                serverRequest.addHeaderParam("Accept", "application/json");
                serverRequest.addParam("email", email);
                serverRequest.addParam("password", password);
                serverRequest.sendRequest(URLs.getApiAddress2() + "/user/login", Request.Method.POST);

                progressBar.setVisibility(View.VISIBLE);
            }
        });

        return view;
    }

    private void showErrorWithHandler(String err, String ref, Context context){
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, err + ref, Toast.LENGTH_SHORT).show();
            }
        });
    }
}