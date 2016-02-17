package model;

import java.util.Date;

public interface User {
    public String getUserName();

    public String getPassword();

    public String getAvatarId();

    public String getCoverPhotoId();

    public Date getJoinInDate();

    public long getUserId();

    public Location getLocation();
}
