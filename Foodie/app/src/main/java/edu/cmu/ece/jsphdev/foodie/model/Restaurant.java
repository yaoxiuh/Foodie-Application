package edu.cmu.ece.jsphdev.foodie.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Restaurant Class, Builder pattern is used to construct this class
 *
 *
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

        /**
         * Add phone to restaurant
         * @param phoneNumber phoneNumber
         * @return builder
         */
        public Builder addPhoneNumber(String phoneNumber) {
            if (phoneNumber == null) {
                throw new IllegalArgumentException("phoneNumber cannot be null");
            }
            this.phoneNumber = phoneNumber;
            return this;
        }

        /**
         * Add tagList to restaurant
         * @param tagList tagList
         * @return builder
         */
        public Builder addTagList(List<RstFlavorTag> tagList) {
            if ((tagList == null) || (tagList.size() == 0)) {
                throw new IllegalArgumentException("tagList cannot be null or empty");
            }
            this.tagList = tagList;
            return this;
        }

        /**
         * Add coverPhotoId to restaurant
         * @param coverPhotoId coverPhotoId
         * @return builder
         */
        public Builder addCoverPhotoURL(String coverPhotoId) {
            if (coverPhotoId == null) {
                throw new IllegalArgumentException("coverPhotoId cannot be null");
            }
            this.coverPhotoID = coverPhotoId;
            return this;
        }

        /**
         * To generate id of a restaurant
         * @return th long id for date
         */
        private long generateID() {
            return new Date().getTime();
        }

        /**
         * Create a restaurant object
         * @return restaurant
         */
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

    /**
     * To get id of the restaurant
     * @return id
     */
    public long getId() {
        return id;
    }


    /**
     * To get Name of the restaurant
     * @return Name
     */
    public String getName() {
        return name;
    }


    /**
     * To get Location of the restaurant
     * @return Location
     */
    public Location getLocation() {
        return location;
    }


    /**
     * To get Address of the restaurant
     * @return Address
     */
    public String getAddress() {
        return address;
    }


    /**
     * To get PhoneNumber of the restaurant
     * @return PhoneNumber
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }


    /**
     * To get price of the restaurant
     * @return price
     */
    public float getPrice() {
        return price;
    }


    /**
     * To get TagList of the restaurant
     * @return TagList
     */
    public List<RstFlavorTag> getTagList() {
        return tagList;
    }


    /**
     * To get CoverPhotoId of the restaurant
     * @return CoverPhotoId
     */
    public String getCoverPhotoId() {
        return coverPhotoId;
    }


    /**
     * To get AverageRating of the restaurant
     * @return AverageRating
     */
    public float getAverageRating() {
        return averageRating;
    }


    /**
     * To get RateTimes of the restaurant
     * @return RateTimes
     */
    public int getRateTimes() {
        return rateTimes;
    }


    /**
     * To get Comment of the restaurant
     * @return Comment
     */
    public List<Comment> getComments() {
        return Collections.unmodifiableList(comments);
    }

}