package com.example.areumcho.androidlabs;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import java.lang.String;


import java.util.ArrayList;

public class ChatWindow extends Activity {

    protected static final String ACTIVITY_NAME = "ChatWindow";
    ListView chatView;
    EditText chatText;
    Button sendButton;
    ArrayList<String> chatList = new ArrayList<String>();
    ChatAdapter messageAdapter;
    private SQLiteDatabase tempDb;
    protected ChatDatabaseHelper chatDatabaseHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);

        chatView = (ListView) findViewById(R.id.chatView);
        chatText = (EditText) findViewById(R.id.chatText);
        sendButton = (Button) findViewById(R.id.sendButton);

//in this case, “this” is the ChatWindow, which is-A Context object
        chatDatabaseHelper = new ChatDatabaseHelper(this);
        tempDb = chatDatabaseHelper.getWritableDatabase();


        messageAdapter = new ChatAdapter(this);
        chatView.setAdapter(messageAdapter);



//        MyDatabaseHelper dhHelper = new MyDatabaseHelper();
//        SQLiteDatabase db = dbHelper.getReadableDatabase()

        // rawQuery() -> Cursor c = db.rawQuery("select * from ? where _id = ?", new String[] { “TABLENAME”, id });

        Cursor cursor = tempDb.query(false, ChatDatabaseHelper.DATABASE_NAME,
                new String[]{ChatDatabaseHelper.KEY_ID, ChatDatabaseHelper.KEY_MESSAGE},
                null, null, null, null, null, null);
        int rows = cursor.getCount(); //number of rows returned

        Log.i(ACTIVITY_NAME, "Cursor column count =" + cursor.getColumnCount());

        cursor.moveToFirst(); //move to first result


        while (!cursor.isAfterLast()) {
            Log.i(ACTIVITY_NAME, "SQL MESSAGE:" + cursor.getString(cursor.getColumnIndex(ChatDatabaseHelper.KEY_MESSAGE)));
            chatList.add(cursor.getString(cursor.getColumnIndex(ChatDatabaseHelper.KEY_MESSAGE)));
            cursor.moveToNext();

        } // end of while


        for (int i = 0; i < cursor.getColumnCount(); i++) {
                cursor.getColumnCount();
                Log.i(ACTIVITY_NAME, "column count" + cursor.getColumnName(i));
        }

            sendButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // get the text in the EditText field, and add it to your array list variable
                    String message = chatText.getText().toString();

                    ContentValues newValues = new ContentValues();

                    newValues.put(ChatDatabaseHelper.KEY_MESSAGE, chatText.getText().toString());
                    tempDb.insert(ChatDatabaseHelper.DATABASE_NAME, message, newValues);

                    chatList.add(chatText.getText().toString());


                    messageAdapter.notifyDataSetChanged();
                    //this restarts the process of getCount()/ getView()
                    chatText.setText("");
                }
            });

    }// end of onCreate



    protected void onResume() {
        super.onResume();
        Log.i(ACTIVITY_NAME, "In onResume()");
    }

    protected void onStart() {
        super.onStart();
        Log.i(ACTIVITY_NAME, "In onStart()");
    }

    protected void onPause() {
        super.onPause();
        Log.i(ACTIVITY_NAME, "In onPause()");
    }

    protected void onStop() {
        super.onStop();
        Log.i(ACTIVITY_NAME, "In onStop()");
    }

    protected void onDestroy() {
        super.onDestroy();
        Log.i(ACTIVITY_NAME, "In onDestroy()");
        tempDb.close();
    }


    private class ChatAdapter extends ArrayAdapter<String> {

        public ChatAdapter(Context ctx) {
            super(ctx, 0);
        }


        public int getCount() {
            return  chatList.size();
        } // return ArrayList size

        public String getItem(int position) {
            return chatList.get(position);
        }

        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = ChatWindow.this.getLayoutInflater();

            View result = null ;
            if(position%2 == 0) {
                result = inflater.inflate(R.layout.chat_row_incoming, null);
            }else {
                result = inflater.inflate(R.layout.chat_row_outgoing, null);
            }

            TextView message = (TextView)result.findViewById(R.id.message_text);
            message.setText(   getItem(position)  ); // get the string at position
            return result;

        }

    }

}
