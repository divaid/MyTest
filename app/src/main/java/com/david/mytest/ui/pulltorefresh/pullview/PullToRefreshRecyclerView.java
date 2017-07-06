package com.david.mytest.ui.pulltorefresh.pullview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.david.mytest.R;

/**
 * Created by David on 2016/7/5.
 */

public class PullToRefreshRecyclerView extends PullToRefreshBase<RecyclerView> {
    public PullToRefreshRecyclerView(Context context) {
        super(context);
    }

    public PullToRefreshRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PullToRefreshRecyclerView(Context context, Mode mode) {
        super(context, mode);
    }

    @Override
    public Orientation getPullToRefreshScrollDirection() {
        return Orientation.VERTICAL;// TODO: 2016/7/5 添加横向的滑动
    }

    @Override
    protected RecyclerView createRefreshableView(Context context, AttributeSet attrs) {
        RecyclerView recyclerView =new RecyclerView(context, attrs);
        recyclerView.setId(R.id.recyclerview);
        return recyclerView;
    }

    @Override
    protected boolean isReadyForPullEnd() {
        return !mRefreshableView.canScrollVertically(1);//不能向上滑动时可以加载更多
    }

    @Override
    protected boolean isReadyForPullStart() {
        return !mRefreshableView.canScrollVertically(-1);//不能向下滑动时可以刷新
    }
}
