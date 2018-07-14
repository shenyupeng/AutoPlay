package com.frank.autoplay.core;

import android.widget.AbsListView;

/**
 * Created by frank on 2018/3/23.
 */

class AbsListViewDirectionCalculator extends VerticalCalculator<AbsListView> {

    private AbsListView mListView;

    AbsListViewDirectionCalculator(AbsListView listView) {
        this.mListView = listView;
    }

    @Override
    AbsListView getTargetView() {
        return mListView;
    }

    @Override
    void release() {
        mListView = null;
    }

    @Override
    int getFirstVisibleChildIndex() {
        if (mListView.getChildCount() > 0) {
            return 0;
        }
        return -1;
    }
}
