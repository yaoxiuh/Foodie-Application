package edu.cmu.ece.jsphdev.foodie.model;

import java.util.Date;
import java.util.List;

/**
 * the interface contains the methods for a comment
 */
public interface Comment {

     long getCommentID();

     User getCreator();

     float getRating();

     String getCommentWord();

     Date getDate();

     List<String> getPhotoList();

}
