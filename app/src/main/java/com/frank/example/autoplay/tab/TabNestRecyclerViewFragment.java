package com.frank.example.autoplay.tab;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.frank.autoplay.AutoPlay;
import com.frank.autoplay.core.ItemViewHelper;
import com.frank.example.autoplay.R;
import com.frank.example.autoplay.tab.autoplay.ListItemAutoPlayItem;
import com.frank.example.autoplay.tab.autoplay.ViewGroupAutoPlayItem;

import java.util.ArrayList;
import java.util.List;

import static android.support.v7.widget.RecyclerView.HORIZONTAL;
import static android.view.ViewGroup.FOCUS_BLOCK_DESCENDANTS;

/**
 * Created by frank on 2018/7/13.
 */

public class TabNestRecyclerViewFragment extends TabFragment {

    private RecyclerView mRecyclerView;

    private RecyclerView.Adapter<RecyclerView.ViewHolder> mAdapter;

    private List<Section> mSections = new ArrayList<>();

    public TabNestRecyclerViewFragment() {
        super();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initData();

        final RecyclerView.RecycledViewPool recycledViewPool = new RecyclerView.RecycledViewPool();

        mAdapter = new SectionAdapter(mSections, recycledViewPool);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FrameLayout frameLayout = new FrameLayout(getActivity());
        frameLayout.setLayoutParams(new ViewPager.LayoutParams());
        frameLayout.setDescendantFocusability(FOCUS_BLOCK_DESCENDANTS);

        mRecyclerView = new RecyclerView(getActivity());
        mRecyclerView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);
        frameLayout.addView(mRecyclerView);
        AutoPlay.bindAutoPlayItem(frameLayout, new ViewGroupAutoPlayItem(mRecyclerView));
        return frameLayout;
    }

    private void initData() {
        addSection("Section-Red", Color.RED);
        addSection("Section-Blue", Color.BLUE);
        addSection("Section-Green", Color.GREEN);
        addSection("Section-Yellow", Color.YELLOW);
        addSection("Section-Grey", Color.GRAY);
        addSection("Section-DGrey", Color.DKGRAY);
        addSection("Section-Cyan", Color.CYAN);
        addSection("Section-LightGrey", Color.LTGRAY);
        addSection("Section-MAGENTA", Color.MAGENTA);
        addSection("Section-Pink", getResources().getColor(R.color.colorAccent));
        addSection("Section-100", Color.rgb(100, 100, 100));
        addSection("Section-50", Color.rgb(50, 50, 50));
        addSection("Section-200", Color.rgb(200, 200, 200));
    }

    private void addSection(String title, @ColorInt int color) {
        Section section = new Section();
        section.setTitle(title);
        for (int i = 0; i < 20; i++) {
            ColorItem colorItem = new ColorItem();
            colorItem.setColor1(color);
            colorItem.setColor2(getResources().getColor(R.color.colorWhite));
            colorItem.setText("index:" + getIndex() + " position:" + i);
            section.getColorItems().add(colorItem);
        }
        mSections.add(section);
    }

    private static class SectionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private List<Section> mSections;
        private RecyclerView.RecycledViewPool mViewPool;

        SectionAdapter(List<Section> sections, RecyclerView.RecycledViewPool viewPool) {
            mSections = sections;
            mViewPool = viewPool;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LinearLayout linearLayout = new LinearLayout(context);
            linearLayout.setOrientation(LinearLayout.VERTICAL);

            TextView textView = new TextView(context);
            textView.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);

            final RecyclerView recyclerView = new RecyclerView(context);
            recyclerView.setLayoutManager(new LinearLayoutManager(context, HORIZONTAL, false));
            new PagerSnapHelper().attachToRecyclerView(recyclerView);
            recyclerView.setRecycledViewPool(mViewPool);


            linearLayout.addView(textView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 48 * 3));
            linearLayout.addView(recyclerView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));

            AutoPlay.bindAutoPlayItem(linearLayout, new ViewGroupAutoPlayItem(recyclerView));
            return new SectionViewHolder(linearLayout);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            SectionViewHolder viewHolder = (SectionViewHolder) holder;
            Section section = mSections.get(position);
            viewHolder.mTextView.setText(section.getTitle());
            viewHolder.mItemAdapter.setList(section.getColorItems());
        }

        @Override
        public int getItemCount() {
            return mSections.size();
        }
    }


    private static class SectionViewHolder extends RecyclerView.ViewHolder {
        private TextView mTextView;
        private RecyclerView mRecyclerView;
        private ItemAdapter mItemAdapter;

        SectionViewHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) ((ViewGroup) itemView).getChildAt(0);
            mRecyclerView = (RecyclerView) ((ViewGroup) itemView).getChildAt(1);
            mItemAdapter = new ItemAdapter();
            mRecyclerView.setAdapter(mItemAdapter);
        }

        private static class ItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
            private List<ColorItem> colorItems = new ArrayList<>();

            void setList(List<ColorItem> colorItems) {
                this.colorItems.clear();
                this.colorItems.addAll(colorItems);
                notifyDataSetChanged();
            }

            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                final View itemView = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
                int displayWidth = parent.getResources().getDisplayMetrics().widthPixels;
                int width = displayWidth;
                itemView.setLayoutParams(new RecyclerView.LayoutParams(width, (int) (width * 9 / 16f)));
                AutoPlay.bindAutoPlayItem(itemView, new ListItemAutoPlayItem((TextView) itemView));
                itemView.setBackgroundColor(Color.WHITE);
                return new RecyclerView.ViewHolder(itemView) {
                };
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
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
        }
    }


}
