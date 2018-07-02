package com.frank.autoplay.core;

import android.view.ViewGroup;

import com.frank.autoplay.utils.L;

import java.lang.ref.WeakReference;

/**
 * Created by frank on 2018/3/9.
 */

public abstract class Detector<TargetView extends ViewGroup> {

    private WeakReference<TargetView> mTargetViewRef;

    private State mState;

    /*package*/ Detector(TargetView target) {
        mTargetViewRef = new WeakReference<>(target);
    }

    /*package*/ TargetView getTargetView() {
        return mTargetViewRef.get();
    }

    State getState() {
        return mState;
    }

    void setState(State state) {
        this.mState = state;

    }

    void detect() {
        if (canDetect()) {
            long start = System.currentTimeMillis();
            onDetecting();
            L.i(this, "detect time:" + (System.currentTimeMillis() - start));
        }
    }

    private boolean canDetect() {
        final TargetView targetView = getTargetView();
        final boolean detectableState = mState == State.STARTED;
        final boolean detectableTarget = targetView != null && targetView.getChildCount() > 0;
        return detectableState && detectableTarget;
    }

    public abstract void start();

    public abstract void resume();

    public abstract void pause();

    public abstract void stop();

    public abstract void release();

    abstract void onDetecting();

    enum State {
        INITIALIZED,
        STARTED,
        PAUSED,
        STOPPED,
        RELEASED
    }

}
