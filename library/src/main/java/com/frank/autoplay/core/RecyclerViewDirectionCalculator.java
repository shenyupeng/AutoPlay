package com.frank.autoplay.core;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

/**
 * Created by frank on 2018/3/23.
 */

class RecyclerViewDirectionCalculator extends DirectionCalculator<RecyclerView> {

    private RecyclerView mRecyclerView;

    private HorizontalCalculator<RecyclerView> mHorizontalCalculator;

    private VerticalCalculator<RecyclerView> mVerticalCalculator;


    RecyclerViewDirectionCalculator(RecyclerView recyclerView) {
        this.mRecyclerView = recyclerView;
    }

    @Override
    RecyclerView getTargetView() {
        return mRecyclerView;
    }

    @Override
    Direction calculate() {
        final RecyclerView.LayoutManager layoutManager = mRecyclerView.getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager) {
            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;

            if (linearLayoutManager.getOrientation() == LinearLayoutManager.VERTICAL) {
                if (mVerticalCalculator == null) {
                    mVerticalCalculator = new RecyclerViewVerticalDirectionCalculator(mRecyclerView);
                }
                return mVerticalCalculator.calculate();

            } else if (linearLayoutManager.getOrientation() == LinearLayoutManager.HORIZONTAL) {
                if (mHorizontalCalculator == null) {
                    mHorizontalCalculator = new RecyclerViewHorizontalDirectionCalculator(mRecyclerView);
                }
                return mHorizontalCalculator.calculate();
            }
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            final StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;

            if (staggeredGridLayoutManager.getOrientation() == StaggeredGridLayoutManager.VERTICAL) {
                if (mVerticalCalculator == null) {
                    mVerticalCalculator = new RecyclerViewVerticalDirectionCalculator(mRecyclerView);
                }
                return mVerticalCalculator.calculate();
            } else if (staggeredGridLayoutManager.getOrientation() == StaggeredGridLayoutManager.HORIZONTAL) {
                if (mHorizontalCalculator == null) {
                    mHorizontalCalculator = new RecyclerViewHorizontalDirectionCalculator(mRecyclerView);
                }
                return mHorizontalCalculator.calculate();
            }
        }
        return null;
    }

    @Override
    void release() {
        if (mHorizontalCalculator != null) {
            mHorizontalCalculator.release();
        }

        if (mVerticalCalculator != null) {
            mVerticalCalculator.release();
        }
    }


    private static class RecyclerViewHorizontalDirectionCalculator extends HorizontalCalculator<RecyclerView> {

        private RecyclerView mRecyclerView;

        RecyclerViewHorizontalDirectionCalculator(RecyclerView recyclerView) {
            this.mRecyclerView = recyclerView;
        }

        @Override
        RecyclerView getTargetView() {
            return mRecyclerView;
        }

        @Override
        void release() {
            mRecyclerView = null;
        }

        @Override
        int getFirstVisibleChildIndex() {
            if (mRecyclerView.getChildCount() > 0) {
                return 0;
            }
            return -1;
        }
    }


    private static class RecyclerViewVerticalDirectionCalculator extends VerticalCalculator<RecyclerView> {

        private RecyclerView mRecyclerView;

        public RecyclerViewVerticalDirectionCalculator(RecyclerView recyclerView) {
            this.mRecyclerView = recyclerView;
        }

        @Override
        RecyclerView getTargetView() {
            return mRecyclerView;
        }

        @Override
        void release() {
            mRecyclerView = null;
        }

        @Override
        int getFirstVisibleChildIndex() {
            if (mRecyclerView.getChildCount() > 0) {
                return 0;
            }
            return -1;
        }
    }


}
