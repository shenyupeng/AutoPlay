package com.frank.autoplay.core;

import android.support.v4.view.ViewPager;

/**
 * Created by frank on 2018/3/12.
 */

public class ViewPagerDetector extends ExclusiveDetector<ViewPager> {

    private DetectListener mDetectListener = new DetectListener();

    private DirectionCalculator<ViewPager> mDirectionCalculator;

    ViewPagerDetector(ViewPager target) {
        super(target);
    }

    @Override
    void bindDetectListener() {
        ViewPager viewPager = getTargetView();
        viewPager.removeOnPageChangeListener(mDetectListener);
        viewPager.addOnPageChangeListener(mDetectListener);
    }

    @Override
    void unbindDetectListener() {
        ViewPager viewPager = getTargetView();
        viewPager.removeOnPageChangeListener(mDetectListener);
    }

    @Override
    DirectionCalculator<ViewPager> getDirectionCalculator() {
        if (mDirectionCalculator == null) {
            mDirectionCalculator = new ViewPagerDirectionCalculator(getTargetView());
        }
        return mDirectionCalculator;
    }

    @Override
    void onDetecting() {
        super.onDetecting();
    }

    class DetectListener extends ViewPager.SimpleOnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            detect();
        }
    }
}
