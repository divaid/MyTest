package com.david.mytest.test.pulltorefreshtest;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.david.mytest.R;
import com.david.mytest.ui.pulltorefresh.pullview.PullToRefreshBase;
import com.david.mytest.ui.pulltorefresh.pullview.PullToRefreshRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by David on 2016/7/5.
 */

public class PullRecyclerActivity extends Activity {
    PullToRefreshRecyclerView mRefreshView;

    private List<String> mData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pull_recycler);
        initData();

        mRefreshView = (PullToRefreshRecyclerView) findViewById(R.id.pull_recycler);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        GridLayoutManager manager1 = new GridLayoutManager(this, 2);
        manager1.setOrientation(LinearLayoutManager.VERTICAL);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRefreshView.getRefreshableView().setLayoutManager(manager1);
        mRefreshView.getRefreshableView().setAdapter(new MyAdapter());
        mRefreshView.setMode(PullToRefreshBase.Mode.BOTH);
        mRefreshView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<RecyclerView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                setTask();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                setTask();
            }
        });
    }

    private void initData(){
        mData = new ArrayList<>();
        for (int i = 0; i < 16; i++) {
            mData.add("item "+ i);
        }
    }

    private void setTask() {
        mRefreshView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mRefreshView.onRefreshComplete();
            }
        }, 1000);
    }

    class MyAdapter extends RecyclerView.Adapter{

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            TextView textView= new TextView(PullRecyclerActivity.this);
            textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 80, getResources().getDisplayMetrics())));
            textView.setTextSize(18);
            textView.setGravity(Gravity.CENTER);
            return new MyHolder(textView);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ((MyHolder) holder).mText.setText(mData.get(position));
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }
    }

    class MyHolder extends RecyclerView.ViewHolder{
        private TextView mText;
        public MyHolder(View itemView) {
            super(itemView);
            mText = (TextView) itemView;
        }
    }
}
