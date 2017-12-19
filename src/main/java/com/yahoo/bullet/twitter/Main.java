package com.yahoo.bullet.twitter;

import com.yahoo.bullet.twitter.model.ColumnWriter;
import com.yahoo.bullet.twitter.model.TwitterPlace;
import com.yahoo.bullet.twitter.model.TwitterStatus;
import com.yahoo.bullet.twitter.model.TwitterUser;

import java.io.IOException;

public class Main {

    public static void main(String[] argv) throws IOException {
        ColumnWriter writer = new ColumnWriter();
        writer.writeColumnsFor(TwitterStatus.class, null);
        writer.writeColumnsFor(TwitterPlace.class, "place");
        writer.writeColumnsFor(TwitterUser.class, "user");
        writer.writeColumnsTo(System.out, true);
    }

}
