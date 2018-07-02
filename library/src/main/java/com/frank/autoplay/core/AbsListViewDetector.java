package com.frank.autoplay.core;

import android.view.View;
import android.widget.AbsListView;

import com.frank.autoplay.AutoPlayItem;
import com.frank.autoplay.utils.L;

import java.util.ArrayList;
import java.util.List;

import static com.frank.autoplay.utils.HookUtils.hookOnScrollListener;
import static com.frank.autoplay.utils.HookUtils.hookRecyclerListener;
import static com.frank.autoplay.core.ItemViewHelper.isActivated;

/**
 * Created by frank on 2018/3/9.
 */

public class AbsListViewDetector extends ExclusiveDetector<AbsListView> {

    private DirectionCalculator<AbsListView> mDirectionCalculator;

    private final ProxyOnScrollListener mProxyScrollListener = new ProxyOnScrollListener();

    private final ProxyRecyclerListener mProxyRecyclerListener = new ProxyRecyclerListener();

    private AbsListView.OnScrollListener mOriginalScrollListener;

    private AbsListView.RecyclerListener mOriginalRecyclerListener;

    private boolean mHasBindListener;

    AbsListViewDetector(AbsListView target) {
        super(target);
    }

    @Override
    void bindDetectListener() {
        if (!mHasBindListener) {
            mHasBindListener = true;
            final AbsListView targetView = getTargetView();

            final DetectListener detectListener = new DetectListener();

            final AbsListView.OnScrollListener scrollListener = hookOnScrollListener(targetView);
            if (scrollListener == null) {
                targetView.setOnScrollListener(detectListener);
            } else {
                mOriginalScrollListener = scrollListener;
                mProxyScrollListener.add(detectListener);
                mProxyScrollListener.add(mOriginalScrollListener);
                targetView.setOnScrollListener(mProxyScrollListener);
            }

            final AbsListView.RecyclerListener recyclerListener = hookRecyclerListener(targetView);
            if (recyclerListener == null) {
                targetView.setRecyclerListener(detectListener);
            } else {
                mOriginalRecyclerListener = recyclerListener;
                mProxyRecyclerListener.add(mOriginalRecyclerListener);
                mProxyRecyclerListener.add(detectListener);
                targetView.setRecyclerListener(mProxyRecyclerListener);
            }

        } else {
            L.i(this, "bindDetectListener: listener has bind already!");
        }
    }

    @Override
    void unbindDetectListener() {
        if (mHasBindListener) {
            mHasBindListener = false;
            final AbsListView targetView = getTargetView();
            if (mOriginalScrollListener != null) {
                targetView.setOnScrollListener(mOriginalScrollListener);
                mOriginalScrollListener = null;
            } else {
                targetView.setOnScrollListener(null);
                mProxyScrollListener.clear();
            }
            if (mOriginalRecyclerListener != null) {
                targetView.setRecyclerListener(mOriginalRecyclerListener);
                mOriginalRecyclerListener = null;
            } else {
                targetView.setRecyclerListener(null);
                mProxyRecyclerListener.clear();
            }
        } else {
            L.i(this, "unbindDetectListener: not bind listener yet!");
        }
    }

    @Override
    DirectionCalculator<AbsListView> getDirectionCalculator() {
        if (mDirectionCalculator == null) {
            mDirectionCalculator = new AbsListViewDirectionCalculator(getTargetView());
        }
        return mDirectionCalculator;
    }

    private class DetectListener implements AbsListView.OnScrollListener, AbsListView.RecyclerListener {

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            detect();
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            detect();
        }

        @Override
        public void onMovedToScrapHeap(View view) {
            L.i(this, "onMovedToScrapHeap " + view);
            AutoPlayItem autoPlayItem = ItemViewHelper.findAutoPlayItem(view);
            if (autoPlayItem != null && isActivated(autoPlayItem.getAutoPlayView())) {
                deactivate(autoPlayItem);
            }
        }
    }

    private static class ProxyOnScrollListener implements AbsListView.OnScrollListener {

        private List<AbsListView.OnScrollListener> mListeners = new ArrayList<>();

        private void add(AbsListView.OnScrollListener listener) {
            if (!mListeners.contains(listener)) {
                mListeners.add(listener);
            }
        }

        private void remove(AbsListView.OnScrollListener listener) {
            mListeners.remove(listener);
        }

        private void clear() {
            mListeners.clear();
        }

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            for (AbsListView.OnScrollListener listener : mListeners) {
                listener.onScrollStateChanged(view, scrollState);
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            for (AbsListView.OnScrollListener listener : mListeners) {
                listener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
            }
        }
    }

    private static class ProxyRecyclerListener implements AbsListView.RecyclerListener {

        private List<AbsListView.RecyclerListener> mListeners = new ArrayList<>();

        private void add(AbsListView.RecyclerListener listener) {
            if (!mListeners.contains(listener)) {
                mListeners.add(listener);
            }
        }

        private void remove(AbsListView.RecyclerListener listener) {
            mListeners.remove(listener);
        }

        private void clear() {
            mListeners.clear();
        }

        @Override
        public void onMovedToScrapHeap(View view) {
            for (AbsListView.RecyclerListener listener : mListeners) {
                listener.onMovedToScrapHeap(view);
            }
        }
    }
}
