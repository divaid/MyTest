package com.david.mytest.test.pulltorefreshtest.refresh;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;

/**
 * Created by wb-cuiweixing on 2017/6/12.
 */

public abstract class PullToRefreshBase<T extends View> extends FrameLayout {
    public static final int SMOOTH_SCROLL_DURATION_MS = 200;

    public static final byte PULL_MODE_DISABLED = 0;
    public static final byte PULL_MODE_FROM_START = 1;
    public static final byte PULL_MODE_FROM_END = 2;
    public static final byte PULL_MODE_FROM_BOTH = 3;

    public static final byte PULL_STATE_IDEL = 0;
    public static final byte PULL_STATE_PULL_TO_REFRESH = 1;
    public static final byte PULL_STATE_RELEASE_TO_REFRESH = 2;
    public static final byte PULL_STATE_REFRESHING = 3;

    static final float FRICTION = 2.0f;//用于减速

    T mRefreshableView;

    private OnPullRefreshListener mRefreshListener;
    private OnPullRefreshWithDerictionListener mRefreshWithDerictionListener;

    private byte mMode = PULL_MODE_FROM_BOTH;
    private byte mCurrentMode;

    private boolean mIsBeingDragged = false;

    private byte mState = PULL_STATE_IDEL;

    private LoadingLayout mHeaderLayout;
    private LoadingLayout mFooterLayout;

    private float mInitialY;
    private float mInitialX;

    public PullToRefreshBase(Context context) {
        super(context);
        initData(context, null);
    }

    public PullToRefreshBase(Context context, AttributeSet attrs) {
        super(context, attrs);
        initData(context, attrs);

    }

    public PullToRefreshBase(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initData(context, attrs);

    }

    private void initData(Context context, AttributeSet attrs) {
        mRefreshableView = createRefreshableView(context, attrs);
        addView(mRefreshableView);


        //初始化刷新布局
        mHeaderLayout = new LoadingLayout(getContext());
        mFooterLayout = new LoadingLayout(getContext());
        addView(mHeaderLayout);
        addView(mFooterLayout);
        post(new Runnable() {
            @Override
            public void run() {
                LayoutParams params = (LayoutParams) mHeaderLayout.getLayoutParams();
                params.setMargins(0, -mHeaderLayout.getHeight(), 0, 0);
                LayoutParams params1 = (LayoutParams) mFooterLayout.getLayoutParams();
                params1.setMargins(0, 0, 0, -mFooterLayout.getHeight());
                params1.gravity = Gravity.BOTTOM;
                addViewInLayout(mHeaderLayout, 0, mHeaderLayout.getLayoutParams());
                addViewInLayout(mFooterLayout, getChildCount(), mFooterLayout.getLayoutParams());
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (!isReadyPull() || mMode == PULL_MODE_DISABLED) {
            return false;
        }

        if (isRefreshing()) {
            return true;
        }

        int action = event.getAction();
        if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
            return false;
        }

        if (action != MotionEvent.ACTION_DOWN && mIsBeingDragged) {
            return true;
        }


        if (action == MotionEvent.ACTION_MOVE) {
            float dis = mInitialY - event.getY() / FRICTION;
            if (dis < 0 && (!(mMode == PULL_MODE_FROM_BOTH || mMode == PULL_MODE_FROM_START) || !isReadyForPullStart()) ||
                    (dis > 0 && (!(mMode == PULL_MODE_FROM_BOTH || mMode == PULL_MODE_FROM_END) || !isReadyForPullEnd()))) {
                return false;
            }


            if (mIsBeingDragged || Math.abs(mInitialY - event.getY() / FRICTION) > (Math.abs(mInitialX - event.getX() / FRICTION))) {
                if (!mIsBeingDragged)
                    mIsBeingDragged = true;
                return true;
            }
        } else if (action == MotionEvent.ACTION_DOWN) {
            mIsBeingDragged = false;
            //+getscrollY是在回滑到的位置的基础上再滑动
            mInitialY = event.getY() / FRICTION + getScrollY();
            mInitialX = event.getX() / FRICTION;
            //移除回弹任务防止未恢复时按下继续执行
            if (mCurrentSmoothScrollRunnable != null) {
                mCurrentSmoothScrollRunnable.stop();
            }
        }
        return mIsBeingDragged;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isReadyPull() || mMode == PULL_MODE_DISABLED) {
            return false;
        }
        if (isRefreshing()) {
            return true;
        }

        float dis = mInitialY - event.getY() / FRICTION;
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            if (mIsBeingDragged || Math.abs(mInitialY - event.getY() / FRICTION) > (Math.abs(mInitialX - event.getX() / FRICTION))) {//防止子控件没有设置事件导致没有move事件传递
                if (!mIsBeingDragged) {
                    mIsBeingDragged = true;
                }
                scrollTo(0, (int) (dis));

                dragging(dis);
                return true;
            }
        } else if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {

            if (mIsBeingDragged) {
                mIsBeingDragged = false;

                if (mState == PULL_STATE_RELEASE_TO_REFRESH && (mRefreshListener != null ||
                        mRefreshWithDerictionListener != null)) {
                    callRefreshListener();
                    if (mCurrentMode == PULL_MODE_FROM_END) {//from end
                        mState = PULL_STATE_REFRESHING;
                        smoothScrollTo(mFooterLayout.getHeight());

                        mFooterLayout.onRefreshing();
                    } else if (mCurrentMode == PULL_MODE_FROM_START) {//from start
                        mState = PULL_STATE_REFRESHING;
                        smoothScrollTo(-mHeaderLayout.getHeight());

                        mHeaderLayout.onRefreshing();
                    }
                } else {
                    //不能刷新时，结束刷新
                    onRefreshComplete();
                }
                return true;
            }
        }
        return true;
    }

