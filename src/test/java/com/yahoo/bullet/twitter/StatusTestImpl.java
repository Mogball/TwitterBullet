package com.yahoo.bullet.twitter;

import lombok.Data;
import twitter4j.GeoLocation;
import twitter4j.HashtagEntity;
import twitter4j.MediaEntity;
import twitter4j.Place;
import twitter4j.RateLimitStatus;
import twitter4j.Scopes;
import twitter4j.Status;
import twitter4j.SymbolEntity;
import twitter4j.URLEntity;
import twitter4j.User;
import twitter4j.UserMentionEntity;

import java.util.Date;

@Data
public class StatusTestImpl implements Status {

    private Date createdAt;
    private long id;
    private String text;
    private int displayTextRangeStart;
    private int displayTextRangeEnd;
    private String source;
    private long inReplyToStatusId;
    private long inReplyToUserId;
    private String inReplyToScreenName;
    private GeoLocation geoLocation;
    private Place place;
    private int favoriteCount;
    private User user;
    private Status retweetedStatus;
    private String lang;
    private long quotedStatusId;
    private Status quotedStatus;

    private boolean retweet;
    private boolean favorited;
    private boolean truncated;
    private boolean retweeted;

    @Override
    public long[] getContributors() {
        return null;
    }

    @Override
    public int getRetweetCount() {
        return 0;
    }

    @Override
    public boolean isRetweetedByMe() {
        return false;
    }

    @Override
    public long getCurrentUserRetweetId() {
        return 0;
    }

    @Override
    public boolean isPossiblySensitive() {
        return false;
    }

    @Override
    public Scopes getScopes() {
        return null;
    }

    @Override
    public String[] getWithheldInCountries() {
        return new String[0];
    }

    @Override
    public int compareTo(Status o) {
        return 0;
    }

    @Override
    public UserMentionEntity[] getUserMentionEntities() {
        return new UserMentionEntity[0];
    }

    @Override
    public URLEntity[] getURLEntities() {
        return new URLEntity[0];
    }

    @Override
    public HashtagEntity[] getHashtagEntities() {
        return new HashtagEntity[0];
    }

    @Override
    public MediaEntity[] getMediaEntities() {
        return new MediaEntity[0];
    }

    @Override
    public SymbolEntity[] getSymbolEntities() {
        return new SymbolEntity[0];
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
