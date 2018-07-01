package com.frank.autoplay.core;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created by frank on 2018/3/22.
 */

abstract class VerticalCalculator<TargetView extends ViewGroup> extends DirectionCalculator<TargetView> {

    private Direction mDirection = Direction.DOWN;

    private int mFirstVisibleIndex = 0;
    private float mFirstVisiblePercent = 1f;

    @Override
    Direction calculate() {
        final Direction direction = calculateVerticalScrollDirection();
        if (direction != Direction.IDLE) {
            mDirection = direction;
        }
        return mDirection;
    }

    private Direction calculateVerticalScrollDirection() {
        final int firstVisibleIndex = getFirstVisibleChildIndex();
        final View fistVisibleView = getTargetView().getChildAt(firstVisibleIndex);
        final float firstVisiblePercent = ItemViewHelper.calculateVerticalVisibleRatio(fistVisibleView);
        try {
            if (mFirstVisibleIndex == firstVisibleIndex) {
                if (mFirstVisiblePercent < firstVisiblePercent) {
                    return Direction.UP;
                } else if (mFirstVisiblePercent > firstVisiblePercent) {
                    return Direction.DOWN;
                } else {
                    return Direction.IDLE;
                }
            } else if (mFirstVisibleIndex < firstVisibleIndex) {
                return Direction.DOWN;
            } else {
                return Direction.UP;
            }
        } finally {
            mFirstVisibleIndex = firstVisibleIndex;
            mFirstVisiblePercent = firstVisiblePercent;
        }
    }

    abstract TargetView getTargetView();

    abstract int getFirstVisibleChildIndex();
}
