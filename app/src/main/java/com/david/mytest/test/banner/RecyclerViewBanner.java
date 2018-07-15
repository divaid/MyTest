package com.david.mytest.test.banner;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
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
import android.widget.TextView;

import com.david.mytest.R;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

/**
 * recommend this one, experience is better
 * Created by weixi on 2017/7/21.
 */
public class RecyclerViewBanner extends FrameLayout {
    public static final short NAV_DOT = 0;
    public static final short NAV_BOTTOM_BAR = 1;
    public static final short NAV_NUMBER = 2;

    private RecyclerView recyclerView;
    private LinearLayout linearLayout;
    private GradientDrawable defaultDrawable,selectedDrawable;
    private TextView mNavNumber;

    private RecyclerAdapter adapter;
    private OnPagerClickListener onPagerClickListener;
    private List<BannerEntity> datas = new ArrayList<>();

    private int size,startX, startY,currentIndex;
    private boolean isPlaying;

    private int mNavType;

    public interface OnPagerClickListener{

        void onClick(BannerEntity entity);
    }

    public interface BannerEntity{
        String getUrl();
    }

    private Handler handler = new Handler();

    private Runnable playTask = new  Runnable(){

        @Override
        public void run() {
            recyclerView.smoothScrollToPosition(++currentIndex);
            changePoint();
            handler.postDelayed(this,3000);
        }
    };

    public RecyclerViewBanner(Context context) {
        this(context,null);
    }

