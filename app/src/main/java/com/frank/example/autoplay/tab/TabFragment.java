package com.frank.example.autoplay.tab;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

/**
 * Created by frank on 2018/3/22.
 */

public class TabFragment extends Fragment {
    public static final String EXTRA_INDEX = "extra_index";

    private int mIndex;

    public TabFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mIndex = getArguments().getInt(EXTRA_INDEX);
    }

    int getIndex() {
        return mIndex;
    }

}
