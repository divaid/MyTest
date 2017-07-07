package com.david.mytest.test.banner;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.david.mytest.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by weixi on 2017/6/30.
 */
public class RecyclerBanner extends FrameLayout {
    RecyclerView mRecyclerView;
    LinearLayout mDotContainer;
    GradientDrawable defaultDrawable, selectedDrawable;

    RecyclerAdapter mAdapter;
    OnPagerClickListener mPageClickListener;
    private List<BannerEntity> mData = new ArrayList<>();

    int mDotSize, startX, startY, currentIndex;
    boolean isPlaying;

    public interface OnPagerClickListener {

        void onClick(BannerEntity entity);
    }

    public interface BannerEntity {
        String getUrl();
    }

    private Runnable playTask = new Runnable() {

        @Override
        public void run() {
            currentIndex = currentIndex >= mData.size() - 1 ? 0 : ++currentIndex;
            if (currentIndex >= mData.size()) {
                currentIndex = currentIndex % mData.size();
            }
            mRecyclerView.smoothScrollToPosition(currentIndex);
            changePoint();
            postDelayed(this, 3000);
        }
    };

    public RecyclerBanner(Context context) {
        this(context, null);
    }

    public RecyclerBanner(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RecyclerBanner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mDotSize = (int) (6 * context.getResources().getDisplayMetrics().density + 0.5f);
        defaultDrawable = new GradientDrawable();
        defaultDrawable.setSize(mDotSize, mDotSize);
        defaultDrawable.setCornerRadius(mDotSize);
        defaultDrawable.setColor(0xffffffff);
        selectedDrawable = new GradientDrawable();
        selectedDrawable.setSize(mDotSize, mDotSize);
        selectedDrawable.setCornerRadius(mDotSize);
        selectedDrawable.setColor(0xff0094ff);

        LayoutParams vpLayoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mDotContainer = new LinearLayout(context);
        mDotContainer.setOrientation(LinearLayout.HORIZONTAL);
        LayoutParams linearLayoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mDotContainer.setGravity(Gravity.CENTER);
        mDotContainer.setPadding(mDotSize * 2, mDotSize * 2, mDotSize * 2, mDotSize * 2);
        linearLayoutParams.gravity = Gravity.BOTTOM;

        mRecyclerView = new RecyclerView(context);
        new PagerSnapHelper().attachToRecyclerView(mRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        mAdapter = new RecyclerAdapter();
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
//                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                int first = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
                int last = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();
                int current;
                if (first==0){
                    current = (int) Math.round((first + last) / 2.0 - 0.1);
                } else {
                    current = (int) Math.round((first + last) / 2.0);
                }
                if (currentIndex != current) {
                    currentIndex = current;
                    changePoint();
                }
//                }
            }
        });

        addView(mRecyclerView, vpLayoutParams);
        addView(mDotContainer, linearLayoutParams);


        /*
        //初始化比例
        TypedArray a = getContext().obtainStyledAttributes(attrs,
                R.styleable.RatioImageView);
        mIsWidthFitDrawableSizeRatio = a.getBoolean(R.styleable.RatioImageView_is_width_fix_drawable_size_ratio,
                mIsWidthFitDrawableSizeRatio);
        mIsHeightFitDrawableSizeRatio = a.getBoolean(R.styleable.RatioImageView_is_height_fix_drawable_size_ratio,
                mIsHeightFitDrawableSizeRatio);
        mHeightRatio = a.getFloat(
                R.styleable.RatioImageView_height_to_width_ratio, mHeightRatio);
        mWidthRatio = a.getFloat(
                R.styleable.RatioImageView_width_to_height_ratio, mWidthRatio);
        a.recycle();*/
    }

    public void setOnPagerClickListener(OnPagerClickListener onPagerClickListener) {
        this.mPageClickListener = onPagerClickListener;
    }

    public synchronized void setPlaying(boolean playing) {
        if (!isPlaying && playing && mAdapter != null && mAdapter.getItemCount() > 2) {
            postDelayed(playTask, 3000);
            isPlaying = true;
        } else if (isPlaying && !playing) {
            removeCallbacks(playTask);
            isPlaying = false;
        }
    }

    public int setDatas(List<? extends BannerEntity> datas) {
        setPlaying(false);
        this.mData.clear();
        mDotContainer.removeAllViews();
        if (mData != null) {
            this.mData.addAll(datas);
        }
        if (this.mData.size() > 1) {
            currentIndex = 0;
            mAdapter.notifyDataSetChanged();
            mRecyclerView.scrollToPosition(currentIndex);
            for (int i = 0; i < this.mData.size(); i++) {
                ImageView img = new ImageView(getContext());
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                lp.leftMargin = mDotSize / 2;
                lp.rightMargin = mDotSize / 2;
                img.setImageDrawable(i == 0 ? selectedDrawable : defaultDrawable);
                mDotContainer.addView(img, lp);
            }
            setPlaying(true);
        } else {
            currentIndex = 0;
            mAdapter.notifyDataSetChanged();
        }
        return this.mData.size();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = (int) ev.getX();
                startY = (int) ev.getY();
                getParent().requestDisallowInterceptTouchEvent(true);
                setPlaying(false);
                break;
            case MotionEvent.ACTION_MOVE:
                int moveX = (int) ev.getX();
                int moveY = (int) ev.getY();
                int disX = moveX - startX;
                int disY = moveY - startY;
                getParent().requestDisallowInterceptTouchEvent(2 * Math.abs(disX) > Math.abs(disY));
                setPlaying(false);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                setPlaying(true);
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        setPlaying(true);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        setPlaying(false);
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        if (visibility == View.GONE) {
            //页面不可见时停止轮播
            setPlaying(false);
        } else if (visibility == View.VISIBLE) {
            // 恢复可见时开始轮播
            setPlaying(true);
        }
        super.onWindowVisibilityChanged(visibility);
    }

    /**
     * height width ratio
     */
    private float ratio = 0.5f;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //获取宽度的模式和尺寸
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        //获取高度的模式和尺寸
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);


        //宽确定，高不确定
        if (widthMode == MeasureSpec.EXACTLY && heightMode != MeasureSpec.EXACTLY && ratio != 0) {
            heightSize = (int) (widthSize * ratio + 0.5f);//根据宽度和比例计算高度
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.EXACTLY);
        } else if (widthMode != MeasureSpec.EXACTLY && heightMode == MeasureSpec.EXACTLY & ratio != 0) {
            widthSize = (int) (heightSize / ratio + 0.5f);
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.EXACTLY);
        }

        //必须调用下面的两个方法之一完成onMeasure方法的重写，否则会报错
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    public void onDestroy() {
        setPlaying(false);
        removeCallbacks(playTask);
        playTask = null;

        mRecyclerView.setAdapter(null);
        mRecyclerView = null;
        mData.clear();
        mData = null;
        mAdapter = null;
    }

    // 内置适配器
    private class RecyclerAdapter extends RecyclerView.Adapter implements OnClickListener {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ImageView img = new ImageView(parent.getContext());
            RecyclerView.LayoutParams l = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            img.setScaleType(ImageView.ScaleType.CENTER_CROP);
            img.setLayoutParams(l);
            img.setId(R.id.icon);
            img.setOnClickListener(this);
            return new RecyclerView.ViewHolder(img) {
            };
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ImageView img = (ImageView) holder.itemView.findViewById(R.id.icon);
            Glide.with(getContext()).load(mData.get(position % mData.size()).getUrl()).placeholder(R.mipmap.ic_launcher).error(R.drawable.arrow_left).into(img);
        }

        @Override
        public int getItemCount() {
            return mData == null ? 0 : mData.size();
        }

        @Override
        public void onClick(View v) {
            if (mPageClickListener != null) {
                mPageClickListener.onClick(mData.get(currentIndex));// TODO: 2017/7/6 返回位置或者广告链接
            }
        }
    }

    private class PagerSnapHelper extends LinearSnapHelper {

        @Override
        public int findTargetSnapPosition(RecyclerView.LayoutManager layoutManager, int velocityX, int velocityY) {
            int targetPos = super.findTargetSnapPosition(layoutManager, velocityX, velocityY);
            final View currentView = findSnapView(layoutManager);
            if (targetPos != RecyclerView.NO_POSITION && currentView != null) {
                int currentPostion = layoutManager.getPosition(currentView);
                int first = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
                int last = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                currentPostion = targetPos < currentPostion ? last : (targetPos > currentPostion ? first : currentPostion);
                targetPos = targetPos < currentPostion ? currentPostion - 1 : (targetPos > currentPostion ? currentPostion + 1 : currentPostion);
            }
            return targetPos;
        }
    }

    private void changePoint() {
        if (mDotContainer != null && mDotContainer.getChildCount() > 0) {
            for (int i = 0; i < mDotContainer.getChildCount(); i++) {
                ((ImageView) mDotContainer.getChildAt(i)).setImageDrawable(i == currentIndex % mData.size() ? selectedDrawable : defaultDrawable);
            }
        }
    }
}
