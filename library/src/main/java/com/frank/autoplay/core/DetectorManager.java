package com.frank.autoplay.core;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by frank on 2018/3/9.
 */

public class DetectorManager {


    private List<Detector> mDetectors = new ArrayList<>();


    private boolean mKeep;


    public void setKeep(boolean isKeep) {
        this.mKeep = isKeep;
    }

    public boolean isKeep() {
        return mKeep;
    }

    public void start(ViewGroup viewGroup) {
        Detector detector = find(viewGroup);
        if (detector == null) {
            detector = add(viewGroup);
        }
        detector.start();
    }

    public void stop(ViewGroup viewGroup) {
        Detector detector = find(viewGroup);
        if (detector != null) {
            detector.stop();
        }
    }


    public void startAll() {
        for (Detector detector : mDetectors) {
            detector.start();
        }
    }

    public void resumeAll() {
        for (Detector detector : mDetectors) {
            detector.resume();
        }
    }

    public void pauseAll() {
        for (Detector detector : mDetectors) {
            detector.pause();
        }
    }

    public void stopAll() {
        for (Detector detector : mDetectors) {
            detector.stop();
        }
    }

    public void release() {
        for (Detector detector : mDetectors) {
            detector.release();
        }
        mDetectors.clear();
    }


    @NonNull
    private Detector add(ViewGroup viewGroup) {
        Detector detector = DetectorFactory.create(viewGroup);
        if (detector != null) {
            mDetectors.add(detector);
            return detector;
        }
        throw new IllegalArgumentException("Unsupported ViewGroup " + viewGroup.getClass().getName());
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
}
