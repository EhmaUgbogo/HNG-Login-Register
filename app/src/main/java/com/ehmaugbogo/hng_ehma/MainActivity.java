package com.ehmaugbogo.hng_ehma;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ehmaugbogo.hng_ehma.Database.UserViewModel;
import com.ehmaugbogo.hng_ehma.Model.User;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class MainActivity extends BaseActivity {
    public static final String USER_KEY="com.ehmaugbogo.hng_ehma_USER_KEY";
    private UserViewModel viewModel;
    private User currentUser;
    private TextView userName;
    private TextView email;
    private TextInputEditText nameEditText;
    private TextInputEditText lastNameEditText;
    private TextInputEditText emailEditText;
    private TextInputEditText passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();


        viewModel = ViewModelProviders.of(this).get(UserViewModel.class);

        viewModel.getCurrentUser().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                currentUser=user;
                setDisplayInfo(currentUser);
            }
        });
    }


    private void initViews() {
        userName = findViewById(R.id.main_activity_username);
        email = findViewById(R.id.main_activity_email_text);
        Button logInBtn=findViewById(R.id.main_activity_button);


        logInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBottomSheetProfileSetup();
            }
        });
        getLoggedInUserFromIntentExtra();
    }


    private void getLoggedInUserFromIntentExtra() {
        if (getSharePref().getLoggedUserId() != -1 &&getIntent()!=null) {
            currentUser = getIntent().getParcelableExtra(USER_KEY);
            setDisplayInfo(currentUser);
        }
    }

    private void setDisplayInfo(User user){
        String displayName = user.getFirstName() + " " + user.getLastName();
        userName.setText(displayName);
        email.setText(user.getEmail());
    }

    private void openBottomSheetProfileSetup() {
        final BottomSheetDialog bottomSheetDialog=new BottomSheetDialog(this);
        bottomSheetDialog.setCancelable(false);
        bottomSheetDialog.setCanceledOnTouchOutside(true);

        View view=View.inflate(this,R.layout.profile_setup,null);
        nameEditText = view.findViewById(R.id.profile_setup_firstname);
        lastNameEditText = view.findViewById(R.id.profile_setup_lastname);
        emailEditText = view.findViewById(R.id.profile_setup_email);
        passwordEditText = view.findViewById(R.id.profile_setup_password);
        Button summitBtn=view.findViewById(R.id.profile_setup_summit_btn);

        nameEditText.setText(currentUser.getFirstName());
        lastNameEditText.setText(currentUser.getLastName());
        emailEditText.setText(currentUser.getEmail());
        passwordEditText.setText(currentUser.getPassword());

        emailEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("Email is Unique to each User. Cannot be changed");
            }
        });

        summitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fName= Objects.requireNonNull(nameEditText.getText()).toString().trim();
                String lName= Objects.requireNonNull(lastNameEditText.getText()).toString().trim();
                String email= Objects.requireNonNull(emailEditText.getText()).toString().trim();
                String password= Objects.requireNonNull(passwordEditText.getText()).toString().trim();

                if (!validateInputs(fName, lName, email, password)){
                    return;
                }

                User user=new User(fName,lName,email);
                user.setPassword(password);
                user.setId(currentUser.getId());
                viewModel.updateUser(user);
                viewModel.setCurrentUser(user);
                bottomSheetDialog.dismiss();
            }
        });


        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.show();
    }

    private boolean validateInputs(String fname, String lname, String email, String password) {
        if(TextUtils.isEmpty(fname)){
            nameEditText.setError("Field cannot be empty");
            return false;
        }

        if(TextUtils.isEmpty(lname)){
            lastNameEditText.setError("Field cannot be empty");
            return false;
        }

        if(TextUtils.isEmpty(email)){
            emailEditText.setError("Field cannot be empty");
            return false;
        }

        if(TextUtils.isEmpty(password)){
            passwordEditText.setError("Required");
            return false;
        } else if(password.length()>15){
            passwordEditText.setError("Password too long");
            return false;
        }
        return true;
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.main_menu_logout:
                logout();
                return true;
            case R.id.main_menu_delete_account:
                deleteAccount();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void deleteAccount() {
        viewModel.deleteUser(currentUser);
        showToast("Account Deleted Successfully");
        logout();
    }

    private void logout() {
        getSharePref().setLoggedUserId((long) -1);
        if (getSharePref().getLoggedUserId() == -1) {
            gotoLoginActivity(MainActivity.this);
            finish();
        }
    }

}
