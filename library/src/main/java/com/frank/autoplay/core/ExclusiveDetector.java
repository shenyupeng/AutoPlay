package com.frank.autoplay.core;

import android.view.View;
import android.view.ViewGroup;

import com.frank.autoplay.AutoPlayItem;
import com.frank.autoplay.utils.L;

import static com.frank.autoplay.core.ItemViewHelper.isActivated;
import static com.frank.autoplay.core.ItemViewHelper.isHorizontalInvisibleEnough;
import static com.frank.autoplay.core.ItemViewHelper.isHorizontalVisibleEnough;
import static com.frank.autoplay.core.ItemViewHelper.isInvisibleEnough;
import static com.frank.autoplay.core.ItemViewHelper.isVerticalInvisibleEnough;
import static com.frank.autoplay.core.ItemViewHelper.isVerticalVisibleEnough;
import static com.frank.autoplay.core.ItemViewHelper.isVisibleEnough;

/**
 * Created by frank on 2018/3/22.
 */

abstract class ExclusiveDetector<TargetView extends ViewGroup> extends Detector<TargetView> {

    ExclusiveDetector(TargetView target) {
        super(target);
        setState(State.INITIALIZED);
    }

    abstract void bindDetectListener();

    abstract void unbindDetectListener();

    abstract DirectionCalculator<TargetView> getDirectionCalculator();

    @Override
    void setState(Detector.State state) {
        L.i(this, "setState:", (getState() == null ? null : getState().name()) + " -> " + state.name());
        super.setState(state);
    }

    @Override
    public void start() {
        bindDetectListener();
        setState(State.STARTED);
        resume();
        detect();
    }

    @Override
    public void resume() {
        setState(State.RESUMED);
    }

    @Override
    public void pause() {
        setState(State.PAUSED);
    }

    @Override
    public void stop() {
        unbindDetectListener();
        deactivate();
        setState(State.STOPPED);
    }

    @Override
    public void release() {
        DirectionCalculator<TargetView> directionCalculator = getDirectionCalculator();
        if (directionCalculator != null) {
            directionCalculator.release();
        }
        setState(State.RELEASED);
    }

    @Override
    void onDetecting() {
        L.i(this, "onDetecting", "START");
        long start = System.currentTimeMillis();

        AutoPlayItem activatedItem = findActivatedAutoPlayableItem();
        final DirectionCalculator.Direction direction = calculateDirection();
        if (activatedItem == null) {
            final AutoPlayItem item = findAutoPlayableItem(direction, 0);
            if (item != null) {
                activate(item);
                activatedItem = item;
            } else {
                L.i(this, "onDetecting", "Can't find detectable item");
            }
        } else {
            final int activatedIndex = findActivatedAutoPlayableItemIndex();
            final AutoPlayItem item = findAutoPlayableItem(direction, activatedIndex);
            if (item == null) {
                if (checkDeactivationCondition(activatedItem, direction)) {
                    deactivate(activatedItem);
                    activatedItem = null;
                } else {
                    keepActivation(activatedItem);
                }
            } else if (item != activatedItem) {
                deactivate(activatedItem);
                activate(item);
                activatedItem = item;
            } else if (checkDeactivationCondition(activatedItem, direction)) {
                deactivate(activatedItem);
                activatedItem = null;
            } else {
                keepActivation(activatedItem);
            }
        }
        L.i(this, "onDetecting", "END " + "(" + (System.currentTimeMillis() - start) + ")" + " activated:" + L.toString(activatedItem) + " direction:" + direction.name());
    }

    DirectionCalculator.Direction calculateDirection() {
        return getDirectionCalculator().calculate();
    }

    void keepActivation(AutoPlayItem item) {
        L.i(this, "keepActivation", L.toString(item));
        item.onActivationKeeping();
    }

    void activate(AutoPlayItem item) {
        L.i(this, "activate", L.toString(item));
        item.activate();
        ItemViewHelper.setActivate(item.getAutoPlayView(), true);
    }

    void deactivate(AutoPlayItem item) {
        L.i(this, "deactivate", L.toString(item));
        item.deactivate();
        ItemViewHelper.setActivate(item.getAutoPlayView(), false);
    }

    void deactivate() {
        final AutoPlayItem item = findActivatedAutoPlayableItem();
        if (item != null) {
            deactivate(item);
        }
    }

    private boolean checkDeactivationCondition(AutoPlayItem item, DirectionCalculator.Direction direction) {
        if (item.isActivable()) {
            final View view = item.getAutoPlayView();
            final float ratio = item.getDeactivationRatio();
            switch (direction) {
                case UP:
                case DOWN:
                    return isVerticalInvisibleEnough(view, ratio);
                case LEFT:
                case RIGHT:
                    return isHorizontalInvisibleEnough(view, ratio);
            }
            return isInvisibleEnough(view, ratio);
        } else {
            return true;
        }
    }

    private boolean checkActivationCondition(AutoPlayItem item, DirectionCalculator.Direction direction) {
        if (item.isActivable()) {
            final View view = item.getAutoPlayView();
            final float ratio = item.getActivationRatio();
            switch (direction) {
                case UP:
                case DOWN:
                    return isVerticalVisibleEnough(view, ratio);
                case LEFT:
                case RIGHT:
                    return isHorizontalVisibleEnough(view, ratio);
            }
            return isVisibleEnough(view, ratio);
        } else {
            return false;
        }
    }

    private AutoPlayItem findAutoPlayableItem(DirectionCalculator.Direction direction, int activatedIndex) {
        final ViewGroup targetView = getTargetView();
        final int count = targetView.getChildCount();
        switch (direction) {
            case LEFT:
            case UP: {
                for (int i = 0; i < count; i++) {
                    final View view = targetView.getChildAt(i);
                    final AutoPlayItem autoPlayItem = ItemViewHelper.findAutoPlayItem(view);
                    if (autoPlayItem != null) {
                        if (checkActivationCondition(autoPlayItem, direction)) {
                            return autoPlayItem;
                        }
                    }
                }
                break;
            }
            case RIGHT:
            case DOWN: {
                for (int i = activatedIndex; i < count; i++) {
                    final View view = targetView.getChildAt(i);
                    final AutoPlayItem autoPlayItem = ItemViewHelper.findAutoPlayItem(view);
                    if (autoPlayItem != null) {
                        if (checkActivationCondition(autoPlayItem, direction)) {
                            return autoPlayItem;
                        }
                    }
                }
                break;
            }
        }
        return null;
    }


    private AutoPlayItem findActivatedAutoPlayableItem() {
        final ViewGroup targetView = getTargetView();
        final int count = targetView.getChildCount();
        for (int i = 0; i < count; i++) {
            final View itemView = targetView.getChildAt(i);
            final AutoPlayItem item = ItemViewHelper.findAutoPlayItem(itemView);
            if (item != null && isActivated(item.getAutoPlayView())) {
                return item;
            }
        }
        return null;
    }

    private int findActivatedAutoPlayableItemIndex() {
        final ViewGroup targetView = getTargetView();
        final int count = targetView.getChildCount();
        for (int i = 0; i < count; i++) {
            final View itemView = targetView.getChildAt(i);
            final AutoPlayItem item = ItemViewHelper.findAutoPlayItem(itemView);
            if (item != null && isActivated(item.getAutoPlayView())) {
                return i;
            }
        }
        return -1;
    }
}
