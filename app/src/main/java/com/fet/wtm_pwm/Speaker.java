package com.fet.wtm_pwm;

import android.support.annotation.VisibleForTesting;

import com.google.android.things.pio.PeripheralManager;
import com.google.android.things.pio.Pwm;

import java.io.IOException;

/**
 * Created by Kevin_Chiu on 2018/03/19.
 */

public class Speaker implements AutoCloseable {

    private Pwm mPwm;

    /**
     * Create a Speaker connected to the given PWM pin name
     */
    public Speaker(String pin) throws IOException {
        PeripheralManager pioService = PeripheralManager.getInstance();
        Pwm device = pioService.openPwm(pin);
        try {
            connect(device);
        } catch (IOException|RuntimeException e) {
            try {
                close();
            } catch (IOException|RuntimeException ignored) {
            }
            throw e;
        }
    }

    /**
     * Create a Speaker from a {@link Pwm} device
     */
    @VisibleForTesting
    /*package*/ Speaker(Pwm device) throws IOException {
        connect(device);
    }

    private void connect(Pwm device) throws IOException {
        mPwm = device;
        mPwm.setPwmDutyCycle(50.0); // square wave
    }

    @Override
    public void close() throws IOException {
        if (mPwm != null) {
            try {
                mPwm.close();
            } finally {
                mPwm = null;
            }
        }
    }

    /**
     * Play the specified frequency. Play continues until {@link #stop()} is called.
     *
     * @param frequency the frequency to play in Hz
     * @throws IOException
     * @throws IllegalStateException if the device is closed
     */
    public void play(double frequency) throws IOException, IllegalStateException {
        if (mPwm == null) {
            throw new IllegalStateException("pwm device not opened");
        }
        mPwm.setPwmFrequencyHz(frequency);
        mPwm.setEnabled(true);
    }

    /**
     * Stop a currently playing frequency
     *
     * @throws IOException
     * @throws IllegalStateException if the device is closed
     */
    public void stop() throws IOException, IllegalStateException {
        if (mPwm == null) {
            throw new IllegalStateException("pwm device not opened");
        }
        mPwm.setEnabled(false);
    }
}
