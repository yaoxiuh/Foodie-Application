package edu.cmu.ece.jsphdev.foodie.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * The class defineds the default value for this project
 */
public class Default {

    private Default() {
        /*
         * constructor is set to private this class should never be initialized
         */
    }

    public static final String RST_COVER_PHOTO_ID = "resDefaultCoverPhoto.png";
    public static final String USER_AVATAR_URL = "userdefaultavatar.png";
    public static final String USER_COVER_URL = "userCoverUrl.png";

    public static Location generateUserDefaultLocation() {
        double longitude = -79.9764 + new Random().nextDouble() * 0.01;
        double latitude = 40.4397 + new Random().nextDouble() * 0.01;
        return new Location(longitude, latitude, Location.LocationType.USER);
    }

    /**
     * Get the default value of a location
     * @return location default value
     */
    public static Location generateRstDefaultLocation() {
        double longitude = -79.9764 + new Random().nextDouble() * 0.01;
        double latitude = 40.4397 + new Random().nextDouble() * 0.01;
        return new Location(longitude, latitude, Location.LocationType.RESTAURANT);
    }

    /**
     * To get the phone number
     * @return phone number
     */
    public static String generatePhoneNumber() {
        long number = 4127260632L + new Random().nextInt(9999);
        return String.valueOf(number);
    }

    /**
     * To get favorate tag
     * @return faverate tag
     */
    public static List<RstFlavorTag> getFlavorTagList() {
        List<RstFlavorTag> list = new ArrayList<>();
        
        int tagNum = new Random().nextInt(4) + 1;
        for (int i = tagNum * 2; i > tagNum; i--) {
            list.add(RstFlavorTag.values()[i]);
        }
        return list;
    }

    /**
     * To get average rating
     * @return average rating
     */
    public static float generateAvgRating() {
        return new Random().nextFloat() * 5;
    }

    /**
     * To get the rating times
     * @return rate times
     */
    public static int generateRateTimes() {
        return new Random().nextInt(500);
    }

    /**
     * To generate price
     * @return price
     */
    public static float generatePrice() {
        return new Random().nextFloat() * 5;
    }
}
