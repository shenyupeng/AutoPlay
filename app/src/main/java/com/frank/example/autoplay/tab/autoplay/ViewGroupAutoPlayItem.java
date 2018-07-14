package com.frank.example.autoplay.tab.autoplay;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import com.frank.autoplay.AutoPlay;
import com.frank.autoplay.AutoPlayItem;

/**
 * Created by frank on 2018/7/1.
 */

public class ViewGroupAutoPlayItem implements AutoPlayItem {

    private ViewGroup mViewGroup;

    public ViewGroupAutoPlayItem(ViewGroup view) {
        this.mViewGroup = view;
    }

    @Override
    public View getAutoPlayView() {
        return mViewGroup;
    }

    @Override
    public float getActivationRatio() {
        return 1 / 2f;
    }

    @Override
    public float getDeactivationRatio() {
        return 1 / 2f;
    }

    @Override
    public boolean isActivable() {
        return true;
    }

    @Override
    public void deactivate() {
        AutoPlay.stopDetect((Activity) mViewGroup.getContext(), mViewGroup);
    }

    @Override
    public void activate() {
        AutoPlay.startDetect((Activity) mViewGroup.getContext(), mViewGroup);
    }

    @Override
    public void onActivationKeeping() {

    }
}