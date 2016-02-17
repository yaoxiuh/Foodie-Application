package edu.cmu.ece.jsphdev.foodie.ui;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import edu.cmu.ece.jsphdev.foodie.R;
import edu.cmu.ece.jsphdev.foodie.adapter.UserAdapter;
import edu.cmu.ece.jsphdev.foodie.httpUtil.GsonHelper;
import edu.cmu.ece.jsphdev.foodie.httpUtil.HttpFacade;
import edu.cmu.ece.jsphdev.foodie.httpUtil.UserListCallBackListener;
import edu.cmu.ece.jsphdev.foodie.model.User;
import edu.cmu.ece.jsphdev.foodie.util.ConvertLocation;

/**
 * @author Yaoxiu Hu
 * Class for handling the near by user activity
 */
public class NearByUsersActivity extends AppCompatActivity {

    private List<User> usersList = new ArrayList<>();
    private Location myLocation;
    private User myUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near_by_users);

        getLocation();
        String userJson =getIntent().getStringExtra("User");
        Gson gson = new GsonBuilder().registerTypeAdapter(User.class, new GsonHelper
                .UserDeserializer())
                .create();
        myUser = gson.fromJson(userJson, User.class);
        Log.d("MainActivity", myUser.getUserName());

        HttpFacade.getPeopleNearby(ConvertLocation.convertLocation(myLocation), myUser, new UserListCallBackListener() {
            @Override
            public void onFinish(List<User> rstList) {

                Message message = new Message();
                message.obj = rstList;
                handler.sendMessage(message);
            }

            @Override
            public void onError(Exception e) {

            }
        });


    }

    private android.os.Handler handler = new Handler() {
        public void handleMessage(Message msg){
            List<User> users = (List<User>)msg.obj;
            for(User user : users){
                usersList.add(user);
            }
            setAdapter();
        }
    };

    /**
     * To set the adapter of the class
     */
    public void setAdapter(){
        Log.d("NearbyUserList", usersList.toString());
        UserAdapter adapter = new UserAdapter(NearByUsersActivity.this,
                R.layout.user_chat_item, usersList);
        ListView listView = (ListView) findViewById(R.id.users_list_view);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                User user = usersList.get(position);
                Intent intent = new Intent(NearByUsersActivity.this, ChattingActivity.class);
                intent.putExtra("myUser", new Gson().toJson(myUser));
                intent.putExtra("nearByUser", new Gson().toJson(user));
                startActivity(intent);
            }
        });

    }

    /**
     * To get the location of user
     */
    public void getLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        String provider = locationManager.getBestProvider(new Criteria(), true);//need change
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        }
        myLocation = locationManager.getLastKnownLocation(provider);
        getLocationInfo(myLocation);
        locationManager.requestLocationUpdates(provider, 2000, 0, locationListener);
    }
    private void getLocationInfo(Location location) {
        if(location!=null){
            myLocation = location;
        }else {

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
                Toast.makeText(getApplicationContext(), "userinfo",
                        Toast.LENGTH_SHORT).show();
                intent.setClass(NearByUsersActivity.this, UserEditActivity.class);
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
                intent.setClass(NearByUsersActivity.this, NearByUsersActivity.class);
                intent.putExtra("User", new Gson().toJson(myUser));
                startActivity(intent);
                break;
            case R.id.home_layout:
                Toast.makeText(getApplicationContext(), "home",
                        Toast.LENGTH_SHORT).show();

                intent.setClass(NearByUsersActivity.this, MainActivity.class);

                intent.putExtra("User", new Gson().toJson(myUser));
                startActivity(intent);
                break;
            case R.id.restaurant_nearby_layout :
                Toast.makeText(getApplicationContext(), "restaurant nearby",
                        Toast.LENGTH_SHORT).show();
                intent.setClass(NearByUsersActivity.this, RestaurantListActivity.class);

                intent.putExtra("User", new Gson().toJson(myUser));
                startActivity(intent);
                break;
        }
    }

}
