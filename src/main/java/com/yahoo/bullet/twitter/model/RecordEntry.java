package com.yahoo.bullet.twitter.model;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Marks class fields that should be written
 * to a {@code BulletRecord}.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface RecordEntry {

    /**
     * @return the name of the field
     */
    String value();

}
