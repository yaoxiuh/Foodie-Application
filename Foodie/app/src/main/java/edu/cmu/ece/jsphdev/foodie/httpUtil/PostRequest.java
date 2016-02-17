package edu.cmu.ece.jsphdev.foodie.httpUtil;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.InvalidParameterException;
import java.util.NoSuchElementException;

import edu.cmu.ece.jsphdev.foodie.model.Comment;
import edu.cmu.ece.jsphdev.foodie.model.Msg;
import edu.cmu.ece.jsphdev.foodie.model.User;

/**
 * @author Guangyu Chen
 */
public class PostRequest {

    private PostRequest() {
    }

    public static void createUser(final String userName, final String password, final
    UserCallBackListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                User user = createUser(userName, password);
                if (listener != null) {
                    if (user != null) {
                        listener.onFinish(user);
                    } else {
                        listener.onError(new NoSuchElementException("User creation failed"));
                    }
                }
            }
        }).start();
    }

    public static User createUser(String userName, String password) {
        if ((userName == null) || (userName.length() == 0) || (password == null)
                || (password.length() == 0)) {
            return null;
        }
        if (!GetRequest.isNewUser(userName)) {
            System.out.println(userName + " has already exist.");
            return null;
        }
        String request = new String(Server.HOST);
        request += Server.USER_AUTH;
        request += "?type=create";
        request += "&username=";
        request += userName;
        request += "&password=";
        request += password;
        String result = null;
        try {
            result = postJsonResult(new URL(request), "");
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
        Gson gson = new GsonBuilder().registerTypeAdapter(User.class, new GsonHelper
                .UserDeserializer())
                .create();
        User user = gson.fromJson(result, User.class);
        return user;
    }

    public static void userAuth(final String userName, final String password, final
    UserCallBackListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                User user = userAuth(userName, password);
                if (listener != null) {
                    if (user != null) {
                        listener.onFinish(user);
                    } else {
                        listener.onError(new RuntimeException("Error in auth"));
                    }
                }
            }
        }).start();

    }

    public static User userAuth(String userName, String password) {
        if ((userName == null) || (userName.length() == 0) || (password == null) ||
                (password.length() == 0)) {
            System.err.println("both userName and password cannot be null or empty");
            return null;
        }
        String request = new String(Server.HOST);
        request += Server.USER_AUTH;
        request += "?type=auth";
        request += "&username=";
        request += userName;
        request += "&password=";
        request += password;
        String result = null;
        try {
            result = postJsonResult(new URL(request), "");
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
        if (Long.valueOf(result) > 0) {
            return GetRequest.getUser(Long.valueOf(result));
        }
        return null;
    }

    public static void changePassword(final long userId, final String newPassword, final
    CallBackListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean result = changePassword(userId, newPassword);
                if (listener != null) {
                    if (result == true) {
                        listener.onFinished("");
                    } else {
                        listener.onError(new RuntimeException("Error in change password"));
                    }
                }
            }
        }).start();

    }

    public static boolean changePassword(long userId, String newPassword) {
        if ((userId < 0) || (newPassword == null) || (newPassword.length() == 0)) {
            System.err.println("both userId and password cannot be null or empty");
            return false;
        }
        String request = new String(Server.HOST);
        request += Server.USER_UPDATE;
        request += "?type=password";
        request += "&userId=";
        request += userId;
        request += "&newPassword=";
        request += newPassword;
        String result = null;
        try {
            result = postJsonResult(new URL(request), "");
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return false;
        }
        if (result.equals("SUCCESS")) {
            return true;
        } else {
            return false;
        }
    }

    public static void postComment(final Comment comment, final long rstId, final
    CallBackListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean postSuccess = postComment(comment, rstId);
                if (listener != null) {
                    if (postSuccess) {
                        listener.onFinished("SUCCESS");
                    } else {
                        listener.onError(new RuntimeException("Error in post comment"));
                    }
                }
            }
        }).start();
    }

    public static boolean postComment(Comment comment, long rstId) {
        String commentString = new Gson().toJson(comment);
        System.out.println(commentString);
        String s = Server.HOST + Server.GET_RST + "?rstId=" + rstId;
        URL url = null;
        try {
            url = new URL(s);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return false;
        }
        String result = postJsonResult(url, commentString);
        System.out.print(result);
        return true;
    }

    public static void postUserAvatar(final long userId, final byte[] imageByte,
                                      final CallBackListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String result = postUserAvatar(userId, imageByte);
                if (listener != null) {
                    if (result != null) {
                        listener.onFinished(result);
                    } else {
                        listener.onError(new RuntimeException("Error in updating user avatar"));
                    }
                }
            }
        }).start();

    }

    public static String postUserAvatar(long userId, byte[] imageByte) {
        String name = postPhoto(imageByte);
        if (name == null) {
            System.err.println("Error in update user avatar");
            return null;
        }
        String request = new String(Server.HOST);
        request += Server.USER_UPDATE;
        request += "?type=avatar";
        request += "&userId=";
        request += userId;
        request += "&name=";
        request += name;
        String result = null;
        try {
            result = postJsonResult(new URL(request), "");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        if (result.equals("SUCCESS")) {
            return name;
        }
        return null;
    }


    public static void postPhoto(final byte[] imageBytes, final CallBackListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String photoId = postPhoto(imageBytes);
                if (listener != null) {
                    if (photoId != null) {
                        listener.onFinished(photoId);
                    } else {
                        listener.onError(new RuntimeException("Error in posting photo"));
                    }
                }
            }
        }).start();
    }

    public static String postPhoto(byte[] imageBytes) {
//         String imageBase64 = Base64.encodeBase64URLSafeString(imageBytes);
        String imageBase64 = android.util.Base64.encodeToString(imageBytes, android.util.Base64
                .URL_SAFE);
        String s = Server.HOST + Server.GET_IMAGE;
        URL url = null;
        try {
            url = new URL(s);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        String result = PostRequest.postJsonResult(url, imageBase64);
        return result;
    }

    public static void postMsg(final Msg msg, final CallBackListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean sendSuccess = postMsg(msg);
                if (listener != null) {
                    if (sendSuccess) {
                        listener.onFinished("SUCCESS");
                    } else {
                        listener.onError(new RuntimeException("Error in sending message"));
                    }
                }
            }
        }).start();
    }

    public static boolean postMsg(Msg msg) {
        String request = new String(Server.HOST);
        request += Server.CHAT;
        String msgJson = new Gson().toJson(msg);
        String result = null;
        try {
            result = postJsonResult(new URL(request), msgJson);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        if (!result.equals("SUCCESS")) {
            return false;
        }
        return true;
    }

    private static String postJsonResult(URL url, String content) {
        if (content == null) {
            throw new InvalidParameterException("Content cannot be null");
        }
        HttpURLConnection connection = null;

        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setConnectTimeout(3000);
            connection.setReadTimeout(3000);
            connection.setDoOutput(true);
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("Content-Type", "multipart/form-data");
            connection.connect();

            DataOutputStream out = new DataOutputStream(connection.getOutputStream());
            out.writeBytes(content);
            out.flush();

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection
                                                                                     .getInputStream()));
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            return result.toString();
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
