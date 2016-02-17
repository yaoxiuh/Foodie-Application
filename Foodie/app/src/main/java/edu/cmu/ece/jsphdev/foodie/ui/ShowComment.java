package edu.cmu.ece.jsphdev.foodie.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.gson.Gson;

import edu.cmu.ece.jsphdev.foodie.R;
import edu.cmu.ece.jsphdev.foodie.httpUtil.HttpFacade;
import edu.cmu.ece.jsphdev.foodie.httpUtil.ImageCallBackListener;
import edu.cmu.ece.jsphdev.foodie.model.Comment;
import edu.cmu.ece.jsphdev.foodie.model.User;

public class ShowComment extends AppCompatActivity {

    private ImageView imageView;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_comment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView commentContent = (TextView) findViewById(R.id.comment_content);
        imageView = (ImageView)findViewById(R.id.user_image);
        TextView userName = (TextView) findViewById(R.id.user_name);
        RatingBar ratingBar = (RatingBar) findViewById(R.id.show_comment_ratingBar);

        String commentJson = getIntent().getStringExtra("Comment");
        Comment comment = new Gson().fromJson(commentJson, Comment.class);

        User user = comment.getCreator();
        commentContent.setText(comment.getCommentWord());
        userName.setText(user.getUserName());
        ratingBar.setRating(comment.getRating());
        String imageId = user.getAvatarId();

        HttpFacade.getImage(imageId, new ImageCallBackListener() {
            @Override
            public void onFinished(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                Message message = new Message();
                message.what = 2;
                message.obj = bitmap;
                handler.sendMessage(message);
            }

            @Override
            public void onError(Exception e) {

            }
        });

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                Bitmap bitmap = (Bitmap)msg.obj;
                imageView.setImageBitmap(bitmap);
            }
        };

    }

}
