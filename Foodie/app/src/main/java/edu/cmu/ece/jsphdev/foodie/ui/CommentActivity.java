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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.cmu.ece.jsphdev.foodie.R;
import edu.cmu.ece.jsphdev.foodie.httpUtil.CallBackListener;
import edu.cmu.ece.jsphdev.foodie.httpUtil.GsonHelper;
import edu.cmu.ece.jsphdev.foodie.httpUtil.HttpFacade;
import edu.cmu.ece.jsphdev.foodie.model.ClientComment;
import edu.cmu.ece.jsphdev.foodie.model.User;

/**
 * @author  Yaoxiu Hu
 * Comment a restaurant
 */
public class CommentActivity extends AppCompatActivity implements RatingBar.OnRatingBarChangeListener,View.OnClickListener{

    public RatingBar commentRatingBar;
    private EditText editComment;
    private Float actualRating;
    private int PICK_IMAGE_REQUEST = 1;
    private ImageView[] picture;
    private List<Bitmap> bitmaps = new ArrayList<>();
    private int pictureIndex = 0;
    private List<String> photoId = new ArrayList<>();
    private User myUser;
    private long restaurantId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        String userJson =getIntent().getStringExtra("User");
        Gson gson = new GsonBuilder().registerTypeAdapter(User.class, new GsonHelper
                .UserDeserializer())
                .create();
        myUser = gson.fromJson(userJson, User.class);

        restaurantId = getIntent().getLongExtra("restaurantId",0);

        commentRatingBar = (RatingBar)findViewById(R.id.comment_ratingBar);
        commentRatingBar.setMax(100);
        commentRatingBar.setProgress(20);
        commentRatingBar.setOnRatingBarChangeListener(this);


        Button btnaddimg = (Button) findViewById(R.id.btnaddimg);
        btnaddimg.setOnClickListener(this);

        editComment = (EditText)findViewById(R.id.edit_comment);
        Button postBtn = (Button) findViewById(R.id.post_comment_btn);
        postBtn.setOnClickListener(this);

        picture = new ImageView[3];
        picture[0] = (ImageView) findViewById(R.id.iv_pic0);
        picture[1] = (ImageView) findViewById(R.id.iv_pic1);
        picture[2] = (ImageView) findViewById(R.id.iv_pic2);
    }

    @Override
    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

        // TODO Auto-generated method stub
        actualRating = rating;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.post_comment_btn:
                for(Bitmap bitmap: bitmaps){
                    if(bitmap != null) {
                        byte[] bytes = Bitmap2Bytes(bitmap);
                        HttpFacade.postCommentPhoto(bytes, new CallBackListener() {
                            @Override
                            public void onFinished(String string) {
                                photoId.add(string);
                                Message message = new Message();
                                message.what = photoId.size() == bitmaps.size() ? 1 : 0;
                                handler.sendMessage(message);
                            }

                            @Override
                            public void onError(Exception e) {

                            }
                        });
                    }
                }

                //Send data to server
                break;
            case R.id.btnaddimg:
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
                break;
        }

    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == 1) {
                String commentContent = editComment.getText().toString();
                ClientComment comment = new ClientComment(myUser,actualRating,commentContent, photoId);
                HttpFacade.postComment(comment, restaurantId, new CallBackListener() {
                    @Override
                    public void onFinished(String string) {
                        Message message = new Message();
                        message.what = 2;
                        handler.sendMessage(message);
                        Log.d("CommentActivity",string);
                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });
            }
            if(msg.what == 2){
                Toast.makeText(getApplicationContext(), "Comment Success",
                        Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    };
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
                finish();
                break;
            case R.id.userinfo_layout:
                Toast.makeText(getApplicationContext(), "userinfo",
                        Toast.LENGTH_SHORT).show();
                intent.setClass(CommentActivity.this, UserEditActivity.class);
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
                Toast.makeText(getApplicationContext(), "people nearby",
                        Toast.LENGTH_SHORT).show();
                intent.setClass(CommentActivity.this, NearByUsersActivity.class);
                intent.putExtra("User", new Gson().toJson(myUser));
                startActivity(intent);
                break;
            case R.id.home_layout:
                Toast.makeText(getApplicationContext(), "home",
                        Toast.LENGTH_SHORT).show();

                intent.setClass(CommentActivity.this, MainActivity.class);
                intent.putExtra("User", new Gson().toJson(myUser));
                startActivity(intent);
                break;
            case R.id.restaurant_nearby_layout :
                Toast.makeText(getApplicationContext(), "restaurant nearby",
                        Toast.LENGTH_SHORT).show();
                intent.setClass(CommentActivity.this, RestaurantListActivity.class);
                intent.putExtra("User", new Gson().toJson(myUser));
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                Bitmap rescaleBitmap = scaleDownBitmap(bitmap,60,this);
                bitmaps.add(rescaleBitmap);
                picture[pictureIndex].setImageBitmap(rescaleBitmap);
                pictureIndex = (pictureIndex+1)%3;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * To change the scalre of picture
     * @param photo the photo
     * @param newHeight the height
     * @param context context
     * @return bitmap
     */
    public Bitmap scaleDownBitmap(Bitmap photo, int newHeight, Context context) {

        final float densityMultiplier = context.getResources().getDisplayMetrics().density;

        int h= (int) (newHeight*densityMultiplier);
        int w= (int) (h * photo.getWidth()/((double) photo.getHeight()));

        photo=Bitmap.createScaledBitmap(photo, w, h, true);

        return photo;
    }

    /**
     * To change bitmap to byte array
     * @param bm bitmap
     * @return array
     */
    public static byte[] Bitmap2Bytes(Bitmap bm)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }
}
