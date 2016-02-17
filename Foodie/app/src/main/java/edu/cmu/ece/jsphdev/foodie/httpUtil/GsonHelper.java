package edu.cmu.ece.jsphdev.foodie.httpUtil;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import edu.cmu.ece.jsphdev.foodie.model.Comment;
import edu.cmu.ece.jsphdev.foodie.model.User;

/**
 * Created by guangyu on 12/2/15.
 */
public class GsonHelper {
    public static class UserDeserializer implements JsonDeserializer<User> {

        @Override
        public User deserialize(JsonElement json, Type type, JsonDeserializationContext
                jsonDeserializationContext) throws JsonParseException {
            try {
                Class clientUser = Class.forName("edu.cmu.ece.jsphdev.foodie.model.ClientUser");
                return jsonDeserializationContext.deserialize(json, clientUser);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            System.out.println("Failed to deserialize user");
            return null;
        }
    }

    public static class CommentDeserializer implements JsonDeserializer<Comment> {

        @Override
        public Comment deserialize(JsonElement json, Type type, JsonDeserializationContext
                jsonDeserializationContext) throws JsonParseException {
            try {
                Class clientComment = Class.forName("edu.cmu.ece.jsphdev.foodie.model" +
                                                            ".ClientComment");
                return jsonDeserializationContext.deserialize(json, clientComment);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            System.out.println("Failed to deserialize comment");
            return null;
        }
    }
}
