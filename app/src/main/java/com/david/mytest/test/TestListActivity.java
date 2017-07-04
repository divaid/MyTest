package com.david.mytest.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.david.mytest.R;

/**
 * Created by weixi on 2017/6/15.
 */

public class TestListActivity extends Activity{
    private RecyclerView mList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_list);
        mList = (RecyclerView) findViewById(R.id.recycle_test_list);
        final String[] names = getResources().getStringArray(R.array.testlist);
        MyAdapter adapter = new MyAdapter(names);
        adapter.setmOnItemClickListener(new OnMyItemClickListener() {
            @Override
            public void onItemClick(int position){
                try {
                    startActivity(new Intent(TestListActivity.this, Class.forName(names[position])));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
        mList.setAdapter(adapter);
    }
    class MyAdapter extends RecyclerView.Adapter implements View.OnClickListener {
        public void setmOnItemClickListener(OnMyItemClickListener mOnItemClickListener) {
            this.mOnItemClickListener = mOnItemClickListener;
        }

        private OnMyItemClickListener mOnItemClickListener;
        private String[] names;

        public MyAdapter(String[] names) {
            this.names = names;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            TextView textView = new TextView(TestListActivity.this);
            textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,80));
            textView.setTextSize(18);
            textView.setOnClickListener(this);
            MyViewHolder holder =new MyViewHolder(textView);
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ((MyViewHolder) holder).name.setText(names[position]);
        }

        @Override
        public int getItemCount() {
            return names.length;
        }

        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick((Integer) v.getTag());
            }
        }
    }
    interface OnMyItemClickListener{//一般定义到Adapter中
        void onItemClick(int position);
    }
    class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView name;
        public MyViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView;
        }
    }
}
