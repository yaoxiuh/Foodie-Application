package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Restaurant Class, Builder pattern is used to construct this class
 *
 * @author Guangyu Chen
 * @version 2.0
 */
public class Restaurant implements Serializable {
    private final long id;
    private final String name;
    private final Location location;
    private final String address;
    private final String phoneNumber;
    private final List<RstFlavorTag> tagList;
    private final String coverPhotoId;
    private final float price;
    private float averageRating;
    private int rateTimes;
    private List<Comment> comments;
    private static final long serialVersionUID = -7060210544600464481L;
    // TODO add user customized tag

    public static class Builder {
        // required field
        String name;
        String address;

        // optional field, set them to initial value
        long id = generateID();
        Location location = Default.generateRstDefaultLocation();
        String phoneNumber = Default.generatePhoneNumber();
        List<RstFlavorTag> tagList = Default.getFlavorTagList();
        String coverPhotoID = Default.RST_COVER_PHOTO_ID;
        float price = Default.generatePrice();
        float averageRating = Default.generateAvgRating();
        int rateTimes = Default.generateRateTimes();
        List<Comment> comments = new ArrayList<>();

        public Builder(String name, String address) {
            if ((name == null) || (address == null)) {
                throw new IllegalArgumentException("name or address cannot be null");
            }
            this.name = name;
            this.address = address;
        }

        public Builder addId(long id) {
            this.id = id;
            return this;
        }

        public Builder addLocation(Location location) {
            if (location == null) {
                throw new IllegalArgumentException("location cannot be null");
            }
            this.location = location;
            return this;
        }

        public Builder addPhoneNumber(String phoneNumber) {
            if (phoneNumber == null) {
                throw new IllegalArgumentException("phoneNumber cannot be null");
            }
            this.phoneNumber = phoneNumber;
            return this;
        }

        public Builder addTagList(List<RstFlavorTag> tagList) {
            if ((tagList == null) || (tagList.size() == 0)) {
                throw new IllegalArgumentException("tagList cannot be null or empty");
            }
            this.tagList = tagList;
            return this;
        }

        public Builder addCoverPhotoURL(String coverPhotoId) {
            if (coverPhotoId == null) {
                throw new IllegalArgumentException("coverPhotoId cannot be null");
            }
            this.coverPhotoID = coverPhotoId;
            return this;
        }

        public Builder addPrice(float price) {
            if ((price > 5) || (price < 0)) {
                throw new IllegalArgumentException("price must be in the range of 0 to 5");
            }
            this.price = price;
            return this;
        }

        public Builder addAvgRating(float averageRating) {
            if ((averageRating > 5) || (averageRating < 0)) {
                throw new IllegalArgumentException("averageRating must be in the range of 0 to 5");
            }
            this.averageRating = averageRating;
            return this;
        }

        public Builder addRateTimes(int rateTimes) {
            if (rateTimes < 0) {
                throw new IllegalArgumentException("rateTimes must be greater or equals to zero");
            }
            this.rateTimes = rateTimes;
            return this;
        }

        private long generateID() {
            return new Date().getTime();
        }

        public Restaurant build() {
            return new Restaurant(this);
        }
    }

    private Restaurant(Builder builder) {

        this.id = builder.id;
        this.averageRating = builder.averageRating;
        this.rateTimes = builder.rateTimes;
        this.price = builder.price;
        this.name = builder.name;
        this.address = builder.address;
        this.location = builder.location;
        this.coverPhotoId = builder.coverPhotoID;
        this.phoneNumber = builder.phoneNumber;
        this.tagList = builder.tagList;
        this.comments = builder.comments;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Location getLocation() {
        return location;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public float getPrice() {
        return price;
    }

    public List<RstFlavorTag> getTagList() {
        return tagList;
    }

    public String getCoverPhotoId() {
        return coverPhotoId;
    }

    public float getAverageRating() {
        return averageRating;
    }

    public int getRateTimes() {
        return rateTimes;
    }

    public List<Comment> getComments() {
        return Collections.unmodifiableList(comments);
    }

    public void addComment(Comment newComment) {
        if (newComment == null) {
            throw new IllegalArgumentException("Comment cannot be null");
        }
        comments.add(newComment);
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}