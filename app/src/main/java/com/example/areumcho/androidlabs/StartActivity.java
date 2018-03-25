package com.example.areumcho.androidlabs;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class StartActivity extends Activity {

    protected static final String ACTIVITY_NAME = "StartActivity";
    private Button button;
    private Button chatButton;
    private Button weatherButton;
    private int REQUEST_CODE = 50;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(ACTIVITY_NAME, "In onCreate()");


        setContentView(R.layout.activity_start);
        button = (Button) findViewById(R.id.button);
        chatButton = (Button)findViewById(R.id.chatButton);
        weatherButton = (Button)findViewById(R.id.weatherButton);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartActivity.this, ListItemsActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        chatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(ACTIVITY_NAME, "User clicked Start Chat");
                Intent intent = new Intent(StartActivity.this, ChatWindow.class);
                startActivity(intent);


            }
        });

        weatherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.i(ACTIVITY_NAME, "User clicked Weather App");
                Intent intent = new Intent(StartActivity.this, WeatherForecast.class);
                startActivity(intent);

            }
        });


    }// end of onCreate
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

        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if(requestCode == REQUEST_CODE && resultCode == RESULT_OK){
                Log.i(ACTIVITY_NAME, "Returned to StartActivity.onActivityResult");
                String messagePassed = data.getStringExtra("Response");
                Toast.makeText(this, messagePassed, Toast.LENGTH_SHORT).show();
            }
        }


    }// end of Class
