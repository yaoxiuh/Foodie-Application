package util;

import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import model.ClientComment;
import model.ClientUser;
import model.Comment;
import model.Msg;
import model.User;

public class GsonHelper {
    public Comment restoreComment(String commentJson) {

        Gson gson = new GsonBuilder().registerTypeAdapter(User.class, new UserDeserializer())
                .registerTypeAdapter(Comment.class, new CommentDeserializer()).create();
        Comment comment = gson.fromJson(commentJson, ClientComment.class);
        return comment;
    }

    public User restoreUser(String userJson) {
        Gson gson = new GsonBuilder().registerTypeAdapter(User.class, new UserDeserializer()).create();
        User user = gson.fromJson(userJson, ClientUser.class);
        return user;
    }
    
    public Msg restoreMsg(String msgJson) {
        Gson gson = new GsonBuilder().registerTypeAdapter(User.class, new UserDeserializer()).create();
        Msg msg = gson.fromJson(msgJson, Msg.class);
        return msg;
    }

    public static class UserDeserializer implements JsonDeserializer<User> {

        @Override
        public User deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext)
                throws JsonParseException {
            try {
                Class<?> serverUser = Class.forName("model.ServerUser");
                return jsonDeserializationContext.deserialize(json, serverUser);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            System.out.println("Failed to deserialize user");
            return null;
        }
    }

    public static class CommentDeserializer implements JsonDeserializer<Comment> {

        @Override
        public Comment deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext)
                throws JsonParseException {
            try {
                Class<?> serverComment = Class.forName("model.ServerClient");
                return jsonDeserializationContext.deserialize(json, serverComment);
            } catch (ClassNotFoundException e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
            System.out.println("Failed to deserialize comment");
            return null;
        }
    }
}
