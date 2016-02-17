package edu.cmu.ece.jsphdev.foodie.ui;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import edu.cmu.ece.jsphdev.foodie.R;
import edu.cmu.ece.jsphdev.foodie.adapter.MainRestaurantAdapter;
import edu.cmu.ece.jsphdev.foodie.httpUtil.GsonHelper;
import edu.cmu.ece.jsphdev.foodie.httpUtil.HttpFacade;
import edu.cmu.ece.jsphdev.foodie.httpUtil.RstCallBackListener;
import edu.cmu.ece.jsphdev.foodie.httpUtil.RstListCallBackListener;
import edu.cmu.ece.jsphdev.foodie.model.Default;
import edu.cmu.ece.jsphdev.foodie.model.Location;
import edu.cmu.ece.jsphdev.foodie.model.Restaurant;
import edu.cmu.ece.jsphdev.foodie.model.RstFlavorTag;
import edu.cmu.ece.jsphdev.foodie.model.SortType;
import edu.cmu.ece.jsphdev.foodie.model.User;

/**
 * Class for handling the main activity
 */
public class MainActivity extends AppCompatActivity {

    private User myUser;
    private MainRestaurantAdapter adapter;
    private List<Restaurant> restaurantList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String userJson = getIntent().getStringExtra("User");
        Gson gson = new GsonBuilder().registerTypeAdapter(User.class, new GsonHelper
                .UserDeserializer())
                .create();
        myUser = gson.fromJson(userJson, User.class);
        Location myLocation = Default.generateUserDefaultLocation();
        if (myUser != null)
            myLocation = myUser.getLocation();

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(android.location.Location location) {
                adapter.setLocation(location.getLongitude(), location.getLatitude());
                System.out.println("Set Location");
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        };

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET}, 10);
                return;
            }
        }
        locationManager.requestLocationUpdates("gps", 1000, 1, locationListener);


        HttpFacade.getRestaurantList(myLocation, SortType.BY_PRICE, new ArrayList<RstFlavorTag>(), new RstListCallBackListener() {
            @Override
            public void onFinish(List<Long> rstList) {
                for (Long i : rstList) {
                    HttpFacade.getRestaurant(i, new RstCallBackListener() {
                        @Override
                        public void onFinish(Restaurant restaurant) {
                            restaurantList.add(restaurant);
                            Message message = new Message();
                            message.obj = restaurantList;
                            handler.sendMessage(message);
                        }

                        @Override
                        public void onError(Exception e) {

                        }
                    });
                }
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }

    private android.os.Handler handler = new Handler() {
        public void handleMessage(Message msg){
            List<Restaurant> restaurants = (List<Restaurant>)msg.obj;
            setAdapter();
        }
    };

    /**
     * Set the adapter of the class
     */
    public void setAdapter(){
        adapter = new MainRestaurantAdapter(MainActivity.this,
                R.layout.main_restaurant_item, restaurantList);
        ListView listView = (ListView) findViewById(R.id.restaurant_listview);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Restaurant restaurant = restaurantList.get(position);
                Intent intent = new Intent(MainActivity.this, RestaurantInfoActivity.class);
                intent.putExtra("restaurant", new Gson().toJson(restaurant));
                intent.putExtra("User", new Gson().toJson(myUser));
                startActivity(intent);
            }
        });
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
                if(myUser == null){
                    Toast.makeText(getApplicationContext(),
                            "Please log in or create a new account",Toast.LENGTH_SHORT).show();
                }else {
                    intent.setClass(MainActivity.this, UserEditActivity.class);
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
                if(myUser == null){
                    Toast.makeText(getApplicationContext(),
                            "Please log in or create a new account",Toast.LENGTH_SHORT).show();
                }else {
                    intent.setClass(MainActivity.this, NearByUsersActivity.class);
                    intent = new Intent(MainActivity.this, NearByUsersActivity.class);
                    intent.putExtra("User", new Gson().toJson(myUser));
                    startActivity(intent);
                }
                break;
            case R.id.home_layout:
                Toast.makeText(getApplicationContext(), "home",
                        Toast.LENGTH_SHORT).show();
                break;
            case R.id.restaurant_nearby_layout :
                intent.setClass(MainActivity.this, RestaurantListActivity.class);
                intent.putExtra("User", new Gson().toJson(myUser));
                startActivity(intent);
                break;
        }
    }



}