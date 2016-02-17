package edu.cmu.ece.jsphdev.foodie.model;

public interface User {

    /**
     * get the user name
     * @return user name
     */
     String getUserName();

    /**
     * Get the user password
     * @return the user password
     */
     String getPassword();

    /**
     * get the url of user avatar in String form
     *
     * @return url of avatar in String form; default if no avatar is set
     */
     String getAvatarId();

    /**
     * To get the user id of the user
     * @return the user id
     */
     long getUserId();

    /**
     * To get the location of the user
     * @return get the location of the user
     */
     Location getLocation();
}
