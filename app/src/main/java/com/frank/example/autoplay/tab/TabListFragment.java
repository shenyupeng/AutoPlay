package com.frank.example.autoplay.tab;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.frank.autoplay.AutoPlay;
import com.frank.autoplay.core.ItemViewHelper;
import com.frank.example.autoplay.R;
import com.frank.example.autoplay.tab.autoplay.ListItemAutoPlayItem;
import com.frank.example.autoplay.tab.autoplay.ViewGroupAutoPlayItem;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by frank on 2018/3/10.
 */

public class TabListFragment extends TabFragment {

    private ListViewAdapter mAdapter;

    public TabListFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        List<ColorItem> colorItems = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            ColorItem colorItem = new ColorItem();
            colorItem.setColor1(getResources().getColor(R.color.colorPrimary));
            colorItem.setColor2(getResources().getColor(R.color.colorWhite));
            colorItem.setText("index:" + getIndex() + " position:" + i);
            colorItems.add(colorItem);
        }
        mAdapter = new ListViewAdapter();
        mAdapter.setList(colorItems);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tab_list_fragment, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AbsListView absListView = (AbsListView) view;
        absListView.setAdapter(mAdapter);
        AutoPlay.bindAutoPlayItem(absListView, new ViewGroupAutoPlayItem(absListView));
    }


    private static class ListViewAdapter extends BaseAdapter {

        private List<ColorItem> mItems = new ArrayList<>();

        private void setList(List<ColorItem> items) {
            mItems.clear();
            mItems.addAll(items);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mItems.size();
        }

        @Override
        public ColorItem getItem(int position) {
            return mItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            final TextView textView;
            if (convertView == null) {
                int displayWidth = parent.getResources().getDisplayMetrics().widthPixels;
                LinearLayout linearLayout = new LinearLayout(parent.getContext());
                linearLayout.setOrientation(LinearLayout.VERTICAL);
                linearLayout.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, displayWidth));
                textView = new TextView(parent.getContext());
                textView.setGravity(Gravity.CENTER);
                linearLayout.addView(textView, new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, displayWidth/3 * 2));
                convertView = linearLayout;
                AutoPlay.bindAutoPlayItem(convertView, new ListItemAutoPlayItem(textView));
            } else {
                textView = (TextView) ((ViewGroup) convertView).getChildAt(0);
            }
            ColorItem colorItem = getItem(position);
            ListItemAutoPlayItem item = (ListItemAutoPlayItem) AutoPlay.findAutoPlayItem(convertView);
            item.setColorItem(colorItem);
            textView.setText(colorItem.getText());
            return convertView;
        }
    }
}
