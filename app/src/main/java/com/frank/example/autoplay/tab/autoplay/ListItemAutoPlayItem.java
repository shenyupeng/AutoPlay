package com.frank.example.autoplay.tab.autoplay;

import android.animation.ValueAnimator;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.view.View;
import android.widget.TextView;

import com.frank.autoplay.AutoPlayItem;
import com.frank.example.autoplay.tab.ColorItem;

/**
 * Created by frank on 2018/7/1.
 */

public class ListItemAutoPlayItem implements AutoPlayItem {

    private final TextView mAutoPlayView;

    private ColorItem mColorItem;

    private ValueAnimator mActiveAnimator;

    public ListItemAutoPlayItem(TextView itemView) {
        this.mAutoPlayView = itemView;
    }

    public void setColorItem(ColorItem colorItem) {
        this.mColorItem = colorItem;
    }

    @Override
    public View getAutoPlayView() {
        return mAutoPlayView;
    }

    @Override
    public float getActivationRatio() {
        return 1 / 2f;
    }

    @Override
    public float getDeactivationRatio() {
        return 1 / 2f;
    }

    @Override
    public boolean isActivable() {
        return true;
    }

    @Override
    public void deactivate() {
        if (mActiveAnimator != null) {
            mActiveAnimator.end();
        }
        mAutoPlayView.setBackgroundColor(mColorItem.getColor2());
    }

    @Override
    public void activate() {
        if (mActiveAnimator == null) {
            mActiveAnimator = ValueAnimator.ofFloat(0, 1);
            mActiveAnimator.setDuration(500);
            mActiveAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float value = (float) animation.getAnimatedValue();
                    mAutoPlayView.setBackgroundColor(blendColors(mColorItem.getColor1(), mColorItem.getColor2(), value));
                }
            });
        }
        if (!mActiveAnimator.isStarted()) {
            mActiveAnimator.start();
        }
    }

    @Override
    public void onActivationKeeping() {
    }

    @ColorInt
    private static int blendColors(int color1, int color2, float ratio) {
        final float inverseRation = 1f - ratio;
        float r = (Color.red(color1) * ratio) + (Color.red(color2) * inverseRation);
        float g = (Color.green(color1) * ratio) + (Color.green(color2) * inverseRation);
        float b = (Color.blue(color1) * ratio) + (Color.blue(color2) * inverseRation);
        return Color.rgb((int) r, (int) g, (int) b);
    }
}
