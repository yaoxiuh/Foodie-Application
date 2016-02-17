package model;

import java.util.Date;
import java.util.List;

/**
 * @author Guangyu Chen
 * @version 1.2
 */
public interface Comment {

    public long getCommentID();

    public User getCreator();

    public float getRating();

    public String getCommentWord();

    public Date getDate();

    public List<String> getPhotoList();

}
