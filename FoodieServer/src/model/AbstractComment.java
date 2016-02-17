package model;

import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * abstract comment class
 * @author Guangyu Chen
 *
 */
public abstract class AbstractComment implements Comment {
    protected long commentID;
    protected User creator;
    protected float rating;
    protected String commentWord;
    protected Date date;
    protected List<String> photoList;

    public long getCommentID() {
        return commentID;
    }

    public User getCreator() {
        return creator;
    }

    public float getRating() {
        return rating;
    }

    public String getCommentWord() {
        return commentWord;
    }

    public Date getDate() {
        return date;
    }

    public List<String> getPhotoList() {
        return Collections.unmodifiableList(photoList);
    }
}
