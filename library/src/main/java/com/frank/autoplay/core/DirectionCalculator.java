package com.frank.autoplay.core;

/**
 * Created by frank on 2018/3/22.
 */

import android.view.ViewGroup;

abstract class DirectionCalculator<TargetView extends ViewGroup> {


    abstract TargetView getTargetView();

    abstract Direction calculate();

    abstract void release();

    protected enum Direction {
        IDLE,
        UP,
        DOWN,
        RIGHT,
        LEFT
    }
}
