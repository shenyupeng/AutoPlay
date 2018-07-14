package com.frank.autoplay.core;

import android.graphics.Rect;
import android.view.View;

import com.frank.autoplay.AutoPlayItem;
import com.frank.autoplay.R;

/**
 * Created by frank on 2018/3/11.
 */

public class ItemViewHelper {

    private static float calculateVisibleRatio(View view) {
        ensureRect();
        final Rect rect = sRect;
        final boolean visible = view.getLocalVisibleRect(rect);
        float ratio = 0;
        if (visible) {
            final int viewWidth = view.getWidth();
            final int viewHeight = view.getHeight();
            final int visibleWidth = rect.width();
            final int visibleHeight = rect.height();
            if (viewWidth > 0 && viewHeight > 0) {
                ratio = (visibleWidth * visibleHeight) / (float) (viewWidth * viewHeight);
            }
        }
        return ratio;
    }

    static float calculateVerticalVisibleRatio(View view) {
        ensureRect();
        final Rect rect = sRect;
        final boolean visible = view.getLocalVisibleRect(rect);
        float ratio = 0;
        if (visible) {
            final int viewHeight = view.getHeight();
            final int visibleHeight = rect.height();
            if (viewHeight > 0) {
                ratio = visibleHeight / (float) viewHeight;
            }
        }
        return ratio;
    }

    static float calculateHorizontalVisibleRatio(View view) {
        ensureRect();
        final Rect rect = sRect;
        final boolean visible = view.getLocalVisibleRect(rect);
        float ratio = 0;
        if (visible) {
            final int viewWidth = view.getWidth();
            final int visibleWidth = rect.width();
            if (viewWidth > 0) {
                ratio = visibleWidth / (float) viewWidth;
            }
        }
        return ratio;
    }

    private static Rect sRect;

    private static void ensureRect() {
        sRect = sRect == null ? new Rect() : sRect;
    }

    static boolean isVerticalInvisibleEnough(View view, float ratio) {
        return 1 - calculateVerticalVisibleRatio(view) >= ratio;
    }

    static boolean isHorizontalInvisibleEnough(View view, float ratio) {
        return 1 - calculateHorizontalVisibleRatio(view) >= ratio;
    }

    static boolean isInvisibleEnough(View view, float ratio) {
        return 1 - calculateVisibleRatio(view) >= ratio;
    }

    static boolean isVerticalVisibleEnough(View view, float ratio) {
        return calculateVerticalVisibleRatio(view) >= ratio;
    }

    static boolean isHorizontalVisibleEnough(View view, float ratio) {
        return calculateHorizontalVisibleRatio(view) >= ratio;
    }

    static boolean isVisibleEnough(View view, float ratio) {
        return calculateVisibleRatio(view) >= ratio;
    }

    static boolean isActivated(View view) {
        final Object object = view.getTag(R.id.tag_item_view_detector_activation_state);
        return object != null && (Boolean) object;
    }

    static void setActivate(View view, boolean isActivate) {
        view.setTag(R.id.tag_item_view_detector_activation_state, isActivate);
    }

    public static AutoPlayItem findAutoPlayItem(final View view) {
        final Object o = view.getTag(R.id.tag_item_view_auto_play_item);
        if (o instanceof AutoPlayItem) {
            return (AutoPlayItem) o;
        }
        if (view instanceof AutoPlayItem) {
            return (AutoPlayItem) view;
        }
        return null;
    }

    public static void bindAutoPlayItem(View view, AutoPlayItem autoPlayItem) {
        view.setTag(R.id.tag_item_view_auto_play_item, autoPlayItem);
    }
}
