package edu.cmu.ece.jsphdev.foodie.ui;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import edu.cmu.ece.jsphdev.foodie.R;
import edu.cmu.ece.jsphdev.foodie.adapter.MsgAdapter;
import edu.cmu.ece.jsphdev.foodie.exception.ErrorType;
import edu.cmu.ece.jsphdev.foodie.exception.UserNotFoundException;
import edu.cmu.ece.jsphdev.foodie.httpUtil.CallBackListener;
import edu.cmu.ece.jsphdev.foodie.httpUtil.GsonHelper;
import edu.cmu.ece.jsphdev.foodie.httpUtil.HttpFacade;
import edu.cmu.ece.jsphdev.foodie.httpUtil.MsgListCallBackListener;
import edu.cmu.ece.jsphdev.foodie.model.Msg;
import edu.cmu.ece.jsphdev.foodie.model.User;
import edu.cmu.ece.jsphdev.foodie.util.MyDataBaseHelper;

/**
 * @author Yaoxiu Hu
 * chatting with other users
 */
public class ChattingActivity extends AppCompatActivity {

    private ListView msgListView;

    private EditText inputText;

    private MsgAdapter adapter;

    private List<Msg> msgList = new ArrayList<>();

    private User myUser;

    private User nearbyUser;

    private Handler handler;

    private MyDataBaseHelper dataBaseHelper;

    private String messageRecords;

    private MyThread myThread;

    private class MyThread extends Thread{
        private boolean mRunning = false;

        @Override
        public void run() {
            mRunning = true;
            while(mRunning){
                SystemClock.sleep(1000);
                HttpFacade.getMsg(myUser, new MsgListCallBackListener() {
                    @Override
                    public void onNewMessages(List<Msg> messages) {
                        for (Msg msg : messages) {
                            msgList.add(msg);
                        }

                        Message message = new Message();
                        message.what = 1;
                        handler.sendMessage(message);
                    }

                    @Override
                    public void onNoMessages() {

                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });
            }
        }

        public void close(){
            mRunning = false;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);

        String userJson =getIntent().getStringExtra("myUser");
        Gson gson = new GsonBuilder().registerTypeAdapter(User.class, new GsonHelper
                .UserDeserializer())
                .create();
        myUser = gson.fromJson(userJson, User.class);
                String nearByuserJson =getIntent().getStringExtra("nearByUser");
                nearbyUser = gson.fromJson(nearByuserJson, User.class);

                dataBaseHelper = new MyDataBaseHelper(this,"Records.db",null,1);
                SQLiteDatabase db = dataBaseHelper.getWritableDatabase();
                Cursor cursor = db.rawQuery("select * from records where username = ? ", new String[]{nearbyUser.getUserName()});
                if (cursor.moveToFirst()) {
                    do {
                        String name = cursor.getString(cursor.getColumnIndex("username"));
                        messageRecords = cursor.getString(cursor.getColumnIndex("message"));
                        Log.d("ChattingActivity", "nearby UserName is " + name);

                    }while (cursor.moveToNext());
                }
                cursor.close();




        initMsgs(messageRecords);
        adapter = new MsgAdapter(ChattingActivity.this, R.layout.msg_item, msgList);
        inputText = (EditText) findViewById(R.id.msg_input_text);
        Button send = (Button) findViewById(R.id.msg_send);
        msgListView = (ListView) findViewById(R.id.msg_list_view);
        msgListView.setAdapter(adapter);
        myThread = new MyThread();
        myThread.start();
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what == 1){
                    adapter.notifyDataSetChanged();
                    msgListView.setSelection(msgList.size());
                }
            }
        };

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = inputText.getText().toString();
                if (!"".equals(content)) {
                    Msg msg = new Msg(myUser, nearbyUser, content);
                    HttpFacade.postMsg(msg, new CallBackListener() {
                        @Override
                        public void onFinished(String string) {

                        }

                        @Override
                        public void onError(Exception e) {

                        }
                    });
                    msgList.add(msg);
                    adapter.notifyDataSetChanged();
                    msgListView.setSelection(msgList.size());
                    inputText.setText("");
                }
            }
        });

    }


    /*
     Initialize message records
     */
    private void initMsgs(String records) {
        if(records == null){
            Msg initialMsg = new Msg(nearbyUser,myUser,"Let's talk");
            initialMsg.sendToReceive();
            msgList.add(new Msg(nearbyUser,myUser,"Let's talk"));
        }else {
            String[] messageRecord = records.split(",");
            for (int i = 0; i < messageRecord.length - 1; i = i + 2) {
                String message = messageRecord[i];
                String type = messageRecord[i + 1];
                if (type.equals("1")) {
                    Msg msg = new Msg(myUser, nearbyUser, message);

                    msgList.add(msg);
                } else {
                    Msg msg = new Msg(nearbyUser, myUser, message);
                    msg.sendToReceive();
                    msgList.add(msg);
                }
            }
        }
    }

    /**
     * For handling the top bar click
     * @param view view
     */
    public void onTopTabClick(View view){
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.back_layout:
                Toast.makeText(getApplicationContext(), "back",
                        Toast.LENGTH_SHORT).show();
                StringBuilder records = new StringBuilder();
                for (int i = 0; i < msgList.size(); i++) {
                    records.append(msgList.get(i).getContent());
                    if (msgList.get(i).getSender() == myUser) {
                        records.append("," + "1"+",");
                    } else {
                        records.append("," + "2"+",");
                    }
                }
                 String recordsDb = records.toString();
                SQLiteDatabase db = dataBaseHelper.getWritableDatabase();

                String userName = nearbyUser.getUserName();
                db.execSQL("insert into records (username, message) values(?, ?)",
                            new String[]{userName, recordsDb});
                db.close();
                finish();
                break;
            case R.id.userinfo_layout:
                intent.setClass(ChattingActivity.this, NearByUsersActivity.class);
                intent.putExtra("User", new Gson().toJson(myUser));
                startActivity(intent);
                break;
        }
    }
    /**
     * For handling the bottom bar click
     * @param view view
     */
    public void onBottomTabClick(View view){
        Intent intent = new Intent();
        switch (view.getId()){
            case R.id.people_nearby_layout :
                intent.setClass(ChattingActivity.this, NearByUsersActivity.class);
                intent.putExtra("User", new Gson().toJson(myUser));
                startActivity(intent);
                break;
            case R.id.home_layout:
                Toast.makeText(getApplicationContext(), "home",
                        Toast.LENGTH_SHORT).show();

                intent.setClass(ChattingActivity.this, MainActivity.class);
                //pass the user name to next activity???
                intent.putExtra("User", new Gson().toJson(myUser));
                startActivity(intent);
                break;
            case R.id.restaurant_nearby_layout :
                intent.setClass(ChattingActivity.this, RestaurantListActivity.class);
                intent.putExtra("User", new Gson().toJson(myUser));
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myThread.close();
    }
}
