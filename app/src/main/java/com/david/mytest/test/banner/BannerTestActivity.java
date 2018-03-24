package com.david.mytest.test.banner;

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

import com.bumptech.glide.Glide;
import com.david.mytest.R;
import com.david.mytest.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by weixi on 2017/6/30.
 */

public class BannerTestActivity extends Activity {
    private RecyclerViewBanner mBanner;
    private RecyclerView mRecyclerView;//演示类似于盒马个人页滑动条

    private List<RecyclerViewBanner.BannerEntity> mImages;
    private List<String> mRecyclerData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner_test);
        initData();
        initView();
    }

    private void initData(){
        mImages = new ArrayList<>();
        mRecyclerData = new ArrayList<>();
        mRecyclerData.add(null);
        mRecyclerData.add("http://pic.58pic.com/58pic/12/46/13/03B58PICXxE.jpg");
        mRecyclerData.add("http://pic34.nipic.com/20131025/2531170_132447503000_2.jpg");
        mRecyclerData.add("http://pic.58pic.com/58pic/12/46/13/03B58PICXxE.jpg");
        mRecyclerData.add("http://pic34.nipic.com/20131025/2531170_132447503000_2.jpg");
        mRecyclerData.add(null);
        mImages.add(new MyImageUrl("http://pic.58pic.com/58pic/12/46/13/03B58PICXxE.jpg"));
        mImages.add(new MyImageUrl("http://www.jitu5.com/uploads/allimg/121120/260529-121120232T546.jpg"));
        mImages.add(new MyImageUrl("http://pic34.nipic.com/20131025/2531170_132447503000_2.jpg"));
        mImages.add(new MyImageUrl("http://img5.imgtn.bdimg.com/it/u=3462610901,3870573928&fm=206&gp=0.jpg"));
    }

    private void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.normal_recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(new MySpaceAdapter());

        new LinearSnapHelper().attachToRecyclerView(mRecyclerView);

        mBanner = (RecyclerViewBanner) findViewById(R.id.banner);
        mBanner.setDatas(mImages);
        mBanner.setOnPagerClickListener(new RecyclerViewBanner.OnPagerClickListener() {
            @Override
            public void onClick(RecyclerViewBanner.BannerEntity entity) {
                ToastUtil.showLong(entity.toString());
            }
        });
    }

    class MyImageUrl implements RecyclerViewBanner.BannerEntity{
        private String mImageUrl;

        public MyImageUrl(String mImageUrl) {
            this.mImageUrl = mImageUrl;
        }

        @Override
        public String getUrl() {
            return mImageUrl;
        }
    }


    /**
     * 实现类似盒马个人页上面的滑动条
     */
    class MySpaceAdapter extends RecyclerView.Adapter {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == 1) {
               return new SpaceViewHolder(new View(BannerTestActivity.this));
            } else {
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_card, parent, false);
                return new NormalViewHolder(itemView);
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof NormalViewHolder) {
                //设置正常条目，第一条跟最后一条的占位图不用特殊设置
                Glide.with(BannerTestActivity.this).load(mRecyclerData.get(position)).into(((NormalViewHolder) holder).image);
//                ((NormalViewHolder) holder).image.setImageResource(mImages.get(position).getUrl());
            }
        }

        @Override
        public int getItemCount() {
            return mRecyclerData.size();
        }

        @Override
        public int getItemViewType(int position) {
            return (position == 0 || position == mRecyclerData.size() - 1) ? 1 : 0;
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
