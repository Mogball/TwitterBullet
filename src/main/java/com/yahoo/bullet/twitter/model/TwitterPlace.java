package com.yahoo.bullet.twitter.model;

import twitter4j.Place;

public class TwitterPlace {

    public TwitterPlace(Place place) {
        if (place == null) {
            return;
        }

        this.id = place.getId();
        this.name = place.getName();
        this.streetAddress = place.getStreetAddress();

        this.countryCode = place.getCountryCode();
        this.country = place.getCountry();

        this.type = place.getPlaceType();
        this.fullName = place.getFullName();
    }

    @RecordEntry("id")
    private String id;
    @RecordEntry("name")
    private String name;
    @RecordEntry("street_address")
    private String streetAddress;

    @RecordEntry("country_code")
    private String countryCode;
    @RecordEntry("country")
    private String country;

    @RecordEntry("type")
    private String type;
    @RecordEntry("full_name")
    private String fullName;

}
