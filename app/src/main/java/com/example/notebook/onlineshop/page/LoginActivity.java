package com.example.notebook.onlineshop.page;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.notebook.onlineshop.R;
import com.example.notebook.onlineshop.helper.PreferencesManager;
import com.example.notebook.onlineshop.network.HttpRequestData;
import com.example.notebook.onlineshop.network.MultipartUtility;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    TextView textUsername, textPassword;
    Button buttonLogin;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        textUsername    = (TextView) findViewById(R.id.text_username);
        textPassword    = (TextView) findViewById(R.id.text_password);
        buttonLogin     = (Button) findViewById(R.id.button_login);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = textUsername.getText().toString();
                String password = textPassword.getText().toString();

                if (username.equals("") || password.equals("")){
                    Toast.makeText(LoginActivity.this, "Username dan pasword harus terisi", Toast.LENGTH_SHORT).show();
                }else {
                    doLogin(username, password);
                }
            }
        });
    }

    private void doLogin(final String username, final String password){
        new HttpRequestData("http://rizmaulana.cloudapp.net/API/onlineshop/index.php/service/login", new HttpRequestData.MultipartCallback() {
            @Override
            public void onPreExecuted() {
                progressDialog = new ProgressDialog(LoginActivity.this);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Autentifikasi user, harap tunggu...");
                progressDialog.show();

            }

            @Override
            public MultipartUtility parameters(String url) {
                try {
                    MultipartUtility parameter = new MultipartUtility(url, "UTF-8", new HashMap<String, String>());
                    parameter.addFormField("username", username);
                    parameter.addFormField("password", password);
                    Log.i("Parameter", username+"  "+password);
                    return  parameter;
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void onResponseServer(String string) {
                try {
                    JSONObject jsonObject = new JSONObject(string);
                    String result = jsonObject.getString("result");
                    if (result.equals("true")){
                        //Login berhasil
                        PreferencesManager.setPreferenceValue(getApplicationContext(), PreferencesManager.userId, jsonObject.getString("id_user"));
                        PreferencesManager.setPreferenceValue(getApplicationContext(), PreferencesManager.username, jsonObject.getString("user_name"));
                        PreferencesManager.setPreferenceValue(getApplicationContext(), PreferencesManager.loginStatus, "true");

                        startActivity(new Intent(getApplicationContext(), BerandaActivity.class));
                        finish();
                    }else {
                        //Login gagal
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this, "Username atau password salah", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "Terjadi kesalahan.", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            }
        }).execute();

    }
}
