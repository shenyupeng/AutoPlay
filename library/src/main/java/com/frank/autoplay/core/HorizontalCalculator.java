package com.frank.autoplay.core;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created by frank on 2018/3/22.
 */

abstract class HorizontalCalculator<TargetView extends ViewGroup> extends DirectionCalculator<TargetView> {

    private DirectionCalculator.Direction mDirection = DirectionCalculator.Direction.RIGHT;

    private int mFirstVisibleIndex = 0;
    private float mFirstVisiblePercent = 1f;

    @Override
    DirectionCalculator.Direction calculate() {
        final DirectionCalculator.Direction direction = calculateHorizontalScrollDirection();
        if (direction != DirectionCalculator.Direction.IDLE) {
            mDirection = direction;
        }
        return mDirection;
    }

    private DirectionCalculator.Direction calculateHorizontalScrollDirection() {
        final int firstVisibleChildIndex = getFirstVisibleChildIndex();
        final View fistVisibleView = getTargetView().getChildAt(firstVisibleChildIndex);
        final float firstVisiblePercent = ItemViewHelper.calculateHorizontalVisibleRatio(fistVisibleView);
        try {
            if (mFirstVisibleIndex == firstVisibleChildIndex) {
                if (mFirstVisiblePercent < firstVisiblePercent) {
                    return Direction.LEFT;
                } else if (mFirstVisiblePercent > firstVisiblePercent) {
                    return Direction.RIGHT;
                } else {
                    return DirectionCalculator.Direction.IDLE;
                }
            } else if (mFirstVisibleIndex < firstVisibleChildIndex) {
                return Direction.RIGHT;
            } else {
                return Direction.LEFT;
            }
        } finally {
            mFirstVisibleIndex = firstVisibleChildIndex;
            mFirstVisiblePercent = firstVisiblePercent;
        }
    }

    abstract int getFirstVisibleChildIndex();
}
