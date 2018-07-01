package com.frank.autoplay.core;

import android.app.Fragment;
import android.view.ViewGroup;

/**
 * Created by frank on 2018/3/9.
 */

public class LifecycleFragment extends Fragment {

    public static final String TAG = "LifecycleFragment";

    private DetectorManager mDetectorManager = new DetectorManager();

    public LifecycleFragment() {
    }

    public void setKeep(boolean isKeep) {
        mDetectorManager.setKeep(isKeep);
    }

    public void startDetect(ViewGroup viewGroup) {
        mDetectorManager.start(viewGroup);
    }

    public void stopDetect(ViewGroup viewGroup) {
        mDetectorManager.stop(viewGroup);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mDetectorManager.isKeep()) {
            mDetectorManager.resumeAll();
        } else {
            mDetectorManager.startAll();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mDetectorManager.isKeep()) {
            mDetectorManager.pauseAll();
        } else {
            mDetectorManager.stopAll();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDetectorManager.release();
        mDetectorManager = null;
    }
}

