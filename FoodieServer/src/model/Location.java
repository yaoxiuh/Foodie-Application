package model;

import java.util.Date;

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

    public Location(double longitude, double latitude, Date expireTime) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.expireTime = expireTime;
    }

    public enum LocationType {
        USER, RESTAURANT
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public Date getExpireTime() {
        return expireTime;
    }

}
