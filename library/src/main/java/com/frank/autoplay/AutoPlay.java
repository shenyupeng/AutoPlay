package com.frank.autoplay;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Build;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import com.frank.autoplay.core.ItemViewHelper;
import com.frank.autoplay.core.LifecycleFragment;

/**
 * Created by frank on 2018/3/9.
 */

public class AutoPlay {


    public static void startDetect(@NonNull Activity activity, @NonNull ViewGroup viewGroup) {
        final LifecycleFragment fragment = makeSureInitialized(activity);
        if (fragment != null) {
            fragment.startDetect(viewGroup);
        }
    }

    public static void stopDetect(@NonNull Activity activity, @NonNull ViewGroup viewGroup) {
        final LifecycleFragment fragment = makeSureInitialized(activity);
        if (fragment != null) {
            fragment.stopDetect(viewGroup);
        }
    }

    public static AutoPlayItem findAutoPlayItem(final View view) {
        return ItemViewHelper.findAutoPlayItem(view);
    }

    public static void bindAutoPlayItem(View view, AutoPlayItem autoPlayItem) {
        ItemViewHelper.bindAutoPlayItem(view, autoPlayItem);
    }

    public static void keep(@NonNull Activity activity, boolean isKeep) {
        final LifecycleFragment fragment = makeSureInitialized(activity);
        if (fragment != null) {
            fragment.setKeep(isKeep);
        }
    }

    private static LifecycleFragment makeSureInitialized(Activity activity) {
        final FragmentManager fragmentManager = compatActivity(activity).getFragmentManager();
        LifecycleFragment fragment = (LifecycleFragment) fragmentManager.findFragmentByTag(LifecycleFragment.TAG);
        if (fragment == null) {
            fragment = new LifecycleFragment();
            FragmentTransaction transaction = fragmentManager.beginTransaction().add(fragment, LifecycleFragment.TAG);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                transaction.commitNow();
            } else {
                transaction.commit();
                fragmentManager.executePendingTransactions();
            }
        }
        return fragment;
    }

    private static Activity compatActivity(Activity activity) {
        if (activity.getParent() != null) {
            activity = activity.getParent();
        }
        return activity;
    }
}
