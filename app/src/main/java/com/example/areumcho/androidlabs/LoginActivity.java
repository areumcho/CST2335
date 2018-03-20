package com.example.areumcho.androidlabs;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends Activity {


    protected static final String ACTIVITY_NAME = "LoginActivity";
    private EditText emailAddress,password;
    private Button btnLogin;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(ACTIVITY_NAME, "In onCreate()");

        setContentView(R.layout.activity_login);


    btnLogin = (Button) findViewById(R.id.btnLogin);
    emailAddress = (EditText) findViewById(R.id.emailAddress);
    password = (EditText) findViewById(R.id.password);
    sharedPreferences = getSharedPreferences("login",MODE_PRIVATE);

    String email = sharedPreferences.getString("DefaultEmail","");
        emailAddress.setText(email);

        btnLogin.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            editor = sharedPreferences.edit();
            editor.putString("DefaultEmail",emailAddress.getText().toString());
            editor.commit();
            Intent intent = new Intent(LoginActivity.this, StartActivity.class);
            startActivity(intent);

        }
    });

    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.i(ACTIVITY_NAME, "In onResume()");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(ACTIVITY_NAME, "In onStart()");

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(ACTIVITY_NAME, "In onPause()");

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(ACTIVITY_NAME, "In onStop()");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(ACTIVITY_NAME, "In onDestroy()");

    }
} // end of class
