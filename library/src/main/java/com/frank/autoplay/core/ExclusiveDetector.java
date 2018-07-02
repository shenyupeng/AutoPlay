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

public abstract class ExclusiveDetector<TargetView extends ViewGroup> extends Detector<TargetView> {

    ExclusiveDetector(TargetView target) {
        super(target);
        setState(Detector.State.INITIALIZED);
    }

    abstract void bindDetectListener();

    abstract void unbindDetectListener();

    abstract DirectionCalculator<TargetView> getDirectionCalculator();

    @Override
    void setState(Detector.State state) {
        super.setState(state);
        L.i(this, "setState:" + state.name());
    }

    @Override
    public void start() {
        bindDetectListener();
        setState(Detector.State.STARTED);
        detect();
    }

    @Override
    public void resume() {
        setState(Detector.State.STARTED);
    }


    @Override
    public void pause() {
        setState(Detector.State.PAUSED);
    }

    @Override
    public void stop() {
        unbindDetectListener();
        deactivate();
        setState(Detector.State.STOPPED);
    }

    @Override
    public void release() {
        DirectionCalculator<TargetView> directionCalculator = getDirectionCalculator();
        if (directionCalculator != null) {
            directionCalculator.release();
        }
        setState(Detector.State.RELEASED);
    }


    @Override
    void onDetecting() {
        AutoPlayItem activatedItem = findActivatedAutoPlayableItem();

        final DirectionCalculator<TargetView> directionCalculator = getDirectionCalculator();
        final DirectionCalculator.Direction direction = directionCalculator.calculate();

        if (activatedItem == null) {
            final AutoPlayItem item = findAutoPlayableItem(direction, 0);
            if (item != null) {
                activate(item);
                activatedItem = item;
            }
        } else {
            final int activatedIndex = findActivatedAutoPlayableItemIndex();
            final AutoPlayItem item = findAutoPlayableItem(direction, activatedIndex);
            if (item == null) {
                if (checkDeactivationCondition(activatedItem, direction)) {
                    deactivate(activatedItem);
                    activatedItem = null;
                } else {
                    activatedItem.onActivationKeeping();
                    L.i(this, "onDetecting:" + "activation not changed!" + "activatedItem:" + activatedItem.getAutoPlayView().getClass().getSimpleName() + activatedItem.hashCode() + " direction:" + direction.name());
                }
            } else if (item != activatedItem) {
                deactivate(activatedItem);
                activate(item);
                activatedItem = item;
            } else if (checkDeactivationCondition(activatedItem, direction)) {
                deactivate(activatedItem);
                activatedItem = null;
            } else {
                activatedItem.onActivationKeeping();
                L.i(this, "onDetecting:" + "activation not changed!" + "activatedItem:" + activatedItem.getAutoPlayView().getClass().getSimpleName() + activatedItem.hashCode() + " direction:" + direction.name());
            }
        }

        L.i(this, "onDetecting:" + "activatedItem:" +
                (activatedItem == null ? "null" : (activatedItem.getAutoPlayView().getClass().getSimpleName() + activatedItem.hashCode())) +
                " direction:" + direction.name());
    }

    void activate(AutoPlayItem item) {
        L.i(this, "activate:" + item.getAutoPlayView().getClass().getSimpleName() + item.hashCode());
        item.activate();
        ItemViewHelper.setActivate(item.getAutoPlayView(), true);
    }

    void deactivate(AutoPlayItem item) {
        L.i(this, "deactivate:" + item.getAutoPlayView().getClass().getSimpleName() + item.hashCode());
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
