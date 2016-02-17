package edu.cmu.ece.jsphdev.foodie.model;

import java.security.InvalidParameterException;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * the class represent the comment from a client
 */
public class ClientComment extends AbstractComment {

    public ClientComment(User creator, float rating, String commentWord, List<String> photoList) {
        if (creator == null) {
            throw new InvalidParameterException("Creator cannot be null");
        }

        if ((rating > 5) || (rating < 0)) {
            throw new InvalidParameterException("Rating must be in range from 0 to 5");
        }

        if ((commentWord == null) || (commentWord.length() == 0)) {
            throw new InvalidParameterException("InValid comment word");
        }

        if (photoList == null) {
            throw new InvalidParameterException(
                    "PhotoList cannot be null. If no picture is " + "upload, an empty list should be used");
        }

        if (photoList.size() > 10) {
            throw new InvalidParameterException("A comment can contains at most 10 photos");
        }

        this.commentID = generateCommentId();
        this.creator = creator;
        this.rating = rating;
        this.commentWord = commentWord;
        this.date = new Date();
        this.photoList = photoList;
    }

    private long generateCommentId() {
        long realTime = new Date().getTime();
        int random = new Random().nextInt(50000);
        return realTime + random;
    }
}
