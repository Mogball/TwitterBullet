package com.yahoo.bullet.twitter;

import lombok.Data;
import twitter4j.RateLimitStatus;
import twitter4j.Status;
import twitter4j.URLEntity;
import twitter4j.User;

import java.util.Date;

@Data
public class UserTestImpl implements User {

    private long id;
    private String name;
    private String email;
    private String screenName;
    private String location;
    private String description;
    private String URL;
    private int followersCount;
    private Status status;
    private String profileBackgroundColor;
    private String profileTextColor;
    private String profileLinkColor;
    private String profileSidebarBorderColor;
    private String profileSidebarFillColor;
    private int friendsCount;
    private Date createdAt;
    private int favouritesCount;
    private int utcOffset;
    private String timeZone;
    private int statusesCount;
    private String lang;
    private int listedCount;

    private boolean defaultProfileImage;
    private boolean contributorsEnabled;
    private boolean profileUseBackgroundImage;
    private boolean defaultProfile;
    private boolean showAllInlineMedia;
    private boolean verified;
    private boolean translator;
    private boolean geoEnabled;
    private boolean profileBackgroundTiled;

    @Override
    public boolean isProtected() {
        return false;
    }

    @Override
    public boolean isFollowRequestSent() {
        return false;
    }

    @Override
    public URLEntity[] getDescriptionURLEntities() {
        return new URLEntity[0];
    }

    @Override
    public URLEntity getURLEntity() {
        return null;
    }

    @Override
    public String[] getWithheldInCountries() {
        return new String[0];
    }

    @Override
    public int compareTo(User o) {
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

    @Override
    public String getProfileImageURL() {
        return null;
    }

    @Override
    public String getBiggerProfileImageURL() {
        return null;
    }

    @Override
    public String getMiniProfileImageURL() {
        return null;
    }

    @Override
    public String getOriginalProfileImageURL() {
        return null;
    }

    @Override
    public String getProfileImageURLHttps() {
        return null;
    }

    @Override
    public String getBiggerProfileImageURLHttps() {
        return null;
    }

    @Override
    public String getMiniProfileImageURLHttps() {
        return null;
    }

    @Override
    public String getOriginalProfileImageURLHttps() {
        return null;
    }

    @Override
    public String getProfileBackgroundImageURL() {
        return null;
    }

    @Override
    public String getProfileBackgroundImageUrlHttps() {
        return null;
    }

    @Override
    public String getProfileBannerURL() {
        return null;
    }

    @Override
    public String getProfileBannerRetinaURL() {
        return null;
    }

    @Override
    public String getProfileBannerIPadURL() {
        return null;
    }

    @Override
    public String getProfileBannerIPadRetinaURL() {
        return null;
    }

    @Override
    public String getProfileBannerMobileURL() {
        return null;
    }

    @Override
    public String getProfileBannerMobileRetinaURL() {
        return null;
    }

}
