package com.ehmaugbogo.hng_ehma;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ehmaugbogo.hng_ehma.Database.UserViewModel;
import com.ehmaugbogo.hng_ehma.Model.User;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "slike.Views.Login";

    private TextInputLayout emailEditText, passwordEditText;
    private ProgressBar progressBar;
    private UserViewModel viewModel;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);


        initViews();


        UserViewModel viewModel = ViewModelProviders.of(this).get(UserViewModel.class);

        if (getSharePref().getLoggedUserId() != -1) {
            for (User looggedUser : viewModel.getallUsers()) {
                if(looggedUser.getId().equals(getSharePref().getLoggedUserId())) {
                    openMainActivity(LoginActivity.this,looggedUser);
                    finish();
                }
            }
        }


    }

    private void initViews() {
        emailEditText = findViewById(R.id.login_activity_email_TextInputLayout);
        passwordEditText = findViewById(R.id.login_activity_password_TextInputLayout);
        TextView registerHere = findViewById(R.id.login_activity_register_text);
        Button loginbtn = findViewById(R.id.login_activity_button);


        registerHere.setOnClickListener(this);
        loginbtn.setOnClickListener(this);

        progressBar = findViewById(R.id.login_activity_progressbar);
        handler = new Handler();

        viewModel = ViewModelProviders.of(this).get(UserViewModel.class);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_activity_button:
                login();
                break;
            case R.id.login_activity_register_text:
                gotoRegisterActivity(LoginActivity.this);
                break;
        }
    }


    private void login() {
        showProgressbar();
        String email = emailEditText.getEditText().getText().toString().trim();
        String password = passwordEditText.getEditText().getText().toString().trim();


        if (!validateEmailPassword(email, password)) {
            hideProgressbar();
            return;
        }

        for(User user:viewModel.getallUsers()){
            if(user.getEmail().equals(email)&&user.getPassword().equals(password)){
                getSharePref().setLoggedUserId(user.getId());
                Log.d(TAG, "register: SharedPref LoggedIn UserId "+getSharePref().getLoggedUserId());
                openMainActivityOnDelay(user);
                return;
            }
        }

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                hideProgressbar();
                showToast("Wrong email or password");
            }
        },2000);

    }

    private void openMainActivityOnDelay(final User user) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                openMainActivity(LoginActivity.this,user);
                finish();
            }
        },3000);
    }


    private boolean validateEmailPassword(String email, String password) {
        if (TextUtils.isEmpty(email)) {
            emailEditText.setError("Required");
            return false;
        }

        if (TextUtils.isEmpty(password)) {
            passwordEditText.setError("Required");
            return false;
        } else if (password.length() > 15) {
            passwordEditText.setError("Password too long");
            return false;
        }
        return true;
    }


    private void showProgressbar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressbar() {
        progressBar.setVisibility(View.GONE);
    }

}