    private void callRefreshListener() {
        if (null != mRefreshListener) {
            mRefreshListener.onRefresh();
        } else if (null != mRefreshWithDerictionListener) {
            if (mCurrentMode == PULL_MODE_FROM_START) {
                mRefreshWithDerictionListener.onRefreshFromStart();
            } else if (mCurrentMode == PULL_MODE_FROM_END) {
                mRefreshWithDerictionListener.onRefreshFromEnd();
            }
        }
    }

    /**
     * 接口返回之后的调用
     */
    public void onRefreshComplete() {
        smoothScrollTo(0);

        if (mCurrentMode == PULL_MODE_FROM_START) {
            mHeaderLayout.onRefreshCompleted();
        } else if (mCurrentMode == PULL_MODE_FROM_END) {
            mFooterLayout.onRefreshCompleted();
        }
        mCurrentMode = PULL_MODE_DISABLED;
        mState = PULL_STATE_IDEL;
    }

    /**
     * 设置不拖动的直接刷新
     */
    public void setRefreshing() {
        if (!isRefreshing() && isReadyForPullStart()) {
            mState = PULL_STATE_REFRESHING;
            mCurrentMode = PULL_MODE_FROM_START;

            scrollTo(0, mHeaderLayout.getHeight());

            if (mRefreshListener != null) {
                mRefreshListener.onRefresh();
            }
            if (mRefreshWithDerictionListener != null) {
                mRefreshWithDerictionListener.onRefreshFromStart();
            }
        }
    }

    /**
     * 设置不拖动的加载更多
     */
    public void setLoading() {
        if (!isRefreshing() && isReadyForPullEnd()) {
            mState = PULL_STATE_REFRESHING;
            mCurrentMode = PULL_MODE_FROM_END;

            scrollTo(0, -mFooterLayout.getHeight());

            if (mRefreshListener != null) {
                mRefreshListener.onRefresh();
            }
            if (mRefreshWithDerictionListener != null) {
                mRefreshWithDerictionListener.onRefreshFromEnd();
            }
        }
    }

    public boolean isRefreshing() {
        return mState == PULL_STATE_REFRESHING;
    }

    private void dragging(float dis) {
        if (dis > 0) {
            mCurrentMode = PULL_MODE_FROM_END;
            mFooterLayout.onPull(dis / mFooterLayout.getHeight());
            if (dis > mFooterLayout.getHeight()) {//from end
                mState = PULL_STATE_RELEASE_TO_REFRESH;
            } else {
                mState = PULL_STATE_PULL_TO_REFRESH;
            }
        } else if (dis < 0) {
            mCurrentMode = PULL_MODE_FROM_START;
            mHeaderLayout.onPull(dis / mFooterLayout.getHeight());

            if (dis < -mHeaderLayout.getHeight()) {//from start
                mState = PULL_STATE_RELEASE_TO_REFRESH;
            } else {
                mState = PULL_STATE_PULL_TO_REFRESH;
            }
        }
    }

    private SmoothScrollRunnable mCurrentSmoothScrollRunnable;
    private Interpolator mScrollAnimationInterpolator;

    private void smoothScrollTo(int newValue) {
        smoothScrollTo(newValue, null);
    }

