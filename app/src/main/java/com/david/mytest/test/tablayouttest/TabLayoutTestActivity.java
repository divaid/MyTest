package com.david.mytest.test.tablayouttest;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import com.david.mytest.R;

import java.util.ArrayList;

/**
 * Created by weixi on 2017/6/15.
 */

public class TabLayoutTestActivity extends FragmentActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tablayout_test);
        tabLayout = (TabLayout) findViewById(R.id.tab_test);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        viewPager = (ViewPager) findViewById(R.id.viewpager_test);
        viewPager.setAdapter(initAdapter());
        tabLayout.setupWithViewPager(viewPager);
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(R.layout.widget_tab_title);
            TextView title = (TextView) tab.getCustomView().findViewById(R.id.tab_title);
            if (i == 0)
                title.setTextColor(Color.GREEN);//刚进入设置选中title的样式

            tab.setTag(title);

        }
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //在此方法中设置选中title的样式
                ((TextView) tab.getTag()).setTextColor(Color.GREEN);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                ((TextView) tab.getTag()).setTextColor(Color.BLACK);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private MyAdapter initAdapter() {
        ArrayList<MyFragment> fragments = new ArrayList<>();
        ArrayList<String> titles = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            fragments.add(new MyFragment());
            titles.add("tab" + i);
        }
        return new MyAdapter(getSupportFragmentManager(), fragments, titles);
    }

    class MyAdapter extends FragmentPagerAdapter {
        private ArrayList<MyFragment> fragments;
        private ArrayList<String> titles;

        public MyAdapter(FragmentManager fm, ArrayList<MyFragment> fragments, ArrayList<String> titles) {
            super(fm);
            this.fragments = fragments;
            this.titles = titles;
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

//        @Override
//        public CharSequence getPageTitle(int position) {
//            return titles.get(position);
//        }
    }
}
