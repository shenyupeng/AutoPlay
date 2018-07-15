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
import com.frank.example.autoplay.tab.TabNestRecyclerViewFragment;
import com.frank.example.autoplay.tab.TabRecyclerViewFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        ViewPager viewPager = findViewById(R.id.viewpager);
        viewPager.setAdapter(new FragmentAdapter(getSupportFragmentManager()));
        AutoPlay.startDetect(this, viewPager);
    }

    private static class FragmentAdapter extends FragmentStatePagerAdapter {

        FragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            TabFragment fragment;
            if (position == 0) {
                fragment = new TabListFragment();
            } else if (position == 1){
                fragment = new TabNestRecyclerViewFragment();
            } else if (position == 2){
                fragment = new TabNestRecyclerViewFragment();
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
