package edu.cmu.ece.jsphdev.foodie.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;

import java.util.List;

import edu.cmu.ece.jsphdev.foodie.R;
import edu.cmu.ece.jsphdev.foodie.httpUtil.Server;
import edu.cmu.ece.jsphdev.foodie.model.Default;
import edu.cmu.ece.jsphdev.foodie.model.Restaurant;
import edu.cmu.ece.jsphdev.foodie.util.BitmapCache;
import edu.cmu.ece.jsphdev.foodie.util.CalculateDistance;

/**
 * @author  Yaoxiu Hu
 * class for handling restaurant listview
 */
public class RestaurantAdapter extends ArrayAdapter<Restaurant> {
    private int resourceId;
    private RequestQueue mQueue;

    public RestaurantAdapter(Context context, int textViewResourceId, List<Restaurant> objects){
        super(context,textViewResourceId,objects);
        mQueue = Volley.newRequestQueue(context);
        resourceId = textViewResourceId;
    }

    /**
     * class for handling a single restaurant item
     */
    class ViewHolder{
        NetworkImageView restaurantImage;
        TextView restaurantName;
        RatingBar ratingBar;
        TextView distance;
        TextView reviewNum;
        TextView address;
        TextView restaurantTag;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       Restaurant restaurant = getItem(position);
        View view;
        ViewHolder viewHolder;
        if(convertView == null){
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
            viewHolder = new ViewHolder();
            viewHolder.restaurantImage =(NetworkImageView) view.findViewById(R.id.iv_photo);
            viewHolder.restaurantName = (TextView) view.findViewById(R.id.tv_name);
            viewHolder.ratingBar = (RatingBar)view.findViewById(R.id.rating_bar);
            viewHolder.distance = (TextView)view.findViewById(R.id.tv_distance);
            viewHolder.reviewNum = (TextView)view.findViewById(R.id.tv_review);
            viewHolder.address = (TextView)view.findViewById(R.id.tv_address);
            viewHolder.restaurantTag = (TextView)view.findViewById(R.id.tv_type);

            view.setTag(viewHolder);
        }else{
            view = convertView;
            viewHolder = (ViewHolder)view.getTag();
        }

        String imgId = restaurant.getCoverPhotoId();
        String request = "";
        request += Server.HOST;
        request += "image/";
        request += imgId;
        ImageLoader imageLoader = new ImageLoader(mQueue, new BitmapCache());
        viewHolder.restaurantImage.setDefaultImageResId(R.drawable.banana_pic);
        viewHolder.restaurantImage.setErrorImageResId(R.drawable.apple_pic);
        viewHolder.restaurantImage.setImageUrl(request,
                imageLoader);
        viewHolder.restaurantName.setText(restaurant.getName());
        viewHolder.ratingBar.setRating(restaurant.getAverageRating());
        viewHolder.address.setText(restaurant.getAddress());
        viewHolder.restaurantTag.setText(restaurant.getTagList().get(0).toString());
        viewHolder.reviewNum.setText(String.valueOf(restaurant.getRateTimes()));
        viewHolder.distance.setText(Double.toString(CalculateDistance.GetDistance(restaurant.getLocation().getLongitude(), restaurant.getLocation().getLatitude(), Default.generateRstDefaultLocation().getLongitude(), Default.generateRstDefaultLocation().getLatitude())));

        return view;
    }

}
