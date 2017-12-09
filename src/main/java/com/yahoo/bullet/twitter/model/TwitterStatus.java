package com.yahoo.bullet.twitter.model;

import twitter4j.Status;

public class TwitterStatus {

    public TwitterStatus(Status status) {
        if (status == null) {
            return;
        }

        if (status.getCreatedAt() != null) {
            this.createdAt = status.getCreatedAt().getTime();
        }
        this.statusId = status.getId();

        this.text = status.getText();
        this.replyToStatusId = status.getInReplyToStatusId();
        this.replyToUserId = status.getInReplyToUserId();
        this.replyToScreenName = status.getInReplyToScreenName();

        if (status.getGeoLocation() != null) {
            this.geoLocationLatitude = status.getGeoLocation().getLatitude();
            this.geoLocationLongitude = status.getGeoLocation().getLongitude();
        }

        this.favorited = status.isFavorited();
        this.retweeted = status.isRetweeted();

        this.favoriteCount = status.getFavoriteCount();
        this.retweetCount = status.getRetweetCount();

        if (status.getRetweetedStatus() != null) {
            this.retweetedStatusId = status.getRetweetedStatus().getId();
        }
        this.quotedStatusId = status.getQuotedStatusId();

        this.language = status.getLang();
    }

    @RecordEntry("created_at")
    private long createdAt;
    @RecordEntry("id")
    private long statusId;

    @RecordEntry("text")
    private String text;

    @RecordEntry("in_reply_to_status_id")
    private long replyToStatusId;
    @RecordEntry("in_reply_to_user_id")
    private long replyToUserId;
    @RecordEntry("in_reply_to_screen_name")
    private String replyToScreenName;

    @RecordEntry("location_latitude")
    private double geoLocationLatitude;
    @RecordEntry("location_longitude")
    private double geoLocationLongitude;

    @RecordEntry("is_favorited")
    private boolean favorited;
    @RecordEntry("is_retweeted")
    private boolean retweeted;

    @RecordEntry("favorite_count")
    private long favoriteCount;
    @RecordEntry("retweet_count")
    private long retweetCount;

    @RecordEntry("retweeted_status_id")
    private long retweetedStatusId;
    @RecordEntry("quoted_status_id")
    private long quotedStatusId;

    @RecordEntry("language")
    private String language;

}
