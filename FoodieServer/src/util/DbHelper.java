package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import dataBase.DbSchema;
import model.Comment;
import model.Location;
import model.Msg;
import model.Restaurant;
import model.RstFlavorTag;
import model.ServerComment;
import model.ServerUser;
import model.SortType;
import model.User;
import model.Location.LocationType;

public class DbHelper {
    private static final String INSERT = "insert into";
    private static final String SELECT_ALL = "select * from";
    private static final String SPACE = " ";
    private static final String USER_NAME = "root";
    private static final String PASSWORD = "";
    private static final String WHERE_ID = "where id =";
    private static final String WHERE = "where";
    private static final String SET = "set";
    private static final String UPDATE_USER = "update" + SPACE + DbSchema.User.TABLE_NAME + SPACE;
    private static final String INSERT_RST = INSERT + SPACE + DbSchema.Rst.TABLE_NAME + SPACE
            + "values(?,?,?,?,?,?,?,?,?,?,?)";
    private static final String INSERT_COMMENT = INSERT + SPACE + DbSchema.Comment.TABLE_NAME + SPACE
            + "values(?,?,?,?,?,?)";
    private static final String INSERT_PHOTO = INSERT + SPACE + DbSchema.CommentPhotos.TABLE_NAME + SPACE
            + "values(?,?)";
    private static final String INSERT_USER = INSERT + SPACE + DbSchema.User.TABLE_NAME + SPACE
            + "values(?,?,?,?,?,?,?,?,?)";
    private static final String INSERT_MSG = INSERT + SPACE + DbSchema.Message.TABLE_NAME + SPACE + "values(?,?,?,?,?)";

    private static final String GET_RST = SELECT_ALL + SPACE + DbSchema.Rst.TABLE_NAME + SPACE + WHERE_ID + SPACE;
    private static final String GET_COMMENT = SELECT_ALL + SPACE + DbSchema.Comment.TABLE_NAME + SPACE
            + "where restaurant_id = ";
    private static final String GET_USER = SELECT_ALL + SPACE + DbSchema.User.TABLE_NAME + SPACE + WHERE_ID + SPACE;
    private static final String GET_PHOTOLIST = SELECT_ALL + SPACE + DbSchema.CommentPhotos.TABLE_NAME + SPACE
            + "where comment_id = ";
    private static final String GET_RST_LIST = SELECT_ALL + SPACE + DbSchema.Rst.TABLE_NAME + SPACE;
    private static final String GET_USER_LIST = SELECT_ALL + SPACE + DbSchema.User.TABLE_NAME + SPACE + WHERE + SPACE
            + DbSchema.User.LOCATIONEXPIRE_COLUMN + " < ";

    private static final String QUERY_USER = SELECT_ALL + SPACE + DbSchema.User.TABLE_NAME + SPACE + WHERE + SPACE;

    private static final String UPDATE_PASSWORD = UPDATE_USER + SET + SPACE + DbSchema.User.PASSWORD_COLUMN;
    private static final String UPDATE_AVATAR = UPDATE_USER + SET + SPACE + DbSchema.User.AVATARURL_COLUMN;

