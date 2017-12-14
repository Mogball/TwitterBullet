package com.yahoo.bullet.twitter.model;

import com.yahoo.bullet.twitter.StatusTestImpl;
import com.yahoo.bullet.twitter.UserTestImpl;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class ModelTest {

    @Test
    public void testCreateTwitterStatusWithNull() {
        StatusTestImpl status = new StatusTestImpl();
        status.setCreatedAt(null);
        status.setGeoLocation(null);
        status.setRetweetedStatus(null);
        TwitterStatus tStatus = new TwitterStatus(status);
        assertEquals(tStatus.createdAt, -1);
        assertEquals(tStatus.geoLocationLatitude, 0.0);
        assertEquals(tStatus.geoLocationLongitude, 0.0);
        assertEquals(tStatus.retweetedStatusId, -1);
    }

    @Test
    public void testCreateTwitterUserWithNull() {
        UserTestImpl user = new UserTestImpl();
        user.setCreatedAt(null);
        TwitterUser tUser = new TwitterUser(user);
        assertEquals(tUser.createdAt, -1);
    }

}
