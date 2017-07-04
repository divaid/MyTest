package com.david.mytest.test.snaphelpertest;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.david.mytest.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by weixi on 2017/6/26.
 */

public class SnapHelperTestActivity extends Activity {
    private RecyclerView mRecyclerView;
    private List<Integer> mImagesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snaphelper_test);
        initView();
    }

    private void initView() {
        mImagesList = new ArrayList<>();
        mImagesList.add(null);
        for (int i = 0; i < 3; i++) {
            mImagesList.add(R.drawable.ic_item1);
            mImagesList.add(R.drawable.ic_item2);
            mImagesList.add(R.drawable.ic_item3);
        }
        mImagesList.add(null);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_main);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mRecyclerView.setAdapter(new MySpaceAdapter());

//        LinearSnapHelper mLinearSnapHelper = new LinearSnapHelper();
//        mLinearSnapHelper.attachToRecyclerView(mRecyclerView);

        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(mRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(view.getContext(), "position" + position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));

//        MySnapHelper mMySnapHelper = new MySnapHelper();
//        mMySnapHelper.attachToRecyclerView(mRecyclerView);

        new LinearSnapHelper().attachToRecyclerView(mRecyclerView);
    }


    class MySpaceAdapter extends RecyclerView.Adapter {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == 1) {
                return new SpaceViewHolder(new View(SnapHelperTestActivity.this));
            } else {
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_card, parent, false);
                return new NormalViewHolder(itemView);
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof NormalViewHolder) {
                //设置正常条目，第一条跟最后一条的占位图不用特殊设置
                ((NormalViewHolder) holder).image.setImageResource(mImagesList.get(position));
            }
        }

        @Override
        public int getItemCount() {
            return mImagesList.size();
        }

        @Override
        public int getItemViewType(int position) {
            return (position == 0 || position == mImagesList.size() - 1) ? 1 : 0;
        }
    }

    class NormalViewHolder extends RecyclerView.ViewHolder {
        ImageView image;

        public NormalViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.imageView);
        }
    }

    class SpaceViewHolder extends RecyclerView.ViewHolder {
        public SpaceViewHolder(View itemView) {
            super(itemView);
            //250是每一条的宽度
            int width = (int) ((getResources().getDisplayMetrics().widthPixels -
                    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 250, getResources().getDisplayMetrics())) / 2);
            itemView.setLayoutParams(new ViewGroup.LayoutParams(width, ViewGroup.LayoutParams.MATCH_PARENT));
        }
    }
}
