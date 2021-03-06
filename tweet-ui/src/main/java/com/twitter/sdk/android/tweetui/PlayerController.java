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

package com.twitter.sdk.android.tweetui;

import android.media.MediaPlayer;
import android.net.Uri;
import android.view.View;

import com.twitter.sdk.android.core.models.MediaEntity;
import com.twitter.sdk.android.tweetui.internal.TweetMediaUtils;
import com.twitter.sdk.android.tweetui.internal.VideoControlView;
import com.twitter.sdk.android.tweetui.internal.VideoView;

import io.fabric.sdk.android.Fabric;

class PlayerController {
    private static final String TAG = "PlayerController";
    final VideoView videoView;
    final VideoControlView videoControlView;

    PlayerController(VideoView videoView, VideoControlView videoControlView) {
        this.videoView = videoView;
        this.videoControlView = videoControlView;
    }

    void prepare(MediaEntity entity) {
        try {
            final boolean looping = TweetMediaUtils.isLooping(entity);
            final String url = TweetMediaUtils.getSupportedVariant(entity).url;
            final Uri uri = Uri.parse(url);

            setUpMediaControl(looping);
            videoView.setVideoURI(uri, looping);
            videoView.requestFocus();
            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                public void onPrepared(MediaPlayer mediaPlayer) {
                    videoView.start();
                }
            });
        } catch (Exception e) {
            Fabric.getLogger().e(TAG, "Error occurred during video playback", e);
        }
    }

    void setUpMediaControl(boolean looping) {
        if (looping) {
            setUpLoopControl();
        } else {
            setUpMediaControl();
        }
    }

    void setUpLoopControl() {
        videoControlView.setVisibility(View.INVISIBLE);
        videoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (videoView.isPlaying()) {
                    videoView.pause();
                } else {
                    videoView.start();
                }
            }
        });
    }

    void setUpMediaControl() {
        videoView.setMediaController(videoControlView);
    }

    void cleanup() {
        videoView.stopPlayback();
    }
}
