package edu.cmu.ece.jsphdev.foodie.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import edu.cmu.ece.jsphdev.foodie.R;
import edu.cmu.ece.jsphdev.foodie.adapter.CommentAdapter;
import edu.cmu.ece.jsphdev.foodie.adapter.ImgAdapter;
import edu.cmu.ece.jsphdev.foodie.exception.CommentNotFoundException;
import edu.cmu.ece.jsphdev.foodie.exception.ErrorType;
import edu.cmu.ece.jsphdev.foodie.httpUtil.GsonHelper;
import edu.cmu.ece.jsphdev.foodie.httpUtil.Server;
import edu.cmu.ece.jsphdev.foodie.model.Comment;
import edu.cmu.ece.jsphdev.foodie.model.Default;
import edu.cmu.ece.jsphdev.foodie.model.Restaurant;
import edu.cmu.ece.jsphdev.foodie.model.RstFlavorTag;
import edu.cmu.ece.jsphdev.foodie.model.User;
import edu.cmu.ece.jsphdev.foodie.util.BitmapCache;
import edu.cmu.ece.jsphdev.foodie.util.CalculateDistance;

public class RestaurantInfoActivity extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback, ViewPager.OnPageChangeListener {

    private Button writeReviewBtn;
    private Button callBtn;
    private GoogleMap map;
    private Restaurant restaurant;
    private edu.cmu.ece.jsphdev.foodie.model.Location myLocation;
    private User myUser;
    private List<String> photoId = new ArrayList<>();
    private List<Comment> commentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RequestQueue mQueue = Volley.newRequestQueue(RestaurantInfoActivity.this);
        setContentView(R.layout.activity_restaurant_info);
        getLocation();
        Gson gson = new GsonBuilder().registerTypeAdapter(User.class, new GsonHelper
                .UserDeserializer())
                .registerTypeAdapter(Comment.class, new GsonHelper.CommentDeserializer())
                .create();
        String restaurantJson = getIntent().getStringExtra("restaurant");

        restaurant = gson.fromJson(restaurantJson, Restaurant.class);

        commentList = restaurant.getComments();
        try {
            if (commentList.size() == 0) {
                throw (new CommentNotFoundException(ErrorType.COMMENT_NOT_FOUND));
            } else {
                for (Comment comment : commentList) {
                    if (comment != null) {
                        List<String> temp = comment.getPhotoList();
                        if (temp != null) {
                            for (String s : temp)
                                photoId.add(s);
                        }
                    }
                }
            }
        }catch (CommentNotFoundException e){
            e.setErrMsg(e.getErrMsg());
        }

        String userJson =getIntent().getStringExtra("User");
        myUser = gson.fromJson(userJson, User.class);
        if(myUser != null)
        myLocation = myUser.getLocation();
        else
        myLocation = Default.generateUserDefaultLocation();

        setRestaurantInfo();

        ImageLoader imageLoader = new ImageLoader(mQueue, new BitmapCache());
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        List<NetworkImageView> mImageViews = new ArrayList<>();
        for(int i = 0; i < photoId.size(); i++){
            String imgId = photoId.get(i);
            String request = "";
            request += Server.HOST;
            request += "image/";
            request += imgId;
            NetworkImageView imageView = new NetworkImageView(RestaurantInfoActivity.this);
            mImageViews.add(imageView);
            imageView.setErrorImageResId(R.drawable.banana_pic);
            imageView.setDefaultImageResId(R.drawable.apple_pic);
            imageView.setImageUrl(request,imageLoader);
            if(mImageViews.size() == photoId.size()) {
                viewPager.setAdapter(new ImgAdapter(mImageViews));
                viewPager.addOnPageChangeListener(RestaurantInfoActivity.this);
                viewPager.setCurrentItem((mImageViews.size()) * 100);
            }
        }

