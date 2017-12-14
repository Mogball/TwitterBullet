package com.yahoo.bullet.twitter.spout;

import com.yahoo.bullet.record.BulletRecord;
import com.yahoo.bullet.storm.BulletStormConfig;
import com.yahoo.bullet.twitter.PlaceTestImpl;
import com.yahoo.bullet.twitter.StatusTestImpl;
import com.yahoo.bullet.twitter.UserTestImpl;
import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.testng.annotations.Test;
import twitter4j.GeoLocation;
import twitter4j.TwitterStream;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertTrue;

public class TwitterSpoutTest {

    private static class RecordAsserter {
        private final BulletRecord record;
        private String prefix;

        private RecordAsserter(BulletRecord record) {
            this.record = record;
            this.prefix = null;
        }

        private void eq(String propName, Object val) {
            String name = prefix != null && !prefix.isEmpty() ? prefix + "_" + propName : propName;
            assertEquals(this.record.get(name), val);
        }

        private void prefix(String prefix) {
            this.prefix = prefix;
        }
    }

    @Test
    public void testStatusToRecord() {
        PlaceTestImpl place = new PlaceTestImpl();
        UserTestImpl user = new UserTestImpl();
        StatusTestImpl status = new StatusTestImpl();

        status.setPlace(place);
        status.setUser(user);

        Date now = new Date();
        long id = 12345;
        StatusTestImpl reStatus = new StatusTestImpl();
        long reId = 54312;
        reStatus.setId(reId);
        long userId = 555678;

        status.setCreatedAt(now);
        status.setId(id);
        status.setText("Twitter Message text");
        status.setGeoLocation(new GeoLocation(12.0, 13.0));
        status.setFavorited(true);
        status.setRetweeted(true);
        status.setRetweetedStatus(reStatus);
        status.setLang("en");

        place.setId("place_id");
        place.setName("place_name");
        place.setStreetAddress("place_street_address");
        place.setCountryCode("place_country_code");
        place.setCountry("place_country");
        place.setPlaceType("place_type");
        place.setFullName("place_full_name");

        user.setId(userId);
        user.setName("user_name");
        user.setScreenName("user_screen_name");
        user.setLocation("user_location");
        user.setDescription("user_desc");
        user.setContributorsEnabled(true);
        user.setDefaultProfile(true);
        user.setFollowersCount(6666);
        user.setFriendsCount(7777);
        user.setCreatedAt(now);
        user.setFavouritesCount(111);
        user.setStatusesCount(11);
        user.setListedCount(1);
        user.setTimeZone("UTC");
        user.setLang("en");

        BulletRecord record = TwitterSpout.statusToRecord(status);
        RecordAsserter ra = new RecordAsserter(record);
        ra.eq("created_at", now.getTime());
        ra.eq("id", id);
        ra.eq("text", "Twitter Message text");
        ra.eq("location_latitude", 12.0);
        ra.eq("location_longitude", 13.0);
        ra.eq("is_favorited", true);
        ra.eq("is_retweeted", true);
        ra.eq("retweeted_status_id", reId);
        ra.eq("language", "en");

        ra.prefix("user");
        ra.eq("id", userId);
        ra.eq("name", "user_name");
        ra.eq("screen_name", "user_screen_name");
        ra.eq("location", "user_location");
        ra.eq("description", "user_desc");
        ra.eq("is_contributors_enabled", true);
        ra.eq("is_default_profile", true);
        ra.eq("followers_count", 6666L);
        ra.eq("friends_count", 7777L);
        ra.eq("created_at", now.getTime());
        ra.eq("favorites_count", 111L);
        ra.eq("statuses_count", 11L);
        ra.eq("listed_count", 1L);
        ra.eq("time_zone", "UTC");
        ra.eq("language", "en");

        ra.prefix("place");
        ra.eq("id", "place_id");
        ra.eq("name", "place_name");
        ra.eq("street_address", "place_street_address");
        ra.eq("country_code", "place_country_code");
        ra.eq("type", "place_type");
        ra.eq("full_name", "place_full_name");
    }

