package com.twitter.sdk.android.tweetui;

import com.twitter.sdk.android.core.models.MediaEntity;

/**
 * Created by edelgado on 2/23/16.
 */
public interface TweetMediaClickListener {
    void onTweetMediaEntityClicked(MediaEntity entity);
}