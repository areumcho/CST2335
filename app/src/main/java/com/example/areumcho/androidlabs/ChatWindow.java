package com.example.areumcho.androidlabs;


import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;



public class ChatWindow extends AppCompatActivity {
    Button button5;
    ListView listView;
    EditText editText;
    ArrayList<Message> arrayList =new ArrayList<>();
    ChatDatabaseHelper dbHelper;
    boolean isTablet;
    ChatAdapter messageAdapter;

    protected static final String ACTIVITY_NAME = "chatWindow";

    SQLiteDatabase db;
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //this refers to the context which is the current activity
        dbHelper = new ChatDatabaseHelper(this);
        db = dbHelper.getWritableDatabase();
        setContentView(R.layout.activity_chat_window);
        messageAdapter =new ChatAdapter( this );
        button5=(Button) findViewById(R.id.sendButton);
        listView=(ListView) findViewById(R.id.chatView);
        editText=(EditText) findViewById(R.id.chatText);

        listView.setAdapter(messageAdapter);
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                String msg = editText.getText().toString();

                ContentValues newValues = new ContentValues();

                newValues.put(ChatDatabaseHelper.KEY_MESSAGE, msg);

                long newId = db.insert(ChatDatabaseHelper.DATABASE_NAME, "", newValues);
                Message msgTemp = new Message(newId, msg);
                Log.d("Message be like:", msgTemp.toString());
                arrayList.add(msgTemp);
                messageAdapter.notifyDataSetChanged();
                editText.setText("");

            }
        });


        //
        final Cursor cursor = db.query(false, ChatDatabaseHelper.DATABASE_NAME,
                //this means return all values for those fields
                new String[] { ChatDatabaseHelper.KEY_ID, ChatDatabaseHelper.KEY_MESSAGE},
                null,null, null, null, null, null);
        int rows = cursor.getCount() ; //number of rows returned
        cursor.moveToFirst(); //move to first result
        Log.i(ACTIVITY_NAME, "Cursor’s  column count =" + cursor.getColumnCount() );

        while(!cursor.isAfterLast() ){
//
            Log.i(ACTIVITY_NAME, "SQL MESSAGE:" + cursor.getString( cursor.getColumnIndex( ChatDatabaseHelper.KEY_MESSAGE) ) );
            //Adds to array
            Message myNewMsg = new Message(cursor.getLong(cursor.getColumnIndex( ChatDatabaseHelper.KEY_ID)), cursor.getString( cursor.getColumnIndex( ChatDatabaseHelper.KEY_MESSAGE) ));
            Log.d("message look like: ", myNewMsg.toString());
            arrayList.add(myNewMsg);
            cursor.moveToNext();
        }
        //GEt the column names for the log message???
        for(int i=0;i<cursor.getColumnCount();i++) {
            cursor.getColumnName(i);
            Log.i(ACTIVITY_NAME, "Column Name:" +  cursor.getColumnName(i));


            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int j, long l) {

                    Bundle bun = new Bundle();
                    bun.putInt("ID", j);//l is the database ID of selected item

                    String msg = arrayList.get(j).getMessage();
                    bun.putString("MESSAGE", msg);
                    bun.putLong("DBID", arrayList.get(j).getId());

                    //step 2, if a tablet, insert fragment into FrameLayout, pass data
                    if(isTablet) {
                        MessageFragment frag = new MessageFragment();
                        frag.setArguments(bun);
                        getFragmentManager().beginTransaction().replace(R.id.frameLayout, frag).commit();
                    }
                    //step 3 if a phone, transition to empty Activity that has FrameLayout
                    else //isPhone
                    {
                        Intent intnt = new Intent(ChatWindow.this, MessageDetails.class);
                        intnt.putExtra("ID", j); //pass the Database ID to next activity
                        intnt.putExtra("DBID", arrayList.get(j).getId());
                        intnt.putExtra("MESSAGE", msg);
                        startActivityForResult(intnt,1);
                    }
                }
            });

            //phone or tablet
            isTablet = (findViewById(R.id.frameLayout) != null); //find out if this is a phone or tablet


        }


    }
    private class Message{
        String msg;
        long id;
        public Message(long id1, String mess){
            id = id1;
            msg = mess;
        }
        public void setId(long newId){
            id = newId;
        }
        public void setMessage(String newMsg){
            msg = newMsg;
        }
        public long getId(){
            return id;
        }
        public String getMessage(){
            return msg;
        }
        public String toString(){
            return "Message: "+msg+"id "+id;
        }
    }
    private class ChatAdapter extends ArrayAdapter<Message>{

        public ChatAdapter(Context ctx) {
            super(ctx, 0);
        }

        public int getCount(){return arrayList.size();
        }
        public Message getItem(int position){
            return arrayList.get(position);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            //Just specifying the chat window is going to use what layout for each item????
            LayoutInflater inflater = ChatWindow.this.getLayoutInflater();
            View result = null;
            if (position % 2 == 0) {
                result = inflater.inflate(R.layout.chat_row_incoming, null);
            } else {
                result = inflater.inflate(R.layout.chat_row_outgoing, null);
            }

            TextView message = (TextView) result.findViewById(R.id.message_text);
            message.setText(getItem(position).getMessage()); // get the string at position
            return result;
        }



    }
    public void onDestroy(){
        super.onDestroy();
        if(db != null){
            db.close();
        }
        if(dbHelper != null){
            dbHelper.close();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                Bundle bun = data.getExtras();
                int result = bun.getInt("ID");
                Long dbid = bun.getLong("DBID");
                Log.d("DELETE FROM DB BEFORE: ", dbid+"");
                deleteFromDb(result, dbid);
            }
        }
    }

    public void deleteFromDb(int index, long dbid){
        Log.d("INDEX LOOKS: ", index+"");
        arrayList.remove(index);
        Log.d("DBID LOOKS: ", dbid+"");
        db.delete(ChatDatabaseHelper.DATABASE_NAME, ChatDatabaseHelper.KEY_ID + "="+ dbid, null);
        messageAdapter.notifyDataSetChanged();
    }
}