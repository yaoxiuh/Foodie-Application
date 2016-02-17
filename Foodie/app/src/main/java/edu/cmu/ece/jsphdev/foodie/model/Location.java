package edu.cmu.ece.jsphdev.foodie.model;

import java.util.Date;

/**
 * the class represent the location
 */
public class Location {
    private static final long ONE_YEAR = 31536000000L;
    private static final long ONE_HOUR = 3600000L;
    private double longitude;
    private double latitude;
    private Date expireTime;

    public Location(double longitude, double latitude, LocationType type) {
        this.longitude = longitude;
        this.latitude = latitude;
        expireTime = new Date();
        switch (type) {
        case RESTAURANT:
            expireTime.setTime(new Date().getTime() + ONE_YEAR);
            break;

        default:
            expireTime.setTime(new Date().getTime() + ONE_HOUR);
            break;
        }
    }

    /*
     * for server usage
     */
    public Location(double longitude, double latitude, Date expireTime) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.expireTime = expireTime;
    }

    public enum LocationType {
        USER, RESTAURANT
    }

    /**
     * get the longitude of the lcoation
     * @return longitude
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * get the latitude of the lcoation
     * @return latitude
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * To get the expire time of the location
     * @return expire time
     */
    public Date getExpireTime() {
        return expireTime;
    }

}
