package edu.cmu.ece.jsphdev.foodie.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import edu.cmu.ece.jsphdev.foodie.R;
import edu.cmu.ece.jsphdev.foodie.adapter.RestaurantAdapter;
import edu.cmu.ece.jsphdev.foodie.exception.ErrorType;
import edu.cmu.ece.jsphdev.foodie.exception.RestaurantNotFoundException;
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
 * @author  Yaoxiu Hu
 */
public class RestaurantListActivity extends FragmentActivity
        implements OnMapReadyCallback {

    private Spinner flavor_spinner;
    private Spinner sort_spinner;
    private User myUser;
    private int numberRestaurant;
    private List<Restaurant> restaurantList = new ArrayList<>();
    private GoogleMap myMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_list);

        initialize();
        setListener();
        String userJson =getIntent().getStringExtra("User");
        Gson gson = new GsonBuilder().registerTypeAdapter(User.class, new GsonHelper
                .UserDeserializer())
                .create();
        myUser = gson.fromJson(userJson, User.class);
        Location myLocation = Default.generateUserDefaultLocation();
        if(myUser != null)
            myLocation = myUser.getLocation();

        HttpFacade.getRestaurantList(myLocation, SortType.BY_PRICE, new ArrayList<RstFlavorTag>(), new RstListCallBackListener() {
            @Override
            public void onFinish(List<Long> rstList) {
                numberRestaurant = rstList.size();
                try {
                    if (rstList.size() == 0) {
                        throw new RestaurantNotFoundException(ErrorType.RESTAURANT_NOT_FOUND);
                    }
                }catch (RestaurantNotFoundException e){
                    e.setErrMsg(e.getErrMsg());
                }
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


        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map_view);
        mapFragment.getMapAsync(this);
    }


    private android.os.Handler handler = new Handler() {
        public void handleMessage(Message msg){
            List<Restaurant> restaurants = (List<Restaurant>)msg.obj;
            setAdapter();
            if(restaurants.size() == numberRestaurant) {
                double Latitude =40.4424966;
                double Longtitude=-79.9447415;
                for(Restaurant r : restaurants) {
                    Latitude = r.getLocation().getLatitude();
                    Longtitude = r.getLocation().getLongitude();
                    myMap.addMarker(new MarkerOptions().position(new LatLng(Latitude, Longtitude)).title(r.getName()));
                }
                moveMap(myMap, new LatLng(Latitude, Longtitude));
            }
        }
    };

    /**
     * To set the adapter of the class
     */
    public void setAdapter(){
        RestaurantAdapter adapter = new RestaurantAdapter(RestaurantListActivity.this,
                R.layout.restaurant_list_item, restaurantList);
        ListView listView = (ListView) findViewById(R.id.restaurant_listview);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Restaurant restaurant = restaurantList.get(position);
                Intent intent = new Intent(RestaurantListActivity.this, RestaurantInfoActivity.class);
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

                Toast.makeText(getApplicationContext(), "userinfo",
                        Toast.LENGTH_SHORT).show();
                intent.setClass(RestaurantListActivity.this, UserEditActivity.class);
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
                intent.setClass(RestaurantListActivity.this, NearByUsersActivity.class);
                //pass the user name to next activity???
                intent.putExtra("User", new Gson().toJson(myUser));
                startActivity(intent);
                break;
            case R.id.home_layout:
                Toast.makeText(getApplicationContext(), "home",
                        Toast.LENGTH_SHORT).show();
                intent.setClass(RestaurantListActivity.this, MainActivity.class);

                intent.putExtra("User", new Gson().toJson(myUser));
                startActivity(intent);
                break;
            case R.id.restaurant_nearby_layout :
                Toast.makeText(getApplicationContext(), "restaurant nearby",
                        Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void setListener() {
        flavor_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), flavor_spinner.getSelectedItem().toString(),
                        Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        sort_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //should sort
                Toast.makeText(getApplicationContext(), sort_spinner.getSelectedItem().toString(),
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initialize() {
        flavor_spinner = (Spinner) findViewById(R.id.flavor_spinner);
        List<String> flavorlist = new ArrayList<String>();
        flavorlist.add("Chinese");
        flavorlist.add("Indian");
        flavorlist.add("Japanese");
        flavorlist.add("American");
        ArrayAdapter flavorAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item, flavorlist);
        flavorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        flavor_spinner.setAdapter(flavorAdapter);


        sort_spinner = (Spinner) findViewById(R.id.sort_spinner);
        List<String> sortlist = new ArrayList<String>();
        sortlist.add("highest price");
        sortlist.add("lowest price");
        sortlist.add("highest rate");
        ArrayAdapter sortAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,sortlist);
        sortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sort_spinner.setAdapter(sortAdapter);
    }




    private void moveMap(GoogleMap mMap,LatLng place){
        CameraPosition cameraPosition=
                new CameraPosition.Builder()
                        .target(place)
                        .zoom(13)
                        .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        myMap = googleMap;
        LatLng cmu = new LatLng(40.4424966,-79.9447415);

        googleMap.setMyLocationEnabled(true);
        moveMap(googleMap, cmu);
    }

    class  onClick implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(RestaurantListActivity.this,RestaurantInfoActivity.class);
            startActivity(intent);
        }
    }


    // TODO handle exception locally
    public Bitmap getLocalBitMap(String fileAddr) {
        InputStream in = null;
        try {
            in = new FileInputStream(fileAddr);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.err.println("cannot find file 1");
        }
        return BitmapFactory.decodeStream(in);
    }

}
