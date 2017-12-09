# Twitter Bullet

[Bullet](https://yahoo.github.io/bullet-docs/) topology for Twitter streaming API.

### Configuring Bullet

You can quickly setup Bullet by following the [Quick-Start](https://yahoo.github.io/bullet-docs/quick-start/).

### Setup and Use

Clone the repo `git clone https://github.com/Mogball/TwitterBullet.git` 
and build the jar with `mvn package`. Alternatively, use the provided
prebuilt jar. Then run `launch.sh` or simple use

```bash
storm jar twitter-bullet-1.0-SNAPSHOT-jar-with-dependencies.jar \
          com.yahoo.bullet.storm.Topology \
          --bullet-conf bullet_settings.yaml \
          --bullet-spout com.yahoo.bullet.twitter.spout.TwitterSpout \
          --bullet-spout-arg <consumer key> \
          --bullet-spout-arg <consumer secret> \
          --bullet-spout-arg <access token> \
          --bullet-spout-arg <access token secret>
```

Insert your consumer key, secret, access token, and token secret. You can serve or merge
`twitter_columns.json` through your Bullet webservice instead to make queries from the Bullet UI.
