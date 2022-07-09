package com.loycompany.hopesoninter.fragments.auth;

import android.content.Context;
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
import com.loycompany.hopesoninter.R;
import com.loycompany.hopesoninter.classes.User;
import com.loycompany.hopesoninter.network.ServerRequest;
import com.loycompany.hopesoninter.network.URLs;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterFragment extends Fragment {

    Handler handler;

    public RegisterFragment() {
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
        View view = inflater.inflate(R.layout.fragment_auth_register, container, false);

        EditText nameEditText = view.findViewById(R.id.name_edit_text),
                emailEditText = view.findViewById(R.id.email_edit_text),
                passwordEditText = view.findViewById(R.id.password_edit_text);

        Button registerButton = view.findViewById(R.id.register_button);

        ProgressBar progressBar = view.findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name, email, password;

                name = nameEditText.getText().toString();
                email = emailEditText.getText().toString();
                password = passwordEditText.getText().toString();

                if (name.equals("")){
                    nameEditText.setError(getString(R.string.please_enter_name));
                    return;
                }

                if (email.equals("")){
                    emailEditText.setError(getString(R.string.please_enter_email));
                    return;
                }

                if (password.equals("")){
                    passwordEditText.setError(getString(R.string.please_create_password));
                    return;
                }

                if (password.length() < 6){
                    passwordEditText.setError(getString(R.string.password_should_be_greater_than_6_characters));
                    return;
                }

                ServerRequest serverRequest = new ServerRequest(requireContext()) {
                    @Override
                    public void onServerResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if  (jsonObject.has("status")){
                                if (jsonObject.getString("status").equals("success")){

                                    // NOT AN ERROR
                                    showErrorWithHandler("Registered successfully please login", "", requireContext());

                                    // switch to loginFragment
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            AuthActivity authActivity = (AuthActivity) RegisterFragment.this.getActivity();
                                            if (authActivity != null){
                                                authActivity.switchFragment("login");
                                                authActivity.setMessage("registrations successful, please login now");
                                            } else {
                                                showErrorWithHandler("AuthActivity is null", "", requireContext());
                                            }
                                        }
                                    });
                                } else {
                                    showErrorWithHandler("Failed to create account", "wrong password or email", requireContext());
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
                serverRequest.addParam("name", name);
                serverRequest.addParam("email", email);
                serverRequest.addParam("password", password);
                serverRequest.sendRequest(URLs.getApiAddress2() + "/user/register", Request.Method.POST);

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