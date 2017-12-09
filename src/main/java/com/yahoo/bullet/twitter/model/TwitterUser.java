package com.yahoo.bullet.twitter.model;

import twitter4j.User;

/**
 * Model for a Twitter {@code User}.
 */
public class TwitterUser {

    public TwitterUser(User user) {
        if (user == null) {
            return;
        }

        this.id = user.getId();
        this.name = user.getName();
        this.screenName = user.getScreenName();

        this.location = user.getLocation();
        this.description = user.getDescription();

        this.contributorsEnabled = user.isContributorsEnabled();
        this.defaultProfile = user.isDefaultProfile();
        this.defaultProfileImage = user.isDefaultProfileImage();
        this.profileBackgroundImage = user.isProfileUseBackgroundImage();
        this.profileBackgroundTiled = user.isProfileBackgroundTiled();
        this.geoEnabled = user.isGeoEnabled();
        this.verifiedCelebrity = user.isVerified();
        this.translator = user.isTranslator();
        this.showInlineMedia = user.isShowAllInlineMedia();
        this.userProtected = user.isProtected();

        this.followersCount = user.getFollowersCount();
        this.friendsCount = user.getFriendsCount();

        this.createdAt = user.getCreatedAt().getTime();
        this.favoritesCount = user.getFavouritesCount();
        this.statusCount = user.getStatusesCount();
        this.listedCount = user.getListedCount();

        this.profileBackgroundColor = user.getProfileBackgroundColor();
        this.profileTextColor = user.getProfileTextColor();
        this.profileLinkColor = user.getProfileLinkColor();
        this.profileSidebarFillColor = user.getProfileSidebarFillColor();
        this.profileSidebarBorderColor = user.getProfileSidebarBorderColor();

        this.timeZone = user.getTimeZone();
        this.language = user.getLang();
    }

    @RecordEntry("id")
    private long id;
    @RecordEntry("name")
    private String name;
    @RecordEntry("screen_name")
    private String screenName;

    @RecordEntry("location")
    private String location;
    @RecordEntry("description")
    private String description;

    @RecordEntry("is_contributors_enabled")
    private boolean contributorsEnabled;
    @RecordEntry("is_default_profile")
    private boolean defaultProfile;
    @RecordEntry("is_default_profile_image")
    private boolean defaultProfileImage;
    @RecordEntry("is_profile_use_background_image")
    private boolean profileBackgroundImage;
    @RecordEntry("is_profile_background_tiled")
    private boolean profileBackgroundTiled;
    @RecordEntry("is_geo_location_enabled")
    private boolean geoEnabled;
    @RecordEntry("is_verified")
    private boolean verifiedCelebrity;
    @RecordEntry("is_translator")
    private boolean translator;
    @RecordEntry("is_show_all_inline_media")
    private boolean showInlineMedia;
    @RecordEntry("is_protected")
    private boolean userProtected;

    @RecordEntry("followers_count")
    private long followersCount;
    @RecordEntry("friends_count")
    private long friendsCount;

    @RecordEntry("created_at")
    private long createdAt;
    @RecordEntry("favorites_count")
    private long favoritesCount;
    @RecordEntry("statuses_count")
    private long statusCount;
    @RecordEntry("listed_count")
    private long listedCount;

    @RecordEntry("profile_background_color")
    private String profileBackgroundColor;
    @RecordEntry("profile_text_color")
    private String profileTextColor;
    @RecordEntry("profile_link_color")
    private String profileLinkColor;
    @RecordEntry("profile_sidebar_fill_color")
    private String profileSidebarFillColor;
    @RecordEntry("profile_sidebar_border_color")
    private String profileSidebarBorderColor;

    @RecordEntry("time_zone")
    private String timeZone;
    @RecordEntry("language")
    private String language;

}
