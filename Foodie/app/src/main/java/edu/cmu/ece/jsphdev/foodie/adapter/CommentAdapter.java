package edu.cmu.ece.jsphdev.foodie.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.List;

import edu.cmu.ece.jsphdev.foodie.R;
import edu.cmu.ece.jsphdev.foodie.httpUtil.Server;
import edu.cmu.ece.jsphdev.foodie.model.Comment;
import edu.cmu.ece.jsphdev.foodie.util.BitmapCache;

/**
 * @author Yaoxiu Hu
 * Class for handling comment listview
 */
public class CommentAdapter extends ArrayAdapter<Comment> {
    private int resourceId;
    private RequestQueue mQueue;

    public CommentAdapter(Context context, int textViewResourceId,List<Comment> objects) {
        super(context,textViewResourceId,objects);
        resourceId = textViewResourceId;
        mQueue = Volley.newRequestQueue(context);

    }

    /**
     * class for single item in listview
     */
    class ViewHolder{
        NetworkImageView userImage;
        TextView userName;
        RatingBar ratingBar;
        TextView text_comment;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Comment comment = getItem(position);
        View view;
        ViewHolder viewHolder;
        if(convertView == null){
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
            viewHolder = new ViewHolder();
            viewHolder.userImage =(NetworkImageView) view.findViewById(R.id.comment_user_image);
            viewHolder.userName = (TextView) view.findViewById(R.id.comment_user_name);
            viewHolder.ratingBar = (RatingBar)view.findViewById(R.id.comment_rating_bar);
            viewHolder.text_comment = (TextView)view.findViewById(R.id.rest_info_comment);
            view.setTag(viewHolder);
        }else{
            view = convertView;
            viewHolder = (ViewHolder)view.getTag();
        }

        String imgId = comment.getCreator().getAvatarId();
        String request = "";
        request += Server.HOST;
        request += "image/";
        request += imgId;
        ImageLoader imageLoader = new ImageLoader(mQueue, new BitmapCache());
        viewHolder.userImage.setDefaultImageResId(R.drawable.banana_pic);
        viewHolder.userImage.setErrorImageResId(R.drawable.apple_pic);
        viewHolder.userImage.setImageUrl(request,
                imageLoader);
        viewHolder.userName.setText(comment.getCreator().getUserName());
        viewHolder.ratingBar.setRating(comment.getRating());
        viewHolder.text_comment.setText(comment.getCommentWord());
        return view;
    }
}
