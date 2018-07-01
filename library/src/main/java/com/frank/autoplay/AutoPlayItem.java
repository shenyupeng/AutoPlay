package com.frank.autoplay;

import android.view.View;

/**
 * Created by frank on 2018/3/10.
 */

public interface AutoPlayItem {

    View getAutoPlayView();

    float getActivationRatio();

    float getDeactivationRatio();

    boolean isActivable();
    
    void deactivate();

    void activate();

    void onActivationKeeping();
}
