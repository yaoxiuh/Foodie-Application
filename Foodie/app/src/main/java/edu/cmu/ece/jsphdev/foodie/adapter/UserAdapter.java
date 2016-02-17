package edu.cmu.ece.jsphdev.foodie.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;

import java.util.List;

import edu.cmu.ece.jsphdev.foodie.R;
import edu.cmu.ece.jsphdev.foodie.httpUtil.Server;
import edu.cmu.ece.jsphdev.foodie.model.User;
import edu.cmu.ece.jsphdev.foodie.util.BitmapCache;


/**
 * @author  Yaoxiu Hu
 * class for handling list of users
 */
//UserAdapter for user list
public class UserAdapter extends ArrayAdapter<User> {
    private int resourceId;
    private RequestQueue mQueue;

    public UserAdapter(Context context, int textViewResourceId, List<User> objects){
        super(context,textViewResourceId,objects);
        mQueue = Volley.newRequestQueue(context);
        resourceId = textViewResourceId;
    }

    /**
     * class for handling a single user item
     */
    class ViewHolder{
        NetworkImageView userImage;
        TextView userName;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        User user = getItem(position);
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
            viewHolder = new ViewHolder();
            viewHolder.userImage = (NetworkImageView) view.findViewById(R.id.network_image_view);
            viewHolder.userName = (TextView) view.findViewById(R.id.user_name);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }

        String imgId = user.getAvatarId();
        String request = "";
        request += Server.HOST;
        request += "image/";
        request += imgId;
        ImageLoader imageLoader = new ImageLoader(mQueue, new BitmapCache());
        viewHolder.userImage.setDefaultImageResId(R.drawable.banana_pic);
        viewHolder.userImage.setErrorImageResId(R.drawable.apple_pic);
        viewHolder.userImage.setMinimumWidth(100);
        viewHolder.userImage.setMinimumHeight(100);
        viewHolder.userImage.setImageUrl(request,
                imageLoader);
        viewHolder.userName.setText(user.getUserName());
        return view;
    }

}
