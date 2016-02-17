package edu.cmu.ece.jsphdev.foodie.ui;

import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import edu.cmu.ece.jsphdev.foodie.R;
import edu.cmu.ece.jsphdev.foodie.model.Restaurant;

/**
 * @author Yaoxiu Hu
 * Show the restautrant list map
 */
public class RestaurantsListMapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    protected ArrayList<Restaurant> locations = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurants_list_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.restaurants_list_map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add makers in the map
        for(Restaurant restaurant:locations){
            /*
             * convert model.location to Android.location
             */
            double longitude = restaurant.getLocation().getLongitude();
            double latitude = restaurant.getLocation().getLatitude();
            Location location = new Location("");
            location.setLongitude(longitude);
            location.setLatitude(latitude);

            LatLng specificLoca = new LatLng(location.getLatitude(),location.getLongitude());
            mMap.addMarker(new MarkerOptions().position(specificLoca).title(restaurant.getName() + "\n"
                    + restaurant.getAddress()));
        }


        LatLng cmu = new LatLng(40.4424966,-79.9447415);
        LatLng lulu = new LatLng(40.4451489,-79.9511745);
        mMap.addMarker(new MarkerOptions().position(lulu).title("LULU"));
        mMap.addMarker(new MarkerOptions().position(cmu).title("Marker in CMU"));

        moveMap(cmu);

    }

    /**
     * move the map to the specific place
     * @param place
     */
    private void moveMap(LatLng place){
        CameraPosition cameraPosition=
                new CameraPosition.Builder()
                        .target(place)
                        .zoom(14)
                        .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }
}
