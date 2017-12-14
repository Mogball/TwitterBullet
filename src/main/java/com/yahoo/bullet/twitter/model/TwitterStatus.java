package com.yahoo.bullet.twitter.model;

import twitter4j.Status;

/**
 * Model for a Twitter {@code Status}.
 */
public class TwitterStatus {

    public TwitterStatus(Status status) {
        if (status.getCreatedAt() != null) {
            this.createdAt = status.getCreatedAt().getTime();
        } else {
            this.createdAt = -1;
        }
        this.statusId = status.getId();

        this.text = status.getText();
        this.replyToStatusId = status.getInReplyToStatusId();
        this.replyToUserId = status.getInReplyToUserId();
        this.replyToScreenName = status.getInReplyToScreenName();

        if (status.getGeoLocation() != null) {
            this.geoLocationLatitude = status.getGeoLocation().getLatitude();
            this.geoLocationLongitude = status.getGeoLocation().getLongitude();
        } else {
            this.geoLocationLatitude = 0.0;
            this.geoLocationLongitude = 0.0;
        }

        this.favorited = status.isFavorited();
        this.retweeted = status.isRetweeted();

        this.favoriteCount = status.getFavoriteCount();
        this.retweetCount = status.getRetweetCount();

        if (status.getRetweetedStatus() != null) {
            this.retweetedStatusId = status.getRetweetedStatus().getId();
        } else {
            this.retweetedStatusId = -1;
        }
        this.quotedStatusId = status.getQuotedStatusId();

        this.language = status.getLang();
    }

    @RecordEntry(value = "created_at", desc = "Timestamp of Tweet creation")
    public final long createdAt;
    @RecordEntry("id")
    public final long statusId;

    @RecordEntry("text")
    public final String text;

    @RecordEntry(value = "in_reply_to_status_id", desc = "ID of the Tweet to which this is a reply")
    public final long replyToStatusId;
    @RecordEntry(value = "in_reply_to_user_id", desc = "ID of the User whose Tweet to which this is a reply")
    public final long replyToUserId;
    @RecordEntry(value = "in_reply_to_screen_name", desc = "Name of the User whose Tweet to which this is a reply")
    public final String replyToScreenName;

    @RecordEntry(value = "location_latitude", desc = "Latitude of the Tweet's location")
    public final double geoLocationLatitude;
    @RecordEntry(value = "location_longitude", desc = "Longitude of the Tweet's location")
    public final double geoLocationLongitude;

    @RecordEntry("is_favorited")
    public final boolean favorited;
    @RecordEntry("is_retweeted")
    public final boolean retweeted;

    @RecordEntry(value = "favorite_count", desc = "The number of times this Tweet has been favorited")
    public final long favoriteCount;
    @RecordEntry(value = "retweet_count", desc = "The number of times this Tweet has been retweeted")
    public final long retweetCount;

    @RecordEntry(value = "retweeted_status_id", desc = "The ID of the Tweet of which this is a Retweet")
    public final long retweetedStatusId;
    @RecordEntry(value = "quoted_status_id", desc = "The ID of the Tweet that is quoted")
    public final long quotedStatusId;

    @RecordEntry(value = "language", desc = "The estimated language of the Tweet")
    public final String language;

}
