package com.frank.example.autoplay.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.frank.autoplay.AutoPlay;
import com.frank.example.autoplay.R;
import com.frank.example.autoplay.tab.TabFragment;
import com.frank.example.autoplay.tab.TabListFragment;
import com.frank.example.autoplay.tab.TabRecyclerViewFragment;

public class MainActivity extends AppCompatActivity {
    private ViewPager mViewPager;

    private FragmentStatePagerAdapter mFragmentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        mViewPager = findViewById(R.id.viewpager);
        mFragmentAdapter = new FragmentAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mFragmentAdapter);
        AutoPlay.startDetect(this, mViewPager);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private static class FragmentAdapter extends FragmentStatePagerAdapter {

        FragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            TabFragment fragment;
            if (position % 2 == 0) {
                fragment = new TabListFragment();
            } else {
                fragment = new TabRecyclerViewFragment();
            }
            Bundle bundle = new Bundle();
            bundle.putInt(TabFragment.EXTRA_INDEX, position);
            fragment.setArguments(bundle);
            return fragment;
        }

        @Override
        public int getCount() {
            return 4;
        }
    }
}