    @Test
    public void testNoopListenEvents() throws NoSuchFieldException, IllegalAccessException {
        TwitterSpout spout = new TwitterSpout(null);
        Field queueField = TwitterSpout.class.getDeclaredField("statusQueue");
        queueField.setAccessible(true);
        BlockingQueue statusQueue = (BlockingQueue) queueField.get(spout);
        assertEquals(statusQueue.size(), 0);
        spout.onDeletionNotice(null);
        assertEquals(statusQueue.size(), 0);
        spout.onTrackLimitationNotice(0);
        assertEquals(statusQueue.size(), 0);
        spout.onScrubGeo(0, 0);
        assertEquals(statusQueue.size(), 0);
        spout.onStallWarning(null);
        assertEquals(statusQueue.size(), 0);
        spout.onException(new Exception("error"));
        assertEquals(statusQueue.size(), 0);
    }

    @Test
    public void testOnStatus() throws NoSuchFieldException, IllegalAccessException {
        // While inactive
        TwitterSpout spout = new TwitterSpout(null);
        Field queueField = TwitterSpout.class.getDeclaredField("statusQueue");
        queueField.setAccessible(true);
        BlockingQueue statusQueue = (BlockingQueue) queueField.get(spout);
        assertEquals(statusQueue.size(), 0);
        spout.onStatus(null);
        assertEquals(statusQueue.size(), 0);
        // While active and queue is full
        BlockingQueue<BulletRecord> fullQueue = new ArrayBlockingQueue<>(1);
        BulletRecord mockRecord = mock(BulletRecord.class);
        fullQueue.offer(mockRecord);
        assertEquals(fullQueue.remainingCapacity(), 0);
        queueField.set(spout, fullQueue);
        Field activeField = TwitterSpout.class.getDeclaredField("active");
        activeField.setAccessible(true);
        AtomicBoolean truth = new AtomicBoolean();
        truth.set(true);
        activeField.set(spout, truth);
        spout.onStatus(null);
        assertNotEquals(mockRecord, fullQueue.peek());
    }

    @Test
    public void testDeclareOutputFields() {
        OutputFieldsDeclarer declarer = mock(OutputFieldsDeclarer.class);
        TwitterSpout spout = new TwitterSpout(null);
        spout.declareOutputFields(declarer);
        verify(declarer).declare(any());
    }

    @Test
    public void testActivateDeactivate() throws NoSuchFieldException, IllegalAccessException {
        TwitterSpout spout = mock(TwitterSpout.class);
        doCallRealMethod().when(spout).activate();
        doCallRealMethod().when(spout).deactivate();
        Field streamField = TwitterSpout.class.getDeclaredField("twitterStream");
        streamField.setAccessible(true);
        AtomicBoolean active = new AtomicBoolean();
        TwitterStream stream = mock(TwitterStream.class);
        doAnswer(iom -> {
            streamField.set(spout, stream);
            return null;
        }).when(spout).initializeFromArgs(any());
        Field queueField = TwitterSpout.class.getDeclaredField("statusQueue");
        queueField.setAccessible(true);
        BlockingQueue queue = mock(BlockingQueue.class);
        queueField.set(spout, queue);
        Field activeField = TwitterSpout.class.getDeclaredField("active");
        activeField.setAccessible(true);
        activeField.set(spout, active);
        spout.activate();
        assertTrue(active.get());
        verify(spout).initializeFromArgs(any());
        verify(stream).sample();
        verify(queue).clear();
        spout.deactivate();
        verify(stream).cleanUp();
        verify(queue, times(2)).clear();
        assertFalse(active.get());
    }

    @Test
    public void testOpenSetsCollector() throws NoSuchFieldException, IllegalAccessException {
        TwitterSpout spout = new TwitterSpout(null);
        Field collectorField = TwitterSpout.class.getDeclaredField("outputCollector");
        collectorField.setAccessible(true);
        SpoutOutputCollector collector = mock(SpoutOutputCollector.class);
        spout.open(null, null, collector);
        assertEquals(collectorField.get(spout), collector);
    }

    @Test
    public void testNextTuple() throws NoSuchFieldException, IllegalAccessException {
        Field queueField = TwitterSpout.class.getDeclaredField("statusQueue");
        BlockingQueue<BulletRecord> queue = new ArrayBlockingQueue<>(10);
        for (int i = 0; i < 4; i++) {
            queue.offer(new BulletRecord());
        }
        TwitterSpout spout = new TwitterSpout(null);
        queueField.setAccessible(true);
        queueField.set(spout, queue);
        Field collectorField = TwitterSpout.class.getDeclaredField("outputCollector");
        SpoutOutputCollector collector = mock(SpoutOutputCollector.class);
        collectorField.setAccessible(true);
        collectorField.set(spout, collector);
        spout.nextTuple();
        verify(collector, times(4)).emit(anyList(), any());
    }

}
