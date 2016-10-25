package com.example.notebook.onlineshop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.notebook.onlineshop.helper.PreferencesManager;
import com.example.notebook.onlineshop.page.BerandaActivity;
import com.example.notebook.onlineshop.page.LoginActivity;

public class LauncherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        String loginStatus = PreferencesManager.getPreferanceValue(getApplicationContext(), PreferencesManager.loginStatus);
        if (loginStatus.equals("true")){
            startActivity(new Intent(getApplicationContext(), BerandaActivity.class));
            finish();
        }else {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();
        }

    }
}
