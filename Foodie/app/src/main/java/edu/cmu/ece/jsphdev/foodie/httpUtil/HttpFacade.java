package edu.cmu.ece.jsphdev.foodie.httpUtil;

import java.net.MalformedURLException;
import java.util.List;

import edu.cmu.ece.jsphdev.foodie.model.Comment;
import edu.cmu.ece.jsphdev.foodie.model.Location;
import edu.cmu.ece.jsphdev.foodie.model.Msg;
import edu.cmu.ece.jsphdev.foodie.model.Restaurant;
import edu.cmu.ece.jsphdev.foodie.model.RstFlavorTag;
import edu.cmu.ece.jsphdev.foodie.model.SortType;
import edu.cmu.ece.jsphdev.foodie.model.User;

/**
 * Facade for all http requests and database connections; In general, all the connections and
 * database CRUD should be done via this util class.
 * <p/>
 * This class is a util class, all methods in this class are static method. You cannot and do not
 * have to initialize it.
 *
 * @author Guangyu Chen
 * @version 2.1
 */
public class HttpFacade {
    private HttpFacade() {
    }

    public static User getUser(long userId) {
        return GetRequest.getUser(userId);
    }

    public static void getUser(long userId, UserCallBackListener listener) {
        GetRequest.getUser(userId, listener);
    }

    /**
     * getRestaurant from remote database
     * <p/>
     * NOTE: please refer to the definition of restaurant, the cover photo are stored by its id
     * as a String form. In order to get and content of photo, you need to call getImage(String
     * photoId). Also, the photos in Comment is also in "id String" form
     * <p/>
     * TODO Exception handling if no corresponding restaurant exists
     *
     * @param rstId restaurant id
     * @return Restaurant object if restaurant exists in the database; null if no such restauarnt
     */
    public static Restaurant getRestaurant(long rstId) {
        return GetRequest.getRestaurant(rstId);
    }

    public static void getRestaurant(long rstId, RstCallBackListener listener) {
        GetRequest.getRestaurant(rstId, listener);
    }

    /**
     * @param photoId photo id
     * @return byte array of photo; default photo is no such photo exists
     * @throws MalformedURLException exceptions may happen during http request is sent
     */
    public static byte[] getImage(String photoId) {
        byte[] imageByte = GetRequest.getImage(photoId);
        if (imageByte == null) {
            //TODO sent back default photo is no such photo
            return null;
        }
        return imageByte;
    }

    public static void getImage(String photoId, ImageCallBackListener listener) {
        GetRequest.getImage(photoId, listener);
    }

    /**
     * Get a list of restaurant in the form of restaurant id, according to different sort method
     * and required tags.
     * <p/>
     * NOTE: You need to get the detailed restaurant information by calling getRestaurant(long
     * rstId) after received this restaurant id list.
     *
     * @param userLocation current location of user (also the location of current request)
     * @param sortType     type of sort, see model.SortType for details; if set null, it will
     *                     return the result of default sorting method.
     * @param tagList      list of tags, see model.RstFlavorTag for details; if set null, it will
     *                     return the request without specific required tags
     * @return list of restaurant id (Long); a empty list will be returned if no restaurant can
     * satisfy the above query request.
     */
    public static List<Long> getRestaurantList(Location userLocation, SortType sortType,
                                               List<RstFlavorTag> tagList) {
        return GetRequest.getRestaurantList(userLocation, sortType, tagList);
    }

    public static void getRestaurantList(Location userLocation, SortType sortType,
                                         List<RstFlavorTag> tagList, RstListCallBackListener
                                                 listener) {
        GetRequest.getRestaurantList(userLocation, sortType,
                                     tagList, listener);
    }

    /**
     * check if the user name has exists or not
     *
     * @param userName user name
     * @return true if user is new; false if user exists.
     */
    public static boolean isNewUser(String userName) {
        return GetRequest.isNewUser(userName);
    }

