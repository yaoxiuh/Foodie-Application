package edu.cmu.ece.jsphdev.foodie.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;
import com.google.gson.Gson;

import edu.cmu.ece.jsphdev.foodie.R;
import edu.cmu.ece.jsphdev.foodie.exception.ErrorType;
import edu.cmu.ece.jsphdev.foodie.exception.InputException;
import edu.cmu.ece.jsphdev.foodie.httpUtil.HttpFacade;
import edu.cmu.ece.jsphdev.foodie.httpUtil.UserCallBackListener;
import edu.cmu.ece.jsphdev.foodie.model.User;

/**
 * Class for handling authorization activity
 */
public class AuthorizationActivity extends AppCompatActivity {

    private EditText username_text;
    private EditText userpassword_text;
    private User myUser;

    /**
     * handle the include field in this activity
     * @param view view
     */
    public void onClickAuth(View view){
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.add_user_layout :
                //create user in database
                if((username_text.getText()!=null) && (userpassword_text.getText()!=null)){
                    HttpFacade.createUser(username_text.getText().toString(), userpassword_text.getText().toString(), new UserCallBackListener() {
                        @Override
                        public void onFinish(User user) {
                                myUser = user;
                                Intent intent = new Intent(AuthorizationActivity.this, MainActivity.class);
                                intent.putExtra("User", new Gson().toJson(myUser));
                                startActivity(intent);
                                Message message = new Message();
                                message.what = 2;
                                handler.sendMessage(message);
                        }

                        @Override
                        public void onError(Exception e) {
                            Message message = new Message();
                            message.what = 1;
                            handler.sendMessage(message);
                        }

                    });
                }

                else {
                    try {
                        if (username_text.getText() == null) {
                            throw new InputException(ErrorType.EMPTY_INPUT);
                        }
                        Toast.makeText(getApplicationContext(), "Please input complete information!", Toast.LENGTH_SHORT).show();
                    } catch (InputException e) {
                        e.setErrMsg(e.getErrMsg());
                    }
                }

                break;
            case R.id.log_in_layout :
                intent.setClass(AuthorizationActivity.this, RestaurantListActivity.class);
                HttpFacade.userAuth(username_text.getText().toString(), userpassword_text.getText().toString(), new UserCallBackListener() {
                    @Override
                    public void onFinish(User user) {
                            myUser = user;
                            Intent intent = new Intent(AuthorizationActivity.this, MainActivity.class);
                            intent.putExtra("User", new Gson().toJson(myUser));
                            startActivity(intent);
                    }

                    @Override
                    public void onError(Exception e) {
                        Message message = new Message();
                        message.what = 3;
                        handler.sendMessage(message);
                    }
                });
                break;

            case R.id.look_around_layout :
                intent.setClass(AuthorizationActivity.this,MainActivity.class);
                intent.putExtra("username", username_text.getText().toString());
                startActivity(intent);
                break;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorization);
        initialize();

    }

    private void initialize(){
        username_text  = (EditText)findViewById(R.id.username_text);
        userpassword_text = (EditText)findViewById(R.id.userpassword_text);
    }

    private android.os.Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                Toast.makeText(AuthorizationActivity.this, "Create Account Failed",
                        Toast.LENGTH_SHORT).show();
            } else if (msg.what == 2) {
                Toast.makeText(AuthorizationActivity.this, "Create Account successfully",
                        Toast.LENGTH_SHORT).show();
            } else if (msg.what == 3) {
                Toast.makeText(getApplicationContext(), "Please input correct information!", Toast.LENGTH_SHORT).show();
            }
        }
    };




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_authorization, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
