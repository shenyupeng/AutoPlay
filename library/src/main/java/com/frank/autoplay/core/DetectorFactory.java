package com.frank.autoplay.core;

import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.AbsListView;

/**
 * Created by frank on 2018/3/22.
 */

class DetectorFactory {


    static Detector create(ViewGroup viewGroup) {
        if (viewGroup instanceof AbsListView) {
            return new AbsListViewDetector((AbsListView) viewGroup);
        } else if (viewGroup instanceof RecyclerView) {
            return new RecyclerViewDetector((RecyclerView) viewGroup);
        } else if (viewGroup instanceof ViewPager) {
            return new ViewPagerDetector((ViewPager) viewGroup);
        }
        return null;
    }
}
