package com.ehmaugbogo.hng_ehma;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.ehmaugbogo.hng_ehma.Common.SharePref;
import com.ehmaugbogo.hng_ehma.Model.User;

import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {
    private SharePref sharePref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharePref=SharePref.getINSTANCE(this);
    }

    public SharePref getSharePref() {
        return sharePref;
    }

    protected void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    protected void openMainActivity(Context context, User currentUser) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(MainActivity.USER_KEY,currentUser);
        startActivity(intent);
    }

    protected void gotoRegisterActivity(Context context) {
        startActivity(new Intent(context, RegistrationActivity.class));
    }

    protected void gotoLoginActivity(Context context) {
        startActivity(new Intent(context, LoginActivity.class));
    }





}
