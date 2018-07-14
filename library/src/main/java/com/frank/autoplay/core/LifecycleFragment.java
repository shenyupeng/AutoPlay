package com.frank.autoplay.core;

import android.app.Fragment;
import android.view.ViewGroup;

/**
 * Created by frank on 2018/3/9.
 */

public class LifecycleFragment extends Fragment {

    public static final String TAG = "LifecycleFragment";

    private DetectorManager mDetectorManager = new DetectorManager();

    private boolean mKeep;

    public LifecycleFragment() {
    }

    public void startDetect(ViewGroup viewGroup) {
        mDetectorManager.start(viewGroup);
    }

    public void stopDetect(ViewGroup viewGroup) {
        mDetectorManager.stop(viewGroup);
    }

    public void setKeep(boolean keep) {
        this.mKeep = keep;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mKeep) {
            mDetectorManager.resume();
        } else {
            mDetectorManager.start();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mKeep) {
            mDetectorManager.pause();
        } else {
            mDetectorManager.stop();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDetectorManager.release();
        mDetectorManager = null;
    }
}

