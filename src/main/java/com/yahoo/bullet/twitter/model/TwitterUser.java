package com.yahoo.bullet.twitter.model;

import twitter4j.User;

/**
 * Model for a Twitter {@code User}.
 */
public class TwitterUser {

    public TwitterUser(User user) {
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

        if (user.getCreatedAt() != null) {
            this.createdAt = user.getCreatedAt().getTime();
        } else {
            this.createdAt = -1;
        }
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
    public final long id;
    @RecordEntry(value = "name", desc = "The proper name of the User")
    public final String name;
    @RecordEntry(value = "screen_name", desc = "The displayed name of the User")
    public final String screenName;

    @RecordEntry(value = "location", desc = "The User's provided location")
    public final String location;
    @RecordEntry("description")
    public final String description;

    @RecordEntry("is_contributors_enabled")
    public final boolean contributorsEnabled;
    @RecordEntry("is_default_profile")
    public final boolean defaultProfile;
    @RecordEntry("is_default_profile_image")
    public final boolean defaultProfileImage;
    @RecordEntry("is_profile_use_background_image")
    public final boolean profileBackgroundImage;
    @RecordEntry("is_profile_background_tiled")
    public final boolean profileBackgroundTiled;
    @RecordEntry("is_geo_location_enabled")
    public final boolean geoEnabled;
    @RecordEntry("is_verified")
    public final boolean verifiedCelebrity;
    @RecordEntry("is_translator")
    public final boolean translator;
    @RecordEntry("is_show_all_inline_media")
    public final boolean showInlineMedia;
    @RecordEntry("is_protected")
    public final boolean userProtected;

    @RecordEntry("followers_count")
    public final long followersCount;
    @RecordEntry("friends_count")
    public final long friendsCount;

    @RecordEntry(value = "created_at", desc = "Timestamp of the User account's creation")
    public final long createdAt;
    @RecordEntry(value = "favorites_count", desc = "Total number of favorites of the User")
    public final long favoritesCount;
    @RecordEntry(value = "statuses_count", desc = "Total number of Tweets made by the User")
    public final long statusCount;
    @RecordEntry(value = "listed_count", desc = "Number of times the User appears in public lists")
    public final long listedCount;

    @RecordEntry("profile_background_color")
    public final String profileBackgroundColor;
    @RecordEntry("profile_text_color")
    public final String profileTextColor;
    @RecordEntry("profile_link_color")
    public final String profileLinkColor;
    @RecordEntry("profile_sidebar_fill_color")
    public final String profileSidebarFillColor;
    @RecordEntry("profile_sidebar_border_color")
    public final String profileSidebarBorderColor;

    @RecordEntry("time_zone")
    public final String timeZone;
    @RecordEntry(value = "language", desc = "User's preferred language")
    public final String language;

}
