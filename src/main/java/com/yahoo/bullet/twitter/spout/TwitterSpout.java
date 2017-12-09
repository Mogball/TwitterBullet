package com.yahoo.bullet.twitter.spout;

import com.yahoo.bullet.record.BulletRecord;
import com.yahoo.bullet.twitter.exception.TwitterBulletRuntimeException;
import com.yahoo.bullet.twitter.model.RecordWriter;
import com.yahoo.bullet.twitter.model.TwitterPlace;
import com.yahoo.bullet.twitter.model.TwitterStatus;
import com.yahoo.bullet.twitter.model.TwitterUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
public class TwitterSpout extends BaseRichSpout implements StatusListener {

    private final List<String> args;

    private TwitterStream twitterStream;
    private BlockingQueue<BulletRecord> statusQueue;
    private AtomicBoolean active;

    private SpoutOutputCollector outputCollector;

    /**
     * Initialize the {@code TwitterSpout}. The Twitter API OAuth access
     * keys and tokens must be passed through the arguments. The passed arguments
     * should be:
     *
     * <ol>
     *     <li>Twitter OAuth consumer key</li>
     *     <li>Twitter OAuth consumer secret</li>
     *     <li>Twitter OAuth access token</li>
     *     <li>Twitter OAuth access token secret</li>
     * </ol>
     *
     * @param args spout arguments
     */
    public TwitterSpout(List<String> args) {
        this.args = args;
        statusQueue = new ArrayBlockingQueue<>(8192);
        active = new AtomicBoolean(false);
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("record"));
    }

    @Override
    public void activate() {
        log.info("Activating TwitterSpout");
        if (twitterStream == null) {
            initializeFromArgs(args);
        }
        twitterStream.addListener(this);
        twitterStream.sample();
        active.set(true);
    }

    @Override
    public void deactivate() {
        active.set(false);
        if (twitterStream != null) {
            twitterStream.removeListener(this);
            twitterStream.cleanUp();
        } else {
            log.info("Deactivating TwitterSpout with null TwitterStream");
        }
        statusQueue.clear();
    }

    @Override
    public void open(Map map, TopologyContext topologyContext, SpoutOutputCollector spoutOutputCollector) {
        log.info("Opening the TwitterSpout");
        outputCollector = spoutOutputCollector;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void nextTuple() {
        if (outputCollector == null) {
            log.error("Requesting nextTuple for a TwitterSpout with null collector");
            return;
        }
        if (statusQueue.isEmpty()) {
            log.info("No statuses in queue");
        } else {
            List<Object> emitted = new ArrayList<>(statusQueue.size());
            statusQueue.drainTo(emitted);
            long id = 0;
            for (Object emit : emitted) {
                outputCollector.emit(new Values(emit), id++);
            }
        }
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            log.error("Error in sleep", e);
        }
    }

    private void initializeFromArgs(List<String> args) {
        if (args == null || args.size() < 4) {
            log.error("TwitterSpout is missing required arguments.");
            throw new TwitterBulletRuntimeException("Missing required arguments for TwitterSpout");
        }
        log.info("Initializing TwitterStream instance with supplied config");
        ConfigurationBuilder configBuilder = new ConfigurationBuilder();
        configBuilder
                .setOAuthConsumerKey(args.get(0))
                .setOAuthConsumerSecret(args.get(1))
                .setOAuthAccessToken(args.get(2))
                .setOAuthAccessTokenSecret(args.get(3));
        TwitterStreamFactory streamFactory = new TwitterStreamFactory(configBuilder.build());
        twitterStream = streamFactory.getInstance();
        log.info("TwitterStream instance initialized");
    }

    @Override
    public void onStatus(Status status) {
        if (!active.get()) {
            log.info("TwitterSpout is not active; ignoring Status");
            return;
        }
        if (statusQueue.remainingCapacity() == 0) {
            log.info("TwitterSpout is full; dropping oldest Status");
            statusQueue.poll();
        }
        statusQueue.offer(statusToRecord(status));
    }

    @Override
    public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {}

    @Override
    public void onTrackLimitationNotice(int numberOfLimitedStatuses) {}

    @Override
    public void onScrubGeo(long userId, long upToStatusId) {}

    @Override
    public void onStallWarning(StallWarning warning) {}

    @Override
    public void onException(Exception e) {
        log.error("Error in listener", e);
    }

    protected static BulletRecord statusToRecord(Status status) {
        TwitterStatus twitterStatus = new TwitterStatus(status);
        TwitterPlace twitterPlace = new TwitterPlace(status.getPlace());
        TwitterUser twitterUser = new TwitterUser(status.getUser());
        BulletRecord bulletRecord = new BulletRecord();
        RecordWriter recordWriter = new RecordWriter(bulletRecord);
        recordWriter.writeFieldsOf(twitterStatus, null);
        recordWriter.writeFieldsOf(twitterPlace, "place");
        recordWriter.writeFieldsOf(twitterUser, "user");
        return bulletRecord;
    }
}
