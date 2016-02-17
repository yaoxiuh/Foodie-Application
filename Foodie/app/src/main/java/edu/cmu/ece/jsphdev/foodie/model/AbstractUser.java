package edu.cmu.ece.jsphdev.foodie.model;

import java.util.Date;

/**
 * Abstract Class for User
 */
public abstract class AbstractUser implements User {

    protected String userName;
    protected String password;
    protected String avatarId; // head portrait
    protected String coverPhotoId;
    protected Date joinInDate;
    protected long userId;
    protected Location location;

    /**
     * get the user name
     * @return user name
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Get the user password
     * @return the user password
     */
    public String getPassword() {
        return password;
    }

    /**
     * get the url of user avatar in String form
     *
     * @return url of avatar in String form; default if no avatar is set
     */
    public String getAvatarId() {
        return avatarId;
    }

    /**
     * To get the user id of the user
     * @return the user id
     */
    public long getUserId() {
        return userId;
    }

    /**
     * To get the location of the user
     * @return get the location of the user
     */
    public Location getLocation() {
        return location;
    }

    // TODO chat history
    // TODO comment history
}
