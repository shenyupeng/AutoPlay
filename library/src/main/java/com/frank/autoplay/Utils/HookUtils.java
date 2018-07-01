package com.frank.autoplay.Utils;

import android.support.annotation.Nullable;
import android.widget.AbsListView;

/**
 * Created by frank on 2018/3/11.
 */

public class HookUtils {

    @Nullable
    public static AbsListView.OnScrollListener hookOnScrollListener(AbsListView view) {
        return ReflectUtils.getField(view, AbsListView.class, "mOnScrollListener");
    }

    @Nullable
    public static AbsListView.RecyclerListener hookRecyclerListener(AbsListView view) {
        final Class<?> RecycleBin = ReflectUtils.getInnerClass(AbsListView.class, "RecycleBin");
        final Object mRecycler = ReflectUtils.getField(view, AbsListView.class, "mRecycler");
        if (RecycleBin != null && mRecycler != null) {
            return ReflectUtils.getField(mRecycler, RecycleBin, "mRecyclerListener");
        }
        return null;
    }
}
