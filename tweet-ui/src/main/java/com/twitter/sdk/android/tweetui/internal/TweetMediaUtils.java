/*
 * Copyright (C) 2015 Twitter, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.twitter.sdk.android.tweetui.internal;

import android.os.Build;

import com.twitter.sdk.android.core.models.MediaEntity;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.models.VideoInfo;

import java.util.ArrayList;
import java.util.List;

final public class TweetMediaUtils {
    public static final String PHOTO_TYPE = "photo";
    public static final String VIDEO_TYPE = "video";
    public static final String GIF_TYPE = "animated_gif";
    private static final String CONTENT_TYPE_MP4 = "video/mp4";
    private static final String CONTENT_TYPE_WEBM = "video/webm";

    private TweetMediaUtils() {
    }

    /**
     * This method gets the last photo entity out of the tweet, this is the photo to display inline
     *
     * @param tweet The Tweet
     * @return The last photo entity of Tweet
     */
    static public MediaEntity getPhotoEntity(Tweet tweet) {
        final List<MediaEntity> mediaEntityList = getAllMediaEntities(tweet);
        for (int i = mediaEntityList.size() - 1; i >= 0; i--) {
            final MediaEntity entity = mediaEntityList.get(i);
            if (entity.type != null && isPhotoType(entity)) {
                return entity;
            }
        }
        return null;
    }

    /**
     * Returns true if there is a media entity with the type of "photo"
     *
     * @param tweet The Tweet entities
     * @return true if there is a media entity with the type of "photo"
     */
    static public boolean hasPhoto(Tweet tweet) {
        return getPhotoEntity(tweet) != null;
    }

    /**
     * This method gets the first video or animated gif entity out of the tweet, this is the video
     * to display inline
     *
     * @param tweet The Tweet
     * @return The last photo entity of Tweet
     */
    static public MediaEntity getVideoEntity(Tweet tweet) {
        for (MediaEntity mediaEntity : getAllMediaEntities(tweet)) {
            if (mediaEntity.type != null && isVideoType(mediaEntity)) {
                return mediaEntity;
            }
        }

        return null;
    }

    /**
     * Returns true if there is a media entity with the type of "video" or "animated_gif"
     *
     * @param tweet The Tweet entities
     * @return true if there is a media entity with the type of "video" or "animated_gif"
     */
    static public boolean hasVideo(Tweet tweet) {
        return getVideoEntity(tweet) != null;
    }

    static boolean isPhotoType(MediaEntity mediaEntity) {
        if (PHOTO_TYPE.equals(mediaEntity.type)) {
            return true;
        }

        return false;
    }

    static boolean isVideoType(MediaEntity mediaEntity) {
        if (VIDEO_TYPE.equals(mediaEntity.type) || GIF_TYPE.equals(mediaEntity.type)) {
            return true;
        }

        return false;
    }

    static public VideoInfo.Variant getSupportedVariant(MediaEntity mediaEntity) {
        for (VideoInfo.Variant variant : mediaEntity.videoInfo.variants) {
            if (isVariantSupported(variant)) {
                return variant;
            }
        }

        return null;
    }

    static public boolean isLooping(MediaEntity mediaEntity) {
        if (GIF_TYPE.equals(mediaEntity.type)) {
            return true;
        }

        return false;
    }

    static boolean isVariantSupported(VideoInfo.Variant variant) {
        if (CONTENT_TYPE_MP4.equals(variant.contentType)) {
            return true;
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
                && CONTENT_TYPE_WEBM.equals(variant.contentType)) {
            return true;
        }

        return false;
    }

    static List<MediaEntity> getAllMediaEntities(Tweet tweet) {
        final List<MediaEntity> entities = new ArrayList<>();
        if (tweet.entities != null && tweet.entities.media != null) {
            entities.addAll(tweet.entities.media);
        }

        if (tweet.extendedEtities != null && tweet.extendedEtities.media != null) {
            entities.addAll(tweet.extendedEtities.media);
        }

        return entities;
    }
}
