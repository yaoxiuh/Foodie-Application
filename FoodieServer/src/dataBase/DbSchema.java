package dataBase;

/**
 * Class describing database schema, including database table name and content
 * 
 * @author Guangyu Chen
 *
 */
public class DbSchema {

    private static final String dbPath = "/Users/guangyu/Documents/18641/18641_team/Github/FoodieServer/DataBase/";
    private static final String imagePath = "/Users/guangyu/Documents/java/.metadata/.plugins/org.eclipse.wst.server.core/tmp1/wtpwebapps/ROOT/";

    /**
     * restaurant table
     *
     */
    public static class Rst {
        public static String TABLE_NAME = "restaurant";
        public static String ID_COLUMN = "id";
        public static String NAME_COLUMN = "name";;
        public static String ADDR_COLUMN = "address";
        public static String PHONE_COLUMN = "phonenumber";
        public static String PRICE_COLUMN = "price";
        public static String AVGRATING_COLUMN = "averagerating";
        public static String RATETIMES_COLUMN = "ratetimes";
        public static String LONGITUDE_COLUMN = "longitude";
        public static String LATITUDE_COLUMN = "latitude";
        public static String COVERURL_COLUMN = "coverphotourl";
        public static String TAGS_COLUMN = "tags";
    }

    public static class Comment {
        public static String TABLE_NAME = "comment";
        public static String ID_COLUMN = "id";
        public static String RATING_COLUMN = "rating";
        public static String COMMENTWORD_COLUMN = "commentword";
        public static String DATE_COLUMN = "date";
        public static String RST_FK_COLUMN = "restaurant_id";
        public static String USER_FK_COLUMN = "user_id";
    }

    /**
     * comment photo table
     *
     */
    public static class CommentPhotos {
        public static String TABLE_NAME = "commentphotos";
        public static String ID_COLUMN = "id";
        public static String COMMENT_FK_COLUMN = "comment_id";
    }

    /**
     * User table
     *
     */
    public static class User {
        public static String TABLE_NAME = "user";
        public static String ID_COLUMN = "id";
        public static String NAME_COLUMN = "name";
        public static String PASSWORD_COLUMN = "password";
        public static String LONGITUDE_COLUMN = "longitude";
        public static String LATITUDE_COLUMN = "latitude";
        public static String LOCATIONEXPIRE_COLUMN = "locationexpire";
        public static String JOININDATE_COLUMN = "joinindate";
        public static String AVATARURL_COLUMN = "avatarurl";
        public static String COVERPHOTOURL_COLUMN = "coverphotourl";
    }

    /**
     * Message table
     *
     */
    public static class Message {
        public static String TABLE_NAME = "message";
        public static String ID_COLUMN = "id";
        public static String SENDER_COLUMN = "sender";
        public static String RECEIVER_COLUMN = "receiver";
        public static String CONTENT_COLUMN = "content";
        public static String TIME_COLUMN = "time";
    }

    /**
     * get database path
     * 
     * @return database path
     */
    public static String getDBPath() {
        return dbPath;
    }

    /**
     * get image path
     * 
     * @return image path
     */
    public static String getImagepath() {
        return imagePath;
    }
}