    private void smoothScrollTo(int newScrollValue,
                                OnSmoothScrollFinishedListener listener) {
        smoothScrollTo(newScrollValue, SMOOTH_SCROLL_DURATION_MS, 0, listener);
    }

    //自定义回弹效果,类似于ListView的滑动效果
    private void smoothScrollTo(int newScrollValue, long duration, long delayMillis,
                                OnSmoothScrollFinishedListener listener) {
        if (null != mCurrentSmoothScrollRunnable) {
            mCurrentSmoothScrollRunnable.stop();
        }

        final int oldScrollValue;
//        switch (getPullToRefreshScrollDirection()) {
//            case HORIZONTAL:
//                oldScrollValue = getScrollX();
//                break;
//            case VERTICAL:
//            default:
        oldScrollValue = getScrollY();
//                break;
//        }

        if (oldScrollValue != newScrollValue) {
            if (null == mScrollAnimationInterpolator) {
                // Default interpolator is a Decelerate Interpolator
                mScrollAnimationInterpolator = new DecelerateInterpolator();
            }
            mCurrentSmoothScrollRunnable = new SmoothScrollRunnable(oldScrollValue, newScrollValue, duration, listener);

            if (delayMillis > 0) {
                postDelayed(mCurrentSmoothScrollRunnable, delayMillis);
            } else {
                post(mCurrentSmoothScrollRunnable);
            }
        }
    }

    final class SmoothScrollRunnable implements Runnable {
        private final Interpolator mInterpolator;
        private final int mScrollToY;
        private final int mScrollFromY;
        private final long mDuration;
        private OnSmoothScrollFinishedListener mListener;

        private boolean mContinueRunning = true;
        private long mStartTime = -1;
        private int mCurrentY = -1;

        SmoothScrollRunnable(int fromY, int toY, long duration, OnSmoothScrollFinishedListener listener) {
            mScrollFromY = fromY;
            mScrollToY = toY;
            mInterpolator = new DecelerateInterpolator();
            mDuration = duration;
            mListener = listener;
        }

        @Override
        public void run() {

            /**
             * Only set mStartTime if this is the first time we're starting,
             * else actually calculate the Y delta
             */
            if (mStartTime == -1) {
                mStartTime = System.currentTimeMillis();
            } else {

                /**
                 * We do do all calculations in long to reduce software float
                 * calculations. We use 1000 as it gives us good accuracy and
                 * small rounding errors
                 */
                long normalizedTime = (1000 * (System.currentTimeMillis() - mStartTime)) / mDuration;
                normalizedTime = Math.max(Math.min(normalizedTime, 1000), 0);

                final int deltaY = Math.round((mScrollFromY - mScrollToY)
                        * mInterpolator.getInterpolation(normalizedTime / 1000f));
                mCurrentY = mScrollFromY - deltaY;
                scrollTo(0, mCurrentY);
            }

            // If we're not at the target Y, keep going...
            if (mContinueRunning && mScrollToY != mCurrentY) {
                postDelayed(this, 16);
            } else {
                if (null != mListener) {
                    mListener.onSmoothScrollFinished();
                }
            }
        }

        void stop() {
            mContinueRunning = false;
            removeCallbacks(this);
        }
    }

    private boolean isReadyPull() {
        switch (mMode) {
            case PULL_MODE_FROM_END:
                return isReadyForPullEnd();
            case PULL_MODE_FROM_START:
                return isReadyForPullStart();
            case PULL_MODE_FROM_BOTH:
                return isReadyForPullStart() || isReadyForPullEnd();
            default:
                return false;
        }
    }

    public T getRefreshableView(){
        return mRefreshableView;
    }

    public void setOnPullRefreshListener(OnPullRefreshListener mRefreshListener) {
        this.mRefreshListener = mRefreshListener;
    }

    public void setOnPullRefreshWithDerictionListener(OnPullRefreshWithDerictionListener mRefreshWithDerictionListener) {
        this.mRefreshWithDerictionListener = mRefreshWithDerictionListener;
    }

    public abstract boolean isReadyForPullStart();

    public abstract boolean isReadyForPullEnd();

    public abstract T createRefreshableView(Context context, AttributeSet attrs);

    public interface OnPullRefreshListener {
        void onRefresh();
    }

    public interface OnPullRefreshWithDerictionListener {
        void onRefreshFromStart();

        void onRefreshFromEnd();
    }

    static interface OnSmoothScrollFinishedListener {
        void onSmoothScrollFinished();
    }
}
