package com.frank.example.autoplay.tab;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.frank.autoplay.AutoPlay;
import com.frank.example.autoplay.R;
import com.frank.example.autoplay.tab.autoplay.ListItemAutoPlayItem;
import com.frank.example.autoplay.tab.autoplay.ViewGroupAutoPlayItem;

import java.util.ArrayList;
import java.util.List;

import static android.support.v7.widget.RecyclerView.Adapter;
import static android.support.v7.widget.RecyclerView.HORIZONTAL;
import static android.support.v7.widget.RecyclerView.VERTICAL;
import static android.support.v7.widget.RecyclerView.ViewHolder;

/**
 * Created by frank on 2018/3/22.
 */
public class TabRecyclerViewFragment extends TabFragment {

    private Adapter mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final List<ColorItem> colorItems = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            ColorItem colorItem = new ColorItem();
            colorItem.setColor1(getResources().getColor(R.color.colorAccent));
            colorItem.setColor2(getResources().getColor(R.color.colorWhite));
            colorItem.setText("index:" + getIndex() + " position:" + i);
            colorItems.add(colorItem);
        }
        mAdapter = new Adapter() {
            @Override
            public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                final View itemView = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
                itemView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                AutoPlay.bindAutoPlayItem(itemView, new ListItemAutoPlayItem((TextView) itemView));
                return new ViewHolder(itemView) {
                };
            }

            @Override
            public void onBindViewHolder(ViewHolder holder, int position) {
                TextView textView = (TextView) holder.itemView;
                ListItemAutoPlayItem item = (ListItemAutoPlayItem) AutoPlay.findAutoPlayItem(textView);
                ColorItem colorItem = colorItems.get(position);
                item.setColorItem(colorItem);
                textView.setText(colorItem.getText());

            }

            @Override
            public int getItemCount() {
                return colorItems.size();
            }
        };
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tab_recyclerview_fragment, container, false);
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final RecyclerView recyclerView = (RecyclerView) view;
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(getIndex() == 3 ? HORIZONTAL : VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(mAdapter);

        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            float downX = 0;
            float downY = 0;
            boolean orientationChange = false;

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        downX = event.getX();
                        downY = event.getY();
                        orientationChange = false;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (orientationChange) {
                            return false;
                        }
                        float xDiff = Math.abs(event.getX() - downX);
                        float yDiff = Math.abs(event.getY() - downY);
                        if (xDiff > yDiff) {
                            if (linearLayoutManager.getOrientation() != LinearLayoutManager.HORIZONTAL) {
                                linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                                orientationChange = true;
                            }
                        } else {
                            if (linearLayoutManager.getOrientation() != LinearLayoutManager.VERTICAL) {
                                linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                                orientationChange = true;
                            }
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        orientationChange = false;
                        downX = 0;
                        downY = 0;
                        break;
                }
                return false;
            }
        });

        new PagerSnapHelper().attachToRecyclerView(recyclerView);
        AutoPlay.bindAutoPlayItem(recyclerView, new ViewGroupAutoPlayItem(recyclerView));
    }
}
