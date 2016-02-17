package edu.cmu.ece.jsphdev.foodie.httpUtil;

/**
 * @author Guangyu Chen
 * @version 1.0
 */
public class Server {
    private Server() {
    }
    public static final String HOST = "http://128.237.163.40:8080/";
    public static final String GET_RST = "FoodieServer/restaurant";
    public static final String GET_IMAGE = "FoodieServer/image";
    public static final String RST_LIST = "FoodieServer/RestaurantList";
    public static final String CHAT = "FoodieServer/chat";
    public static final String USER_LIST = "FoodieServer/PeopleNearbyList";
    public static final String USER_AUTH = "FoodieServer/auth";
    public static final String USER_INFO = "FoodieServer/User";
    public static final String USER_UPDATE = "FoodieServer/UserUpdate";
}