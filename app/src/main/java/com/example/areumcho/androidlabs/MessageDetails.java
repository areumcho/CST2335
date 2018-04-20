package com.example.areumcho.androidlabs;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

//
//
// For the case where this is running on a phone,
public class MessageDetails extends Activity {


    TextView textViewMessage;
    TextView textViewID;
    Button deleteButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment);

        textViewMessage = (TextView) findViewById(R.id.textViewMessage);

        textViewID = (TextView) findViewById(R.id.textViewID);

        deleteButton = (Button) findViewById(R.id.deleteButton);




        MessageFragment frag = new MessageFragment();

        frag.setArguments(getIntent().getExtras());
        getFragmentManager().beginTransaction().replace(R.id.frameLayout, frag).commit();
    }
}


