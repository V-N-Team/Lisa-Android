package ht.lisa.app;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;

import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;

import ht.lisa.app.util.SharedPreferencesUtil;

public class LisaSoundTest {
    public Context instrumentationContext;

    @Before
    public void setup() {
        instrumentationContext = InstrumentationRegistry.getInstrumentation().getContext();

    }

    @Test
    public void testSound() {
        try {
            MediaPlayer mediaPlayer = MediaPlayer.create(instrumentationContext, ht.lisa.app.R.raw.tagline_lisa);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_NOTIFICATION);
            mediaPlayer.start();
            SharedPreferencesUtil.setLisaSoundNeeded(false);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
