package edu.cmu.ece.jsphdev.foodie.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import edu.cmu.ece.jsphdev.foodie.R;
import edu.cmu.ece.jsphdev.foodie.httpUtil.GsonHelper;
import edu.cmu.ece.jsphdev.foodie.httpUtil.Server;
import edu.cmu.ece.jsphdev.foodie.model.Comment;
import edu.cmu.ece.jsphdev.foodie.model.User;
import edu.cmu.ece.jsphdev.foodie.util.BitmapCache;

/**
 * @author  Yaoxiu Hu
 * show the comments
 */
public class ShowCommentActivity extends AppCompatActivity {

    private User myUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_comment);

        RequestQueue mQueue = Volley.newRequestQueue(ShowCommentActivity.this);

        TextView commentContent = (TextView) findViewById(R.id.comment_content);
        NetworkImageView userImage = (NetworkImageView) findViewById(R.id.user_image);
        TextView userName = (TextView) findViewById(R.id.user_name);
        RatingBar ratingBar = (RatingBar) findViewById(R.id.show_comment_ratingBar);

        Gson gson = new GsonBuilder().registerTypeAdapter(User.class, new GsonHelper
                .UserDeserializer())
                .registerTypeAdapter(Comment.class, new GsonHelper.CommentDeserializer())
                .create();
        String commentJson = getIntent().getStringExtra("Comment");
        String userJson = getIntent().getStringExtra("User");
        Comment comment = gson.fromJson(commentJson, Comment.class);
        myUser = gson.fromJson(userJson,User.class);
        User user = comment.getCreator();
        commentContent.setText(comment.getCommentWord());
        userName.setText(user.getUserName());
        ratingBar.setRating(comment.getRating());
        String imgId = comment.getCreator().getAvatarId();
        String request = "";
        request += Server.HOST;
        request += "image/";
        request += imgId;
        ImageLoader imageLoader = new ImageLoader(mQueue, new BitmapCache());
        userImage.setDefaultImageResId(R.drawable.banana_pic);
        userImage.setErrorImageResId(R.drawable.apple_pic);
        userImage.setImageUrl(request,
                imageLoader);
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
                finish();
                break;
            case R.id.userinfo_layout:
                if(myUser == null) {
                    Toast.makeText(ShowCommentActivity.this,"Please log in or create an account",Toast.LENGTH_SHORT).show();
                }else {
                    intent.setClass(ShowCommentActivity.this, UserEditActivity.class);

                    intent.putExtra("User", new Gson().toJson(myUser));
                    startActivity(intent);
                }
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
                if(myUser == null) {
                    Toast.makeText(ShowCommentActivity.this,"Please log in or create an account",Toast.LENGTH_SHORT).show();
                }else {
                    intent.setClass(ShowCommentActivity.this, NearByUsersActivity.class);
                    //pass the user name to next activity???
                    intent.putExtra("User", new Gson().toJson(myUser));
                    startActivity(intent);
                }
                break;
            case R.id.home_layout:
                Toast.makeText(getApplicationContext(), "home",
                        Toast.LENGTH_SHORT).show();

                intent.setClass(ShowCommentActivity.this, MainActivity.class);
                intent.putExtra("User", new Gson().toJson(myUser));
                startActivity(intent);
                break;
            case R.id.restaurant_nearby_layout :
                Toast.makeText(getApplicationContext(), "restaurant nearby",
                        Toast.LENGTH_SHORT).show();
                intent.setClass(ShowCommentActivity.this, RestaurantListActivity.class);
                intent.putExtra("User", new Gson().toJson(myUser));
                startActivity(intent);
                break;
        }
    }

}
