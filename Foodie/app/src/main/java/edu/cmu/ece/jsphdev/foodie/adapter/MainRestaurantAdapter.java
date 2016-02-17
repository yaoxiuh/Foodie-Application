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
 * This class is for handling listview in main activity
 */
public class MainRestaurantAdapter extends ArrayAdapter<Restaurant> {
    private final RequestQueue mQueue;
    private final int resourceId;

    private double longitude = 0;
    private double latitude = 0;

    public MainRestaurantAdapter(final Context context, int textViewResourceId, List<Restaurant> objects) {
        super(context, textViewResourceId, objects);
        mQueue = Volley.newRequestQueue(context);
        resourceId = textViewResourceId;
    }

    /**
     * class for handling an item in list view
     */
    class ViewHolder{
        NetworkImageView restaurantImage;
        TextView restaurantName;
        RatingBar ratingBar;
        TextView distance;
        TextView reviewNum;
    }

    /**
     * set the location of an restaurant
     * @param longitude longitude
     * @param latitude latitude
     */
    public void setLocation(double longitude, double latitude){
        this.longitude = longitude;
        this.latitude = latitude;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Restaurant restaurant = getItem(position);

        View view;
        ViewHolder viewHolder;
        if(convertView == null){
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
            viewHolder = new ViewHolder();
            viewHolder.restaurantImage =(NetworkImageView) view.findViewById(R.id.restaurant_photo);
            viewHolder.restaurantName = (TextView) view.findViewById(R.id.tv_name);
            viewHolder.ratingBar = (RatingBar)view.findViewById(R.id.rating_bar);
            viewHolder.distance = (TextView)view.findViewById(R.id.tv_distance);
            viewHolder.reviewNum = (TextView)view.findViewById(R.id.tv_review);

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
        viewHolder.reviewNum.setText(String.valueOf(restaurant.getRateTimes()));
       viewHolder.distance.setText(Double.toString(CalculateDistance.GetDistance(restaurant.getLocation().getLongitude(), restaurant.getLocation().getLatitude(), Default.generateRstDefaultLocation().getLongitude(), Default.generateRstDefaultLocation().getLatitude())));

        System.out.println("longitude : "+longitude);
        System.out.println("latitude : " + latitude);


        return view;
    }

}
