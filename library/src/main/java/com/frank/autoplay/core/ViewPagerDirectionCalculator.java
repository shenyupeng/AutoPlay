package com.frank.autoplay.core;

import android.support.v4.view.ViewPager;

/**
 * Created by frank on 2018/3/23.
 */

public class ViewPagerDirectionCalculator extends DirectionCalculator<ViewPager> implements ViewPager.OnPageChangeListener {

    private ViewPager mViewPager;

    private float mCurrentPositionOffset;

    private Direction mCurrentDirection = DirectionCalculator.Direction.RIGHT;

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
        return mCurrentDirection;
    }

    @Override
    void release() {
        mViewPager.removeOnPageChangeListener(this);
        mViewPager = null;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (mCurrentPositionOffset > positionOffset) {
            mCurrentDirection = Direction.RIGHT;
        } else if (mCurrentPositionOffset < positionOffset) {
            mCurrentDirection = Direction.LEFT;
        } else {
            mCurrentDirection = Direction.IDLE;
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