        //Comments Adapter
        CommentAdapter commentAdapter = new CommentAdapter(RestaurantInfoActivity.this,
                R.layout.comment_item, commentList);
        ListView listView = (ListView) findViewById(R.id.comment_list_view);
        listView.setAdapter(commentAdapter);
        commentAdapter.notifyDataSetChanged();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Comment comment = commentList.get(position);
                Intent intent = new Intent(RestaurantInfoActivity.this, ShowCommentActivity.class);
                intent.putExtra("Comment", new Gson().toJson(comment));
                intent.putExtra("User",new Gson().toJson(myUser));
                startActivity(intent);
            }
        });

        callBtn.setOnClickListener(this);
        writeReviewBtn.setOnClickListener(this);
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.rest_info_map);
        mapFragment.getMapAsync(this);
    }


    @SuppressLint("SetTextI18n")
    private void setRestaurantInfo(){
        //Set restaurant name
        TextView restaurantName = (TextView)findViewById(R.id.restaurant_info_name);
        restaurantName.setText(restaurant.getName());
        //Set restaurant distance
        TextView restaurant_distance = (TextView)findViewById(R.id.rest_info_distance);
        //Calculate distance
        double distance = CalculateDistance.GetDistance(myLocation.getLatitude(), myLocation.getLongitude(),
                restaurant.getLocation().getLatitude(), restaurant.getLocation().getLongitude());
        restaurant_distance.setText("Distance " + Double.toString(distance) + " m");

        //Set the price
        TextView restaurant_price = (TextView)findViewById(R.id.rest_info_price);
        restaurant_price.setText("$ " + String.valueOf(restaurant.getPrice()));

        //Set review times
        TextView restaurantReivewNum = (TextView)findViewById(R.id.rest_info_number_reviews);
        restaurantReivewNum.setText("Reviews " + String.valueOf(restaurant.getRateTimes()));

        //Set tag
        TextView restaurantTag = (TextView)findViewById(R.id.rest_info_tag);
        StringBuilder tag = new StringBuilder();
        for(RstFlavorTag t : restaurant.getTagList()){
            tag.append(" ");
            tag.append(t);
        }
        restaurantTag.setText(tag);

        callBtn = (Button)findViewById(R.id.rest_info_call);
        callBtn.setText("Call:" + restaurant.getPhoneNumber());
        writeReviewBtn = (Button)findViewById(R.id.rest_info_write_review);

        RatingBar ratingBar = (RatingBar) findViewById(R.id.rest_info_ratingBar);
        ratingBar.setRating(restaurant.getAverageRating());

        //Set Address
        TextView restAddress = (TextView)findViewById(R.id.rest_info_address);
        restAddress.setText("Address: " + restaurant.getAddress());
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.rest_info_write_review:
                Intent intent = new Intent(RestaurantInfoActivity.this,CommentActivity.class);
                if(myUser == null) {
                 Toast.makeText(RestaurantInfoActivity.this,"Please log in or create an account",Toast.LENGTH_SHORT).show();
                }else {
                    intent.putExtra("User", new Gson().toJson(myUser));
                    intent.putExtra("restaurantId", restaurant.getId());
                    startActivity(intent);
                }
                break;
            case R.id.rest_info_call:
                String phoneNumber = callBtn.getText().toString().split(":")[1];
                String tel = "tel:"+ phoneNumber;
                Intent intent1 = new Intent(Intent.ACTION_DIAL);
                intent1.setData(Uri.parse(tel));
                startActivity(intent1);
        }
    }

    private void moveMap(LatLng place){
        CameraPosition cameraPosition=
                new CameraPosition.Builder()
                        .target(place)
                        .zoom(13)
                        .build();
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
            map = googleMap;
        map.setMyLocationEnabled(true);
        LatLng restaurant_addr = new LatLng(restaurant.getLocation().getLatitude(),restaurant.getLocation().getLongitude());
        map.addMarker(new MarkerOptions().position(restaurant_addr).title(restaurant.getName()));
        moveMap(restaurant_addr);
    }

    /**
     * To get the location of a restaurant
     */
    public void getLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        String provider = locationManager.getBestProvider(new Criteria(), true);//need change
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

        }
        locationManager.requestLocationUpdates(provider, 2000, 0, locationListener);
    }

    private void getLocationInfo(Location location) {
        String latLongInfo;
        if(location!=null){
            double lat=location.getLatitude();
            double lng=location.getLongitude();
            latLongInfo="Lat:"+lat+"\nLong:"+lng;
          //  myLocation = location;
        }else {
            latLongInfo="No location found";
        }

    }
    private final LocationListener locationListener =new LocationListener() {
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            // TODO Auto-generated method stub

        }
        @Override
        public void onProviderEnabled(String provider) {
            getLocationInfo(null);

        }
        @Override
        public void onProviderDisabled(String provider) {
            getLocationInfo(null);
        }
        @Override
        public void onLocationChanged(Location location) {
            getLocationInfo(location);
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
                if(myUser == null) {
                    Toast.makeText(RestaurantInfoActivity.this,"Please log in or create an account",Toast.LENGTH_SHORT).show();
                }else {
                    intent.setClass(RestaurantInfoActivity.this, UserEditActivity.class);
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
                    Toast.makeText(RestaurantInfoActivity.this,"Please log in or create an account",Toast.LENGTH_SHORT).show();
                }else {
                    intent.setClass(RestaurantInfoActivity.this, NearByUsersActivity.class);
                    //pass the user name to next activity???
                    intent.putExtra("User", new Gson().toJson(myUser));
                    startActivity(intent);
                }
                break;
            case R.id.home_layout:
                Toast.makeText(getApplicationContext(), "home",
                        Toast.LENGTH_SHORT).show();

                intent.setClass(RestaurantInfoActivity.this, MainActivity.class);
                intent.putExtra("User", new Gson().toJson(myUser));
                startActivity(intent);
                break;
            case R.id.restaurant_nearby_layout :
                Toast.makeText(getApplicationContext(), "restaurant nearby",
                        Toast.LENGTH_SHORT).show();
                intent.setClass(RestaurantInfoActivity.this, RestaurantListActivity.class);
                intent.putExtra("User", new Gson().toJson(myUser));
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
