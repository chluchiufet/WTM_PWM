package com.fet.wtm_pwm;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;

public class MainActivity extends Activity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private static final String PWM_BUS = "PWM1";
    private Speaker mSpeaker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupSpeaker();
        try {
            mSpeaker.play(/* G4 */ 391.995);
        } catch (IOException e) {
            Log.e(TAG, "Error playing note", e);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        destroySpeaker();
    }

    private void setupSpeaker() {
        try {
            mSpeaker = new Speaker(PWM_BUS);
            mSpeaker.stop(); // in case the PWM pin was enabled already
        } catch (IOException e) {
            Log.e(TAG, "Error initializing speaker");
        }
    }

    private void destroySpeaker() {
        if (mSpeaker != null) {
            try {
                mSpeaker.stop();
                mSpeaker.close();
            } catch (IOException e) {
                Log.e(TAG, "Error closing speaker", e);
            } finally {
                mSpeaker = null;
            }
        }
    }
}
