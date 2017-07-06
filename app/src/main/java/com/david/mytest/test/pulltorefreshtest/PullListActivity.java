package com.david.mytest.test.pulltorefreshtest;

import android.app.Activity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.david.mytest.R;
import com.david.mytest.ui.pulltorefresh.pullview.PullToRefreshBase;
import com.david.mytest.ui.pulltorefresh.pullview.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by David on 2016/7/5.
 */

public class PullListActivity extends Activity{
    private PullToRefreshListView mRefreshView;

    private List<String> mData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pull_list);
        initData();

        mRefreshView = (PullToRefreshListView) findViewById(R.id.pull_listview);
        mRefreshView.setMode(PullToRefreshBase.Mode.BOTH);
        mRefreshView.setAdapter(new MyAdapter());
        mRefreshView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                setTask();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                setTask();
            }
        });
    }

    private void setTask(){
        mRefreshView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mRefreshView.onRefreshComplete();
            }
        }, 1000);
    }

    private void initData(){
        mData = new ArrayList<>();
        for (int i = 0; i < 16; i++) {
            mData.add("item "+ i);
        }
    }



    class MyAdapter extends android.widget.BaseAdapter{

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public Object getItem(int position) {
            return mData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            PullListActivity.MyHolder holder = null;
            if (convertView == null){
                holder = new PullListActivity.MyHolder();
                TextView textView= new TextView(PullListActivity.this);
                textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 80, getResources().getDisplayMetrics())));
                textView.setTextSize(18);
                textView.setGravity(Gravity.CENTER);
                holder.mText = textView;
                convertView = textView;
                convertView.setTag(holder);
            } else {
                holder = (PullListActivity.MyHolder) convertView.getTag();
            }
            holder.mText.setText(mData.get(position));
            return convertView;
        }
    }

    class MyHolder {
        TextView mText;
    }
}