    public static void isNewUser(String userName, CallBackListener listener) {
        GetRequest.isNewUser(userName, listener);
    }
    /**
     * authenticate user name and password.
     *
     * @param userName user name
     * @param password password
     * @return user id (Long) if successfully authenticated, -1 if not;
     */
    public static User userAuth(String userName, String password) {
        return PostRequest.userAuth(userName, password);
    }

    public static void userAuth(String userName, String password, UserCallBackListener listener) {
        PostRequest.userAuth(userName, password,listener);
    }

    /**
     * Create new user.
     * <p/>
     * NOTE: This method will check whether the user is new first; if user name corrupted with
     * existed users, the new user will not be created and it will return -1. Advise use
     * isUserNew(String, String) to
     * check first.
     *
     * @param userName new user's name
     * @param password new user's password
     * @return id of new created user if created successfully; -1 if creation fails
     */
    public static User createUser(String userName, String password) {
        return PostRequest.createUser(userName, password);
    }

    public static void createUser(String userName, String password, UserCallBackListener listener) {
        PostRequest.createUser(userName, password, listener);
    }

    /**
     * change user's password
     *
     * @param userId      user id
     * @param newPassword new password
     * @return true if change successfully; false if not
     */
    public static boolean changePassword(long userId, String newPassword) {
        return PostRequest.changePassword(userId, newPassword);
    }

    public static void changePassword(long userId, String newPassword, CallBackListener listener) {
        PostRequest.changePassword(userId, newPassword, listener);
    }

    /**
     * Post a comment of restaurant to database.
     * <p/>
     * NOTE: You need to create a comment object with all required information first and post
     * this comment object to database.
     *
     * @param comment comment object
     * @return true if comment is posted successfully; false if not.
     */
    public static boolean postComment(Comment comment, long rstId) {
        return PostRequest.postComment(comment, rstId);
    }

    public static void postComment(Comment comment, long rstId, CallBackListener listener) {
        PostRequest.postComment(comment, rstId, listener);
    }

    /**
     * Get a list of user nearby
     *
     * @param location current location of user
     * @return list of nearby users in the form of user id; if no users are nearby, an empty list
     * will be returned
     */
    public static List<User> getPeopleNearby(Location location, User user) {
        return GetRequest.getPeopleNearby(location, user);
    }

    public static void getPeopleNearby(final Location location, final User user, final
    UserListCallBackListener
            listener) {
        GetRequest.getPeopleNearby(location, user, listener);
    }

    /**
     * Post user's avatar to database.
     *
     * @param userId    user id
     * @param imageByte image byte array of avatar
     * @return image id if it is successfully uploaded; -1 if not.
     */
    public static String postUserAvatar(long userId, byte[] imageByte) {
        return PostRequest.postUserAvatar(userId, imageByte);
    }

    public static void postUserAvatar(long userId, byte[] imageByte, CallBackListener listener) {
        PostRequest.postUserAvatar(userId, imageByte, listener);
    }


    /**
     * Post photo in comment to database.
     * <p/>
     * NOTE: In the creation of comment, you need to first upload all comment images, get
     * their id and add all photo's id to the new created comment. Then you can upload the new
     * created comment to database. See model.Comment for details.
     * <p/>
     * NOTE:Only upload comment image is meaningless; you have to create and upload comment
     * object afterwards.
     *
     * @param imageByte image byte array of comment image
     * @return image id if it is successfully uploaded; -1 if not.
     */
    public static String postCommentPhoto(byte[] imageByte) {
        return PostRequest.postPhoto(imageByte);
    }

    public static void postCommentPhoto(byte[] imageBytes, CallBackListener listener) {
        PostRequest.postPhoto(imageBytes, listener);
    }

    /*
     * new added chatting features
     */
    public static boolean postMsg(Msg msg) {
        return PostRequest.postMsg(msg);
    }

    public static void postMsg(Msg msg, CallBackListener listener) {
        PostRequest.postMsg(msg, listener);
    }

    public static List<Msg> getMsg(User user) {
        return GetRequest.getMsg(user);
    }

    public static void getMsg(User user, MsgListCallBackListener listener) {
        GetRequest.getMsg(user, listener);
    }
}
