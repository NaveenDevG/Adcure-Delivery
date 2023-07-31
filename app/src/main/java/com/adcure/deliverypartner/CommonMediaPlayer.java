package com.adcure.deliverypartner;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;

public class CommonMediaPlayer {
    public static CommonMediaPlayer Instance;
    static MediaPlayer mediaPlayer = new MediaPlayer();

    public static CommonMediaPlayer getMediaPlayerInstance() {
        if (Instance == null) {
            Instance = new CommonMediaPlayer();
        }
        return Instance;
    }

    public void playAudioFile(Context context, Uri sampleAudio) {
        if (mediaPlayer != null) {
            mediaPlayer = MediaPlayer.create(context, sampleAudio);
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
        }
    }

    public void stopAudioFile() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }
}
