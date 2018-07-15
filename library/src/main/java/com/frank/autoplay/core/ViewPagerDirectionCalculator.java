package com.frank.autoplay.core;

import android.support.v4.view.ViewPager;

/**
 * Created by frank on 2018/3/23.
 */

class ViewPagerDirectionCalculator extends DirectionCalculator<ViewPager> implements ViewPager.OnPageChangeListener {

    private ViewPager mViewPager;

    private float mCurrentPositionOffset;

    private Direction mDirection = DirectionCalculator.Direction.RIGHT;

    ViewPagerDirectionCalculator(ViewPager viewPager) {
        this.mViewPager = viewPager;
        this.mViewPager.addOnPageChangeListener(this);
    }

    @Override
    ViewPager getTargetView() {
        return mViewPager;
    }

    @Override
    Direction calculate() {
        return mDirection;
    }

    @Override
    void release() {
        mViewPager.removeOnPageChangeListener(this);
        mViewPager = null;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (mCurrentPositionOffset > positionOffset) {
            mDirection = Direction.RIGHT;
        } else if (mCurrentPositionOffset < positionOffset) {
            mDirection = Direction.LEFT;
        } else {
            mDirection = Direction.IDLE;
        }
        mCurrentPositionOffset = positionOffset;
    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }
}
