package com.david.mytest.test;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.david.mytest.R;
import com.david.mytest.ui.VerticalViewPager;
import com.david.mytest.ui.VerticalViewPagerAllowChildScroll;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mac on 2018/7/28.
 */

public class ActivityVerticalViewPager extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vertical_pager);
        final List<View> views = new ArrayList<>();
        for(int i = 0; i < 3; ++i) {
            ImageView imageView = new ImageView(this);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setImageResource(R.mipmap.ic_launcher);
            views.add(imageView);
        }
        VerticalViewPager pager = (VerticalViewPager) findViewById(R.id.activity_vertical_pager);
        pager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return views.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return object == view;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(views.get(position));
                return views.get(position);
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(views.get(position));
            }
        });


        final List<View> scrollViews = new ArrayList<>();
        scrollViews.add(LayoutInflater.from(this).inflate(R.layout.item_vertical_pager, null));
        scrollViews.add(LayoutInflater.from(this).inflate(R.layout.item_vertical_pager, null));
        scrollViews.add(LayoutInflater.from(this).inflate(R.layout.item_vertical_pager, null));

        VerticalViewPagerAllowChildScroll viewPager = (VerticalViewPagerAllowChildScroll) findViewById(R.id.activity_vertical_pager_allow_child_scroll);
        viewPager.setViews(scrollViews);
        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return scrollViews.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return object == view;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(scrollViews.get(position));
                return scrollViews.get(position);
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(scrollViews.get(position));
            }
        });
    }
}
