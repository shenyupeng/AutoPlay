package com.frank.autoplay.core;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.frank.autoplay.AutoPlayItem;
import com.frank.autoplay.utils.L;

import static com.frank.autoplay.core.ItemViewHelper.isActivated;

/**
 * Created by frank on 2018/3/15.
 */

public class RecyclerViewDetector extends ExclusiveDetector<RecyclerView> {

    private DetectListener mDetectListener = new DetectListener();

    private DirectionCalculator<RecyclerView> mDirectionCalculator;

    RecyclerViewDetector(RecyclerView target) {
        super(target);
    }

    @Override
    void bindDetectListener() {
        final RecyclerView targetView = getTargetView();
        targetView.removeOnScrollListener(mDetectListener);
        targetView.addOnScrollListener(mDetectListener);
        targetView.setRecyclerListener(mDetectListener);
    }

    @Override
    void unbindDetectListener() {
        final RecyclerView targetView = getTargetView();
        targetView.removeOnScrollListener(mDetectListener);
        targetView.setRecyclerListener(null);
    }

    @Override
    DirectionCalculator<RecyclerView> getDirectionCalculator() {
        if (mDirectionCalculator == null) {
            mDirectionCalculator = new RecyclerViewDirectionCalculator(getTargetView());
        }
        return mDirectionCalculator;
    }

    class DetectListener extends RecyclerView.OnScrollListener implements RecyclerView.RecyclerListener {


        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            detect();
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            detect();
        }

        @Override
        public void onViewRecycled(RecyclerView.ViewHolder holder) {
            final View view = holder.itemView;
            L.i(this, "onViewRecycled " + view);
            AutoPlayItem detectableItem = ItemViewHelper.findAutoPlayItem(view);
            if (detectableItem != null && isActivated(detectableItem.getAutoPlayView())) {
                deactivate(detectableItem);
            }
        }
    }
}
