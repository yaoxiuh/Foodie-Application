package edu.cmu.ece.jsphdev.foodie.httpUtil;

import android.util.Base64;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import edu.cmu.ece.jsphdev.foodie.model.Comment;
import edu.cmu.ece.jsphdev.foodie.model.Location;
import edu.cmu.ece.jsphdev.foodie.model.Msg;
import edu.cmu.ece.jsphdev.foodie.model.Restaurant;
import edu.cmu.ece.jsphdev.foodie.model.RstFlavorTag;
import edu.cmu.ece.jsphdev.foodie.model.SortType;
import edu.cmu.ece.jsphdev.foodie.model.User;

/**
 * @author Guangyu Chen
 * @version 1.0
 */
public class GetRequest {

    private GetRequest() {
    }

    public static void getUser(final long userId, final UserCallBackListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                User user = getUser(userId);
                if (listener != null) {
                    if (user != null) {
                        listener.onFinish(user);
                    } else {
                        listener.onError(new NoSuchElementException("No user"));
                    }
                }
            }
        }).start();

    }

    public static User getUser(long userId) {
        String request = new String(Server.HOST);
        request += Server.USER_INFO;
        request += "?userId=";
        request += userId;
        String result = null;
        try {
            result = GetRequest.getString(new URL(request));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Gson gson = new GsonBuilder().registerTypeAdapter(User.class, new GsonHelper
                .UserDeserializer())
                .create();
        User user = gson.fromJson(result, User.class);
        return user;
    }

    public static void getRestaurant(final long rstId, final RstCallBackListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Restaurant restaurant = getRestaurant(rstId);
                if (listener != null) {
                    if (restaurant != null) {
                        listener.onFinish(restaurant);
                    } else {
                        listener.onError(new NoSuchElementException("No restaurant"));
                    }
                }
            }
        }).start();
    }

    public static Restaurant getRestaurant(long rstID) {
        String request = new String();
        request += Server.HOST;
        request += Server.GET_RST;
        request += "?rstId=";
        request += rstID;

        URL url = null;
        try {
            Log.d("GetRequest", request);
            System.out.println(request);
            url = new URL(request);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }

        String response = GetRequest.getString(url);

        Gson gson = new GsonBuilder().registerTypeAdapter(User.class, new GsonHelper
                .UserDeserializer())
                .registerTypeAdapter(Comment.class, new GsonHelper.CommentDeserializer())
                .create();
        Restaurant restaurant = gson.fromJson(response, Restaurant.class);

        return restaurant;
    }

    public static void getImage(final String name, final ImageCallBackListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                byte[] bytes = getImage(name);
                if (listener != null) {
                    if (bytes != null) {
                        listener.onFinished(bytes);
                    } else {
                        listener.onError(new NoSuchElementException("No image"));
                    }
                }
            }
        }).start();
    }

    public static byte[] getImage(String name) {

        String request = new String();
        request += Server.HOST;
        request += Server.GET_IMAGE;
        request += "?name=";
        request += name;

        URL url = null;
        System.out.println(request);
        try {
            url = new URL(request);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
        String response = GetRequest.getString(url);
        Log.d("GetRequest", response);
        byte[] imageByte = Base64.decode(response, Base64.URL_SAFE);
        // System.out.println(imageByte);
        return imageByte;
    }

    public static void getRestaurantList(final Location userLocation, final SortType sortType,
                                         final List<RstFlavorTag> tagList, final
                                         RstListCallBackListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Long> rstList = getRestaurantList(userLocation, sortType, tagList);
                if (listener != null) {
                    if (rstList != null) {
                        listener.onFinish(rstList);
                    } else {
                        listener.onError(new NoSuchElementException("No Rst List"));
                    }
                }
            }
        }).start();
    }

    protected static List<Long> getRestaurantList(Location userLocation, SortType sortType,
                                                  List<RstFlavorTag> tagList) {
        String request = new String();
        request += Server.HOST;
        request += Server.RST_LIST;
        request += "?longitude=";
        request += userLocation.getLongitude();
        request += "&latitude=";
        request += userLocation.getLatitude();
        request += "&sortType=";
        request += sortType;
        request += "&tags=";
        if (tagList.size() != 0) {
            request += tagList.get(0);
        } else {
            request += RstFlavorTag.NULL;
        }

        String result = null;
        try {
            result = getString(new URL(request));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Type listType = new TypeToken<ArrayList<Long>>() {

        }.getType();

        List<Long> rstIdList = new Gson().fromJson(result, listType);

        return rstIdList;
    }

    public static void isNewUser(final String userName, final CallBackListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean result = isNewUser(userName);
                if (listener != null) {
                    if (result == true) {
                        listener.onFinished("");
                    } else {
                        listener.onError(new RuntimeException("error in updating new user"));
                    }
                }
            }
        }).start();
    }

    public static boolean isNewUser(String userName) {
        String request = new String(Server.HOST);
        request += Server.USER_AUTH;
        request += "?username=";
        request += userName;
        String result = null;
        try {
            result = getString(new URL(request));
        } catch (MalformedURLException e) {
            System.err.println("Error in querying new user");
            e.printStackTrace();
            return false;
        }
        if (result.equals("TRUE")) {
            return true;
        }
        return false;
    }

    public static void getPeopleNearby(final Location location, final User user, final
    UserListCallBackListener
            listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<User> userList = getPeopleNearby(location, user);
                if (listener != null) {
                    if (userList != null) {
                        listener.onFinish(userList);
                    } else {
                        listener.onError(new RuntimeException("Error in getting people nearby " +
                                                                      "list"));
                    }
                }
            }
        }).start();
    }

    public static List<User> getPeopleNearby(Location location, User user) {
        double longitude = location.getLongitude();
        double latitude = location.getLatitude();
        long userId = user.getUserId();

        String request = new String(Server.HOST);
        request += Server.USER_LIST;
        request += "?longitude=";
        request += longitude;
        request += "&latitude=";
        request += latitude;
        request += "&userId=";
        request += userId;

        String result = null;
        try {
            result = getString(new URL(request));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Type listType = new TypeToken<ArrayList<User>>() {
        }.getType();
        Gson gson = new GsonBuilder().registerTypeAdapter(User.class, new GsonHelper
                .UserDeserializer()).create();

        List<User> userList = gson.fromJson(result, listType);

        return userList;
    }

    public static void getMsg(final User user, final MsgListCallBackListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Msg> messages = getMsg(user);
                if (listener != null) {
                    if ((messages != null) && messages.size() != 0) {
                        listener.onNewMessages(messages);
                    } else if ((messages != null) && (messages.size() == 0)) {
                        listener.onNoMessages();
                    } else {
                        listener.onError(new NoSuchElementException("Error in receiving messages"));
                    }
                }
            }
        }).start();
    }

    public static List<Msg> getMsg(User user) {
        long userId = user.getUserId();
        String request = new String(Server.HOST);
        request += Server.CHAT;
        request += "?userId=";
        request += userId;
        System.out.println(request);
        String result = null;
        try {
            result = getString(new URL(request));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Type listType = new TypeToken<ArrayList<Msg>>() {
        }.getType();

        Gson gson = new GsonBuilder().registerTypeAdapter(User.class, new GsonHelper
                .UserDeserializer())
                .create();

        List<Msg> messages = gson.fromJson(result, listType);
        return messages;
    }

    private static String getString(URL url) {
        HttpURLConnection connection = null;

        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(3000);
            connection.setReadTimeout(3000);

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection
                                                                                     .getInputStream()));
            StringBuffer response = new StringBuffer();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            //TODO use non-blocking
            return response.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return null;
    }
}
