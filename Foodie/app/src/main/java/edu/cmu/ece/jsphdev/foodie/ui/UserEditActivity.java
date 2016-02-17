package edu.cmu.ece.jsphdev.foodie.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import edu.cmu.ece.jsphdev.foodie.R;
import edu.cmu.ece.jsphdev.foodie.httpUtil.CallBackListener;
import edu.cmu.ece.jsphdev.foodie.httpUtil.GsonHelper;
import edu.cmu.ece.jsphdev.foodie.httpUtil.HttpFacade;
import edu.cmu.ece.jsphdev.foodie.model.User;

import static edu.cmu.ece.jsphdev.foodie.ui.CommentActivity.Bitmap2Bytes;

public class UserEditActivity extends AppCompatActivity {
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private int PICK_IMAGE_REQUEST = 1;
    private ImageButton portrait_btn;

    private EditText user_name_text;
    private EditText user_password_text;
    private EditText new_password_text;
    private Bitmap myBitmap;
    private User myUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_edit);

        String userJson =getIntent().getStringExtra("User");
        Gson gson = new GsonBuilder().registerTypeAdapter(User.class, new GsonHelper
                .UserDeserializer())
                .create();
        myUser = gson.fromJson(userJson, User.class);

        initialize();
        setListener();
    }

    /**
     * For handling the edit layout bar click
     * @param view view
     */
    public void onEditTopClick(View view){
        switch (view.getId()) {
            case R.id.edit_top_back:
                Toast.makeText(getApplicationContext(), "back",
                        Toast.LENGTH_SHORT).show();
                finish();
                break;
            case R.id.edit_top_camera:
                // create Intent to take a picture and return control to the calling application
                Toast.makeText(getApplicationContext(), "camera",
                        Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                // start the image capture Intent
                startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);

                break;
        }
    }

    private void initialize() {
        portrait_btn = (ImageButton) findViewById(R.id.portrait_btn);


        user_name_text = (EditText) findViewById(R.id.user_name_text);
        user_name_text.setText(myUser.getUserName());

        user_password_text = (EditText) findViewById(R.id.user_password_text);
        new_password_text = (EditText) findViewById(R.id.new_password_text);
    }


    private void setListener() {

        portrait_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
// Show only images, no videos or anything else
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
// Always show the chooser (if there are multiple options available)
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);







            }
        });


    }

    public void onUserEditClick(View view){
        switch (view.getId()) {
            case R.id.reset_btn :

                user_name_text.setText(null);
                user_password_text.setText(null);
                new_password_text.setText(null);
                break;
            case R.id.ok_btn :
                Toast.makeText(getApplicationContext(), "Send password", Toast.LENGTH_LONG).show();
                HttpFacade.changePassword(myUser.getUserId(), user_password_text.getText().toString(), new CallBackListener() {

                    @Override
                    public void onFinished(String string) {

                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });

                byte[] bytes = Bitmap2Bytes(myBitmap);

                HttpFacade.postUserAvatar(myUser.getUserId(), bytes, new CallBackListener() {
                    @Override
                    public void onFinished(String string) {

                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });

                finish();
                break;
        }
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            Toast.makeText(getApplicationContext(), "in handler", Toast.LENGTH_LONG).show();
                if(msg.what == 1) {
                    byte[] bytes = Bitmap2Bytes(myBitmap);
                    HttpFacade.postUserAvatar(myUser.getUserId(),bytes, new CallBackListener() {
                        @Override
                        public void onFinished(String string) {
                            Toast.makeText(getApplicationContext(), "change photo", Toast.LENGTH_LONG).show();
                            Message message = new Message();
                            message.what = 2;
                            handler.sendMessage(message);

                        }

                        @Override
                        public void onError(Exception e) {

                        }
                    });
                }

                if(msg.what == 2){
                    Toast.makeText(getApplicationContext(), "Finish", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                Bitmap rescaleBitmap = scaleDownBitmap(bitmap,200,this);

                myBitmap = rescaleBitmap;
                portrait_btn.setImageBitmap(rescaleBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            Bitmap photo = null;
            if (data != null) {
                photo = (Bitmap) data.getExtras().get("data");
            }
            Bitmap rescalePhote = scaleDownBitmap(photo,200,this);
            myBitmap = rescalePhote;
            portrait_btn.setImageBitmap(rescalePhote);

        }

        //send photo to server
    }

    public Bitmap scaleDownBitmap(Bitmap photo, int newHeight, Context context) {

        final float densityMultiplier = context.getResources().getDisplayMetrics().density;

        int h= (int) (newHeight*densityMultiplier);
        int w= (int) (h * photo.getWidth()/((double) photo.getHeight()));

        photo=Bitmap.createScaledBitmap(photo, w, h, true);

        return photo;
    }

}
