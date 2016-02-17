package edu.cmu.ece.jsphdev.foodie.model;
import java.util.Date;
import java.util.Random;

public class ClientUser extends AbstractUser {
    public ClientUser(String userName, String password) {
        if ((userName == null) || (userName.length() == 0)) {
            throw new IllegalArgumentException("User name cannot be null or empty");
        }
        if ((password == null) || (password.length() == 0)) {
            throw new IllegalArgumentException("Password name cannot be null or empty");
        }
        this.userName = userName;
        this.password = password;
        this.avatarId = Default.USER_AVATAR_URL;
        this.coverPhotoId = Default.USER_COVER_URL;
        this.joinInDate = new Date();
        this.userId = generateUserId();
        this.location = Default.generateUserDefaultLocation();
    }

    private long generateUserId() {
        long realTime = new Date().getTime();
        int random = new Random().nextInt(5000);
        return realTime + random;
    }
}
