#! /bin/bash

storm jar twitter-bullet-1.0-SNAPSHOT-jar-with-dependencies.jar \
          com.yahoo.bullet.storm.Topology \
          --bullet-conf bullet_settings.yaml \
          --bullet-spout com.yahoo.bullet.twitter.spout.TwitterSpout \
          --bullet-spout-parallelism 1 \
          --bullet-spout-cpu-load 100.0 \
          --bullet-spout-on-heap-memory-load 128.0 \
          --bullet-spout-off-heap-memory-load 196.0 \
          --bullet-spout-arg <consumer key> \
          --bullet-spout-arg <consumer secret> \
          --bullet-spout-arg <access token> \
          --bullet-spout-arg <access token secret> \
          -c topology.acker.executors=1 \
          -c topology.max.spout.pending=1000 \
          -c topology.backpressure.enable=false
