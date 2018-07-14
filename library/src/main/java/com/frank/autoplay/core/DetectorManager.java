package com.frank.autoplay.core;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by frank on 2018/3/9.
 */

class DetectorManager {

    private List<Detector> mDetectors = new ArrayList<>();
    private Map<Detector, Detector.State> mStates = new HashMap<>();

    void start() {
        for (Detector detector : mDetectors) {
            final Detector.State savedState = mStates.get(detector);
            if (savedState == Detector.State.RESUMED) {
                start(detector);
            }
        }
    }

    void resume() {
        for (Detector detector : mDetectors) {
            final Detector.State savedState = mStates.get(detector);
            if (savedState == Detector.State.RESUMED) {
                resume(detector);
            }
        }
    }

    void pause() {
        mStates.clear();
        for (Detector detector : mDetectors) {
            final Detector.State state = detector.getState();
            mStates.put(detector, state);
            pause(detector);
        }
    }

    void stop() {
        mStates.clear();
        for (Detector detector : mDetectors) {
            final Detector.State state = detector.getState();
            mStates.put(detector, state);
            stop(detector);
        }
    }

    void release() {
        for (Detector detector : mDetectors) {
            stop(detector);
            release(detector);
        }
        mDetectors.clear();
    }

    void start(ViewGroup viewGroup) {
        Detector detector = find(viewGroup);
        if (detector == null) {
            detector = add(viewGroup);
        }
        start(detector);
    }

    void stop(ViewGroup viewGroup) {
        Detector detector = find(viewGroup);
        if (detector != null) {
            stop(detector);
        }
    }

    private void start(Detector detector) {
        if (canStart(detector)) {
            detector.start();
        }
    }

    private void resume(Detector detector) {
        if (canResume(detector)) {
            detector.resume();
        }
    }

    private void pause(Detector detector) {
        if (canPause(detector)) {
            detector.pause();
        }
    }

    private void stop(Detector detector) {
        if (canStop(detector)) {
            detector.stop();
        }
    }

    private void release(Detector detector) {
        if (canRelease(detector)) {
            detector.release();
        }
    }

    @NonNull
    private Detector add(ViewGroup viewGroup) {
        Detector detector = DetectorFactory.create(viewGroup);
        mDetectors.add(detector);
        return detector;
    }

    @Nullable
    private Detector find(ViewGroup viewGroup) {
        for (Detector detector : mDetectors) {
            if (detector.getTargetView() == viewGroup) {
                return detector;
            }
        }
        return null;
    }

    private boolean canStart(Detector<?> detector) {
        return detector.getState() == Detector.State.INITIALIZED || detector.getState() == Detector.State.STOPPED;
    }

    private boolean canResume(Detector<?> detector) {
        return detector.getState() == Detector.State.PAUSED;
    }

    private boolean canPause(Detector<?> detector) {
        return detector.getState() == Detector.State.RESUMED;
    }

    private boolean canStop(Detector<?> detector) {
        final Detector.State state = detector.getState();
        switch (state) {
            case INITIALIZED:
            case STARTED:
            case RESUMED:
            case PAUSED:
                return true;
            case STOPPED:
            case RELEASED:
            default:
                return false;
        }
    }

    private boolean canRelease(Detector<?> detector) {
        return detector.getState() != Detector.State.RELEASED;
    }
}
