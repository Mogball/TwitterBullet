package com.yahoo.bullet.twitter;

import lombok.Data;
import twitter4j.GeoLocation;
import twitter4j.Place;
import twitter4j.RateLimitStatus;

@Data
public class PlaceTestImpl implements Place {

    private String name;
    private String streetAddress;
    private String countryCode;
    private String id;
    private String country;
    private String placeType;
    private String URL;
    private String fullName;
    private String boundingBoxType;
    private String geometryType;

    @Override
    public GeoLocation[][] getBoundingBoxCoordinates() {
        return null;
    }

    @Override
    public GeoLocation[][] getGeometryCoordinates() {
        return null;
    }

    @Override
    public Place[] getContainedWithIn() {
        return null;
    }

    @Override
    public int compareTo(Place o) {
        return 0;
    }

    @Override
    public RateLimitStatus getRateLimitStatus() {
        return null;
    }

    @Override
    public int getAccessLevel() {
        return 0;
    }

}
