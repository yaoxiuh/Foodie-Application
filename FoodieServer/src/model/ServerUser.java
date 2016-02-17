package model;

import java.util.Date;

public class ServerUser extends AbstractUser {

    public ServerUser(String userName, String password, String avatarId, String coverPhotoId, Date joinInDate,
            long userId, Location location) {
        super();
        this.userName = userName;
        this.password = password;
        this.avatarId = avatarId;
        this.coverPhotoId = coverPhotoId;
        this.joinInDate = joinInDate;
        this.userId = userId;
        this.location = location;
    }
}
