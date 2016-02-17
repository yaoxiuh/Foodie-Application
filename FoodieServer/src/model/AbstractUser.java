package model;

import java.util.Date;

/**
 * @author Yaoxiu Hu, Guangyu Chen
 * @version 2.0
 */
public abstract class AbstractUser implements User {

    protected String userName;
    protected String password;
    protected String avatarId;
    protected String coverPhotoId;
    protected Date joinInDate;
    protected long userId;
    protected Location location;

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    /**
     * get the url of user avatar in String form
     *
     * @return url of avatar in String form; default if no avatar is set
     */
    public String getAvatarId() {
        return avatarId;
    }

    public String getCoverPhotoId() {
        return coverPhotoId;
    }

    public Date getJoinInDate() {
        return joinInDate;
    }

    public long getUserId() {
        return userId;
    }

    public Location getLocation() {
        return location;
    }
}