    private Connection connectDatabase() {

        Connection conn = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/foodie", USER_NAME, PASSWORD);
        } catch (ClassNotFoundException e) {
            System.err.println("Error in loading mysql driver");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Error in connecting to database");
            e.printStackTrace();
        }
        return conn;
    }

    public void insertRestaurant(Restaurant rst) throws SQLException {
        Connection conn = connectDatabase();

        PreparedStatement preInsertRestaurant = conn.prepareStatement(INSERT_RST);
        preInsertRestaurant.setLong(1, rst.getId());
        preInsertRestaurant.setString(2, rst.getName());
        preInsertRestaurant.setString(3, rst.getAddress());
        preInsertRestaurant.setString(4, rst.getPhoneNumber());
        preInsertRestaurant.setFloat(5, rst.getPrice());
        preInsertRestaurant.setFloat(6, rst.getAverageRating());
        preInsertRestaurant.setInt(7, rst.getRateTimes());
        preInsertRestaurant.setDouble(8, rst.getLocation().getLongitude());
        preInsertRestaurant.setDouble(9, rst.getLocation().getLatitude());
        preInsertRestaurant.setString(10, rst.getCoverPhotoId());
        preInsertRestaurant.setString(11, rst.getTagList().get(0).toString());
        preInsertRestaurant.executeUpdate();
        preInsertRestaurant.close();

        conn.close();

        for (Comment c : rst.getComments()) {
            insertComment(c, rst.getId());
        }
    }

    public void insertComment(Comment comment, long rstId) {
        Connection conn = connectDatabase();

        PreparedStatement preInsertComment;
        try {
            preInsertComment = conn.prepareStatement(INSERT_COMMENT);

            preInsertComment.setLong(1, comment.getCommentID());
            preInsertComment.setFloat(2, comment.getRating());
            preInsertComment.setString(3, comment.getCommentWord());
            preInsertComment.setLong(4, comment.getDate().getTime());
            preInsertComment.setLong(5, rstId);
            preInsertComment.setLong(6, comment.getCreator().getUserId());
            preInsertComment.executeUpdate();
            preInsertComment.close();

            for (String s : comment.getPhotoList()) {
                PreparedStatement preInsertPhoto = conn.prepareStatement(INSERT_PHOTO);
                preInsertPhoto.setString(1, s);
                preInsertPhoto.setLong(2, comment.getCommentID());
                preInsertPhoto.executeUpdate();
                preInsertPhoto.close();
            }
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertUser(User user) {
        Connection conn = connectDatabase();
        try {
            PreparedStatement preInsertUser = conn.prepareStatement(INSERT_USER);
            preInsertUser.setLong(1, user.getUserId());
            preInsertUser.setString(2, user.getUserName());
            preInsertUser.setString(3, user.getPassword());
            preInsertUser.setDouble(4, user.getLocation().getLongitude());
            preInsertUser.setDouble(5, user.getLocation().getLatitude());
            preInsertUser.setLong(6, user.getLocation().getExpireTime().getTime());
            preInsertUser.setDate(7, new java.sql.Date(user.getJoinInDate().getTime()));
            preInsertUser.setString(8, user.getAvatarId());
            preInsertUser.setString(9, user.getCoverPhotoId());
            preInsertUser.executeUpdate();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertMessage(Msg msg) {
        Connection conn = connectDatabase();
        try {
            PreparedStatement preInsertMsg = conn.prepareStatement(INSERT_MSG);
            preInsertMsg.setLong(1, 0);
            preInsertMsg.setLong(2, msg.getSender().getUserId());
            preInsertMsg.setLong(3, msg.getReceiver().getUserId());
            preInsertMsg.setString(4, msg.getContent());
            preInsertMsg.setLong(5, msg.getTime().getTime());
            preInsertMsg.executeUpdate();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Restaurant getRestaurant(long rstId) throws SQLException {
        Restaurant restaurant = null;
        Connection conn = connectDatabase();
        Statement statement = conn.createStatement();
        ResultSet result = statement.executeQuery(GET_RST + rstId);
        System.out.println("get restaurant");
        result.last();
        int resultNum = result.getRow();
        if (resultNum == 1) {
            String name = result.getString(DbSchema.Rst.NAME_COLUMN);
            String address = result.getString(DbSchema.Rst.ADDR_COLUMN);
            Restaurant.Builder builder = new Restaurant.Builder(name, address);

            builder.addId(result.getLong(DbSchema.Rst.ID_COLUMN));
            builder.addAvgRating(result.getFloat(DbSchema.Rst.AVGRATING_COLUMN));
            builder.addCoverPhotoURL(result.getString(DbSchema.Rst.COVERURL_COLUMN));
            builder.addPhoneNumber(result.getString(DbSchema.Rst.PHONE_COLUMN));
            builder.addPrice(result.getFloat(DbSchema.Rst.PRICE_COLUMN));
            builder.addRateTimes(result.getInt(DbSchema.Rst.RATETIMES_COLUMN));

            double longitude = result.getDouble(DbSchema.Rst.LONGITUDE_COLUMN);
            double latitude = result.getDouble(DbSchema.Rst.LATITUDE_COLUMN);
            builder.addLocation(new Location(longitude, latitude, LocationType.RESTAURANT));

            List<RstFlavorTag> tags = new ArrayList<>();
            tags.add(RstFlavorTag.valueOf(result.getString(DbSchema.Rst.TAGS_COLUMN)));
            builder.addTagList(tags);

            restaurant = builder.build();

            List<Comment> comments = getComments(rstId);
            restaurant.setComments(comments);
        } else {
            System.out.println("No restaurant result for " + rstId);
        }
        result.close();
        statement.close();
        conn.close();
        return restaurant;
    }

    private List<Comment> getComments(long rstId) throws SQLException {
        List<Comment> comments = new ArrayList<>();
        Connection conn = connectDatabase();
        Statement statement = conn.createStatement();
        ResultSet result = statement.executeQuery(GET_COMMENT + rstId);
        while (result.next()) {
            User creator = getUser(result.getLong(DbSchema.Comment.USER_FK_COLUMN));
            float rating = result.getFloat(DbSchema.Comment.RATING_COLUMN);
            String commentWord = result.getString(DbSchema.Comment.COMMENTWORD_COLUMN);
            long commentID = result.getLong(DbSchema.Comment.ID_COLUMN);
            long date = result.getLong(DbSchema.Comment.DATE_COLUMN);
            List<String> photoList = getPhotoList(commentID);
            Comment comment = new ServerComment(commentID, creator, rating, commentWord, new Date(date), photoList);
            comments.add(comment);
        }
        result.close();
        statement.close();
        conn.close();
        return comments;
    }

    public User getUser(long userId) {
        User user = null;
        Connection conn = connectDatabase();
        try {

            Statement statement = conn.createStatement();
            ResultSet result = statement.executeQuery(GET_USER + userId);
            result.last();
            int resultNum = result.getRow();
            if (resultNum == 1) {
                String userName = result.getString(DbSchema.User.NAME_COLUMN);
                // String password =
                // result.getString(DbSchema.User.PASSWORD_COLUMN);
                String password = "N/A";
                String avatarId = result.getString(DbSchema.User.AVATARURL_COLUMN);
                String coverPhotoId = result.getString(DbSchema.User.COVERPHOTOURL_COLUMN);
                Date joinInDate = new Date(result.getDate(DbSchema.User.JOININDATE_COLUMN).getTime());

                double longitude = result.getDouble(DbSchema.User.LONGITUDE_COLUMN);
                double latitude = result.getDouble(DbSchema.User.LATITUDE_COLUMN);
                long expireTime = result.getLong(DbSchema.User.LOCATIONEXPIRE_COLUMN);
                Location location = new Location(longitude, latitude, new Date(expireTime));
                user = new ServerUser(userName, password, avatarId, coverPhotoId, joinInDate, userId, location);
            } else {
                System.out.println("No user result for " + userId);
            }
            result.close();
            statement.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;

    }

    public List<String> getPhotoList(long commentId) throws SQLException {
        List<String> photoList = new ArrayList<>();
        Connection conn = connectDatabase();
        Statement statement = conn.createStatement();
        ResultSet result = statement.executeQuery(GET_PHOTOLIST + commentId);
        while (result.next()) {
            String photoId = result.getString(DbSchema.CommentPhotos.ID_COLUMN);
            photoList.add(photoId);
        }
        result.close();
        statement.close();
        conn.close();
        return photoList;
    }

    public List<Restaurant> getRestaurantList(Location userLocation, SortType sortType, List<RstFlavorTag> tags) {
        Connection conn = connectDatabase();
        // TODO implement location related stuff
        String query = GET_RST_LIST;

        double longitude = userLocation.getLongitude();
        double latitude = userLocation.getLatitude();

        query += "where (longitude-" + longitude + ")^2+(latitude-" + latitude + ")^2 < 10";
        // TODO implement multiple tags
        if (tags.size() != 0) {
            query += " and ";
            query += "tags = '" + tags.get(0) + "'";
        }

        List<Restaurant> rstList = new ArrayList<>();
        Statement statement;
        try {
            statement = conn.createStatement();
            ResultSet result = statement.executeQuery(query);

            while (result.next()) {
                long rstId = result.getLong(DbSchema.Rst.ID_COLUMN);
                Restaurant restaurant = getRestaurant(rstId);
                rstList.add(restaurant);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        switch (sortType) {
        case BY_DISTACNE:
            rstList.sort(new SortByLocation(userLocation));
            break;
        case BY_PRICE:
            rstList.sort(new SortByPrice());
            break;
        case BY_RATE:
            rstList.sort(new SortByRating());
            break;
        }
        return rstList;
    }

    public List<User> getPeopleNearbyList(Location userLocation, long userId) {
        // TODO change it back to real time
        long expireTime = userLocation.getExpireTime().getTime() + 1000 * 3600 * 24 * 10;
        String query = GET_USER_LIST + expireTime;
        Connection conn = connectDatabase();
        List<User> userList = new ArrayList<>();
        try {
            Statement statement = conn.createStatement();
            ResultSet result = statement.executeQuery(query);

            while (result.next()) {
                long id = result.getLong(DbSchema.User.ID_COLUMN);
                if (id != userId) {
                    User user = getUser(id);
                    userList.add(user);
                }
            }
            userList.sort(new SortNearbyUser(userLocation));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userList;
    }

    public boolean isNewUser(String userName) {
        long userId = getUserId(userName);
        if (userId > 0) {
            return false;
        } else {
            return true;
        }
    }

    public long userAuth(String userName, String password) {
        Connection conn = connectDatabase();
        try {
            Statement statement;
            statement = conn.createStatement();
            ResultSet result = statement
                    .executeQuery(QUERY_USER + DbSchema.User.NAME_COLUMN + " = " + "'" + userName + "'");
            result.last();
            int resultNum = result.getRow();
            if (resultNum == 0) {
                System.out.println("auth " + userName + " not exists");
                return -1;
            }
            String passwordTrue = result.getString(DbSchema.User.PASSWORD_COLUMN);
            if (password.equals(passwordTrue)) {
                return Long.valueOf(result.getString(DbSchema.User.ID_COLUMN));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public boolean changePassWord(long userId, String newPassword) {
        if (getUser(userId) == null) {
            return false;
        }
        String sql = UPDATE_PASSWORD + "=" + "'" + newPassword + "'" + SPACE + WHERE_ID + SPACE + userId;
        try {
            Connection conn = connectDatabase();
            Statement statement = conn.createStatement();
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean updateUserAvatar(long userId, String name) {
        if (name == null) {
            return false;
        }
        String sql = UPDATE_AVATAR + "=" + "'" + name + "'" + SPACE + WHERE_ID + SPACE + userId;
        try {
            Connection conn = connectDatabase();
            Statement statement = conn.createStatement();
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private long getUserId(String userName) {
        try {
            Connection conn = connectDatabase();
            Statement statement = conn.createStatement();

            String sql = QUERY_USER + DbSchema.User.NAME_COLUMN + " = " + "'" + userName + "'";
            ResultSet result = statement.executeQuery(sql);
            result.last();
            if (result.getRow() == 1) {
                return Long.valueOf(result.getLong(DbSchema.User.ID_COLUMN));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    private class SortByLocation implements Comparator<Restaurant> {
        private double longitude;
        private double latitude;

        public SortByLocation(Location location) {
            super();
            this.longitude = location.getLongitude();
            this.latitude = location.getLatitude();
        }

        @Override
        public int compare(Restaurant o1, Restaurant o2) {
            double distanceSqrt1 = Math.pow((longitude - o1.getLocation().getLongitude()), 2)
                    + Math.pow((latitude - o1.getLocation().getLatitude()), 2);
            double distanceSqrt2 = Math.pow((longitude - o2.getLocation().getLongitude()), 2)
                    + Math.pow(latitude - o2.getLocation().getLatitude(), 2);
            return Double.compare(distanceSqrt2, distanceSqrt1);
        }
    }

    private class SortByPrice implements Comparator<Restaurant> {

        @Override
        public int compare(Restaurant o1, Restaurant o2) {
            return Float.compare(o1.getPrice(), o2.getPrice());
        }

    }

    private class SortByRating implements Comparator<Restaurant> {

        @Override
        public int compare(Restaurant o1, Restaurant o2) {
            return Float.compare(o2.getAverageRating(), o1.getAverageRating());
        }

    }

    private class SortNearbyUser implements Comparator<User> {
        private Location userLocation;
        private static final double GEO_WEIGHT = 0;
        private static final double TIME_WEIGHT = 1;

        public SortNearbyUser(Location location) {
            this.userLocation = location;
        }

        @Override
        public int compare(User o1, User o2) {
            long timeNow = new Date().getTime();
            double distance1 = getDistance(userLocation, o1.getLocation());
            double distance2 = getDistance(userLocation, o2.getLocation());
            long timeToExpire1 = Math.abs(o1.getLocation().getExpireTime().getTime() - timeNow);
            long timeToExpire2 = Math.abs(o2.getLocation().getExpireTime().getTime() - timeNow);
            double weight1 = GEO_WEIGHT / distance1 + TIME_WEIGHT * timeToExpire1;
            double weight2 = GEO_WEIGHT / distance2 + TIME_WEIGHT * timeToExpire2;
            return Double.compare(weight1, weight2);
        }

        private double getDistance(Location l1, Location l2) {
            double longitude1 = l1.getLongitude();
            double latitude1 = l1.getLongitude();
            double longitude2 = l2.getLongitude();
            double latitude2 = l2.getLongitude();
            return Math.sqrt(Math.pow(longitude1 - longitude2, 2) + Math.pow(latitude1 - latitude2, 2));
        }
    }
}
