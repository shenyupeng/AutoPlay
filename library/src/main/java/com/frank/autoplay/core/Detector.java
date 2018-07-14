package com.frank.autoplay.core;

import android.view.ViewGroup;

import java.lang.ref.WeakReference;

/**
 * Created by frank on 2018/3/9.
 */

public abstract class Detector<TargetView extends ViewGroup> {

    private WeakReference<TargetView> mTargetViewRef;

    private State mState;

    Detector(TargetView target) {
        mTargetViewRef = new WeakReference<>(target);
    }

    TargetView getTargetView() {
        return mTargetViewRef.get();
    }

    State getState() {
        return mState;
    }

    void setState(State state) {
        this.mState = state;
    }

    void detect() {
        postDetect();
    }

    private void innerDetect() {
        if (canDetect()) {
            onDetecting();
        }
    }

    void postDetect() {
        getTargetView().removeCallbacks(mDetectEvent);
        getTargetView().postOnAnimation(mDetectEvent);
    }

    private Runnable mDetectEvent = new Runnable() {
        @Override
        public void run() {
            innerDetect();
        }
    };

    private boolean canDetect() {
        final TargetView targetView = getTargetView();
        final boolean detectableState = mState == State.RESUMED;
        final boolean detectableTarget = targetView != null && targetView.getChildCount() > 0;
        return detectableState && detectableTarget;
    }

    public abstract void start();

    public abstract void resume();

    public abstract void pause();

    public abstract void stop();

    public abstract void release();

    /*package*/
    abstract void onDetecting();

    enum State {
        INITIALIZED,
        STARTED,
        RESUMED,
        PAUSED,
        STOPPED,
        RELEASED;
    }

}
