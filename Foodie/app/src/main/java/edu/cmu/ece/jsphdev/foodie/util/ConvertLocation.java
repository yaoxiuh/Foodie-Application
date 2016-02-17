package edu.cmu.ece.jsphdev.foodie.util;

import android.location.Location;

/**
 * @author  Yaoxiu Hu
 * Conver the location to gps
 */
public class ConvertLocation {
    public static edu.cmu.ece.jsphdev.foodie.model.Location convertLocation(Location androidLocation){
        if(androidLocation == null){
            return new edu.cmu.ece.jsphdev.foodie.model.Location(-44,78, edu.cmu.ece.jsphdev.foodie.model.Location.LocationType.USER);

        }
        else {
            double altitude = androidLocation.getAltitude();
            double longtitude = androidLocation.getLongitude();
            return new edu.cmu.ece.jsphdev.foodie.model.Location(longtitude, altitude, edu.cmu.ece.jsphdev.foodie.model.Location.LocationType.USER);
        }
    }

}
