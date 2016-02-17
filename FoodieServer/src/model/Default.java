package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import model.Location.LocationType;

/**
 * Default User g
 * @author Guangyu Chen
 * @version 1.1
 */
public class Default {

    private Default() {
        /*
         * constructor is set to private this class should never be initialized
         */
    }

    public static final String RST_COVER_PHOTO_ID = "resDefaultCoverPhoto.png";
    public static final String USER_AVATAR_URL = "userDefaultAvatar.png";
    public static final String USER_COVER_URL = "userCoverUrl.png";

    public static Location generateUserDefaultLocation() {
        double longitude = -79.9764 + new Random().nextDouble() * 0.01;
        double latitude = 40.4397 + new Random().nextDouble() * 0.01;
        Location location = new Location(longitude, latitude, LocationType.USER);
        return location;
    }

    public static Location generateRstDefaultLocation() {
        double longitude = -79.9764 + new Random().nextDouble() * 0.01;
        double latitude = 40.4397 + new Random().nextDouble() * 0.01;
        Location location = new Location(longitude, latitude, LocationType.RESTAURANT);
        return location;
    }

    public static String generatePhoneNumber() {
        long number = 4127260632L + new Random().nextInt(9999);
        return String.valueOf(number);
    }

    public static List<RstFlavorTag> getFlavorTagList() {
        List<RstFlavorTag> list = new ArrayList<>();

        int tagNum = new Random().nextInt(4) + 1;
        for (int i = tagNum * 2; i > tagNum; i--) {
            list.add(RstFlavorTag.values()[i]);
        }
        return list;
    }

    public static float generateAvgRating() {
        return new Random().nextFloat() * 5;
    }

    public static int generateRateTimes() {
        return new Random().nextInt(500);
    }

    public static float generatePrice() {
        return new Random().nextFloat() * 5;
    }
}
