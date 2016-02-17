package model;

import java.util.Date;
import java.util.List;

public class ServerComment extends AbstractComment {
    public ServerComment(long commentID, User creator, float rating, String commentWord, Date date,
            List<String> photoList) {
        this.commentID = commentID;
        this.creator = creator;
        this.rating = rating;
        this.commentWord = commentWord;
        this.date = date;
        this.photoList = photoList;
    }
}