    public RecyclerViewBanner(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public RecyclerViewBanner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        size = (int) (6 * context.getResources().getDisplayMetrics().density + 0.5f);
        initNavView();

        recyclerView = new RecyclerView(context);
        LayoutParams vpLayoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        LayoutParams linearLayoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        linearLayout.setGravity(Gravity.CENTER);
        linearLayout.setPadding(size * 2,size * 2,size * 2,size * 2);
        linearLayoutParams.gravity = Gravity.BOTTOM;
        addView(recyclerView,vpLayoutParams);
        addView(linearLayout,linearLayoutParams);

        new PagerSnapHelper().attachToRecyclerView(recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false));
        adapter = new RecyclerAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int first = ((LinearLayoutManager)recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
                int last = ((LinearLayoutManager)recyclerView.getLayoutManager()).findLastVisibleItemPosition();
                if(currentIndex != (first + last) / 2){
                    currentIndex = (first + last) / 2;
                    changePoint();
                }
            }
        });
    }

    private void initNavView(int type){
        if (type == NAV_DOT) {
            if (linearLayout != null) {
                linearLayout.setVisibility(VISIBLE);
            }
            if (mNavNumber != null && mNavNumber.getVisibility() == VISIBLE) {
                mNavNumber.setVisibility(GONE);
            }
            defaultDrawable = new GradientDrawable();
            defaultDrawable.setSize(size, size);
            defaultDrawable.setCornerRadius(size);
            defaultDrawable.setColor(0xffffffff);
            selectedDrawable = new GradientDrawable();
            selectedDrawable.setSize(size, size);
            selectedDrawable.setCornerRadius(size);
            selectedDrawable.setColor(0xff0094ff);
        } else if (type == NAV_BOTTOM_BAR) {
            if (linearLayout != null) {
                linearLayout.setVisibility(VISIBLE);
            }
            if (mNavNumber != null && mNavNumber.getVisibility() == VISIBLE) {
                mNavNumber.setVisibility(GONE);
            }
            int width = (getResources().getDisplayMetrics().widthPixels - 160) / (datas.size() == 0 ? 5 : datas.size());
            defaultDrawable = new GradientDrawable();
            defaultDrawable.setSize(width, size / 3);
            defaultDrawable.setCornerRadius(0);
            defaultDrawable.setColor(0xffffffff);
            selectedDrawable = new GradientDrawable();
            selectedDrawable.setSize(width, size / 3);
            selectedDrawable.setCornerRadius(0);
            selectedDrawable.setColor(0xff0094ff);
        } else if (type == NAV_NUMBER) {
            if (linearLayout != null) {
                linearLayout.setVisibility(GONE);
                linearLayout.removeAllViews();
            }
            if (mNavNumber != null) {
                mNavNumber.setVisibility(VISIBLE);
                return;
            }
            mNavNumber = new TextView(getContext());
            mNavNumber.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            int roundRadius = 20; // 8dp 圆角半径
            int strokeColor = Color.parseColor("#2E3135");//边框颜色
            int fillColor = Color.parseColor("#DFDFE0");//内部填充颜色
            GradientDrawable gd = new GradientDrawable();//创建drawable（动态创建shape）
            gd.setColor(fillColor);
            gd.setCornerRadius(roundRadius);
//            int strokeWidth = 5; // 3dp 边框宽度
//            gd.setStroke(strokeWidth, strokeColor);

            mNavNumber.setBackground(gd);
            mNavNumber.setGravity(Gravity.CENTER);
            mNavNumber.setPadding(15, 6, 15, 6);
            mNavNumber.setText("0/0");
            LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.RIGHT | Gravity.BOTTOM;
            params.bottomMargin = 30;
            params.rightMargin = 30;
            addView(mNavNumber, params);
        }
    }

    private void initNavView() {
        initNavView(mNavType);
    }

    public void setOnPagerClickListener(OnPagerClickListener onPagerClickListener) {
        this.onPagerClickListener = onPagerClickListener;
    }

    public synchronized void setPlaying(boolean playing){
        if(!isPlaying && playing && adapter != null && adapter.getItemCount() > 2){
            handler.postDelayed(playTask,3000);
            isPlaying = true;
        }else if(isPlaying && !playing){
            handler.removeCallbacksAndMessages(null);
            isPlaying = false;
        }
    }

    @SuppressLint("DefaultLocale")
    public void setData(List<? extends BannerEntity> data){
        if (datas.size() == 0 && mNavType == NAV_BOTTOM_BAR) {
            initNavView();
        }


        setPlaying(false);
        this.datas.clear();
        linearLayout.removeAllViews();
        if(data != null){
            this.datas.addAll(data);
        }
        if(this.datas.size() > 1){
            currentIndex = this.datas.size() * 10000;
            adapter.notifyDataSetChanged();
            recyclerView.scrollToPosition(currentIndex);
            if (mNavType != NAV_NUMBER) {
                for (int i = 0; i < this.datas.size(); i++) {
                    ImageView img = new ImageView(getContext());
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    lp.leftMargin = size / 2;
                    lp.rightMargin = size / 2;
                    img.setImageDrawable(i == 0 ? selectedDrawable : defaultDrawable);
                    linearLayout.addView(img, lp);
                }
            } else {
                mNavNumber.setText(String.format("1 / %d", datas.size()));
            }
            setPlaying(true);
        }else {
            currentIndex = 0;
            adapter.notifyDataSetChanged();
        }
        this.datas.size();
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
        if(visibility == View.GONE){
            // 停止轮播
            setPlaying(false);
        }else if(visibility == View.VISIBLE){
            // 开始轮播
            setPlaying(true);
        }
        super.onWindowVisibilityChanged(visibility);
    }

    /**
     * height width ratio
     */
    private float ratio = 0.5f;

    public void setRatio(float ratio) {
        this.ratio = ratio;
    }

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

    /**
     * set nav type
     * @param navType type of nav(dot bar or number)
     */
    public void setNavType(int navType) {
        if (navType != mNavType) {
            initNavView(navType);
            this.mNavType = navType;
        }
    }

    public void onDestroy() {
        setPlaying(false);
        removeCallbacks(playTask);
        playTask = null;

        recyclerView.setAdapter(null);
        recyclerView = null;
        datas.clear();
        datas = null;
        datas = null;
    }

    // 内置适配器
    private class RecyclerAdapter extends RecyclerView.Adapter implements OnClickListener {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            SimpleDraweeView img = new SimpleDraweeView(getContext());
            RecyclerView.LayoutParams l = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            img.setScaleType(ImageView.ScaleType.CENTER_CROP);
            img.setLayoutParams(l);
            img.setId(R.id.icon);
            img.setOnClickListener(this);
            return new ImageHolder(img);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ((ImageHolder) holder).img.setImageURI(datas.get(position % datas.size()).getUrl());
        }

        @Override
        public int getItemCount() {
            return datas == null ? 0 : datas.size() < 2 ? datas.size() : Integer.MAX_VALUE;
        }

        @Override
        public void onClick(View v) {
            if(onPagerClickListener != null){
                onPagerClickListener.onClick(datas.get(currentIndex % datas.size()));
            }
        }
    }
    private class ImageHolder extends RecyclerView.ViewHolder{
        private SimpleDraweeView img;
        public ImageHolder(View itemView) {
            super(itemView);
            img = (SimpleDraweeView) itemView;
        }
    }

    private class PagerSnapHelper extends LinearSnapHelper {

        @Override
        public int findTargetSnapPosition(RecyclerView.LayoutManager layoutManager, int velocityX, int velocityY) {
            int targetPos = super.findTargetSnapPosition(layoutManager, velocityX, velocityY);
            View currentView = findSnapView(layoutManager);
            if(targetPos != RecyclerView.NO_POSITION && currentView != null){
                int currentPosition = layoutManager.getPosition(currentView);
                currentPosition = targetPos < currentPosition ?
                        ((LinearLayoutManager)layoutManager).findLastVisibleItemPosition() : (targetPos > currentPosition ?
                        ((LinearLayoutManager)layoutManager).findFirstVisibleItemPosition() : currentPosition);
                targetPos = targetPos < currentPosition ?
                        currentPosition - 1 : (targetPos > currentPosition ?
                        currentPosition + 1 : currentPosition);
            }
            return targetPos;
        }
    }

    @SuppressLint("DefaultLocale")
    private void changePoint(){
        if(linearLayout != null && linearLayout.getChildCount() > 0){
            for (int i = 0; i < linearLayout.getChildCount(); ++i) {
                ((ImageView)linearLayout.getChildAt(i)).setImageDrawable(i == currentIndex % datas.size() ? selectedDrawable : defaultDrawable);
            }
        } else if (mNavType == NAV_NUMBER && mNavNumber != null) {
            mNavNumber.setText(String.format("%d / %d", currentIndex % datas.size() + 1, datas.size()));
        }
    }
}
