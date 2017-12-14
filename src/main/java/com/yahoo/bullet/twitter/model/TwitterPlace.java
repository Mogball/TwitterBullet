package com.yahoo.bullet.twitter.model;

import twitter4j.Place;

/**
 * Model for status {@code Place}.
 */
public class TwitterPlace {

    public TwitterPlace(Place place) {
        this.id = place.getId();
        this.name = place.getName();
        this.streetAddress = place.getStreetAddress();

        this.countryCode = place.getCountryCode();
        this.country = place.getCountry();

        this.type = place.getPlaceType();
        this.fullName = place.getFullName();
    }

    @RecordEntry("id")
    public final String id;
    @RecordEntry(value = "name", desc = "Default name of the Place")
    public final String name;
    @RecordEntry("street_address")
    public final String streetAddress;

    @RecordEntry("country_code")
    public final String countryCode;
    @RecordEntry("country")
    public final String country;

    @RecordEntry(value = "type", desc = "The type of Place described")
    public final String type;
    @RecordEntry(value = "full_name", desc = "Verbose name of the Tweet Place")
    public final String fullName;

}
