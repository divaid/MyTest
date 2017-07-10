package com.david.mytest.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.david.mytest.R;
import com.david.mytest.activity.detail.DetailActivity;
import com.david.mytest.data.NewsAdapter;
import com.david.mytest.request.ErrorResponse;
import com.david.mytest.request.OnRequestListener;
import com.david.mytest.requestBean.NewsMsgBean;
import com.david.mytest.test.banner.RecyclerBanner;
import com.david.mytest.ui.pulltorefresh.pullview.PullToRefreshBase;
import com.david.mytest.ui.pulltorefresh.pullview.PullToRefreshRecyclerView;
import com.david.mytest.utils.ReTrofitManager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.david.mytest.activity.detail.DetailActivity.NEWS_ID;

/**
 * Created by david on 2016/6/22.
 */
public class MainFragment extends BaseFragment {


    private static final String TAG = "MainFragment";
//    @BindView(R.id.swipe_container)
//    SwipeRefreshLayout swipeContainer;


    @BindView(R.id.recycle_view)
    PullToRefreshRecyclerView mRefreshView;
    private RecyclerView recyclerView;
    private RecyclerBanner mHeader;

    private NewsMsgBean mNewsData;
    private NewsAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);
        return view;

    }

    @Override
    public void initView() {
        mRefreshView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<RecyclerView>() {
            @Override
            public void onRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                requestData();
            }
        });

        recyclerView = mRefreshView.getRefreshableView();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //这是RecycleView默认的item动画
        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        NewsAdapter adapter = new NewsAdapter(UiUtils.getContext(), mNewsData.getStories());
//        recycleView.setAdapter(adapter);


        //设置进度条的颜色主题，最多能设置四种 加载颜色是循环播放的，只要没有完成刷新就会一直循环，holo_blue_bright>holo_green_light>holo_orange_light>holo_red_light
//        swipeContainer.setColorSchemeColors(Color.BLUE,
//                Color.GREEN,
//                Color.YELLOW,
//                Color.RED);
//        // 设置手指在屏幕下拉多少距离会触发下拉刷新
//        swipeContainer.setDistanceToTriggerSync(300);
//        // 设定下拉圆圈的背景
//        swipeContainer.setProgressBackgroundColorSchemeColor(Color.WHITE);
//        // 设置圆圈的大小
//        swipeContainer.setSize(SwipeRefreshLayout.DEFAULT);
//        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                if (!swipeContainer.isRefreshing()) {
////                    isRefreshing = true;
//                    swipeContainer.setRefreshing(true); //显示刷新进度条
//                    PathMeasure pathMeasure;
//                    requestData();
//                }
//            }
//        });
    }

    @Override
    public void initData() {
        requestData();
    }

    private void requestData() {
        ReTrofitManager.getResponse("http://news-at.zhihu.com/api/4/", new OnRequestListener<NewsMsgBean>() {
            @Override
            public void onSuccess(NewsMsgBean o) {
                mRefreshView.onRefreshComplete();
                mNewsData = o;
                List<NewsMsgBean.TopStoriesBean> top_stories = mNewsData.getTop_stories();

                adapter = new NewsAdapter(getActivity(), mNewsData.getStories());
                recyclerView.setAdapter(adapter);

                setHeader(recyclerView, top_stories);
            }

            @Override
            public void onError(ErrorResponse o) {
                mRefreshView.onRefreshComplete();
                // TODO: 2017/7/7 展示错误页面
            }

            @Override
            public void onSystemError(ErrorResponse o) {
                mRefreshView.onRefreshComplete();

            }
        });

//        OkHttpManager.getAsyn("http://news-at.zhihu.com/api/4/news/latest", new OkHttpManager.ResultCallback<NewsMsgBean>() {
//
//            @Override
//            public void onError(Request request, Exception e) {
////                swipeContainer.setRefreshing(false);
//                mRefreshView.onRefreshComplete();
////                isRefreshing = false;
//                Log.d("TAG", e.getMessage());
//            }
//
//            @Override
//            public void onSuccess(NewsMsgBean response) {
////                swipeContainer.setRefreshing(false);    //隐藏刷新进度条
//                mRefreshView.onRefreshComplete();
////                isRefreshing = false;
//                Logger.d(response);
//                mNewsData = response;
//                List<NewsMsgBean.TopStoriesBean> top_stories = mNewsData.getTop_stories();
//
//                adapter = new NewsAdapter(getActivity(), mNewsData.getStories());
//                recyclerView.setAdapter(adapter);
//
//                setHeader(recyclerView, top_stories);
//            }
//
//        });
    }

    private void setHeader(RecyclerView recycleView, List<NewsMsgBean.TopStoriesBean> top_stories) {
        if (top_stories == null || top_stories.size() == 0) {
            return;
        }
        if (mHeader == null) {
            mHeader = (RecyclerBanner) LayoutInflater.from(getContext()).inflate(R.layout.widget_news_header, recycleView, false);
            mHeader.setOnPagerClickListener(new RecyclerBanner.OnPagerClickListener() {
                @Override
                public void onClick(RecyclerBanner.BannerEntity entity) {
                    Intent intent = new Intent(getActivity(), DetailActivity.class);
                    intent.putExtra(NEWS_ID, ((NewsMsgBean.TopStoriesBean) entity).getId());
                    startActivity(intent);
                }
            });
        }

        mHeader.setDatas(top_stories);

        adapter.addHeaderView(mHeader);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //移除消息
        if (mHeader != null) {
            mHeader.onDestroy();
        }
        recyclerView.removeAllViews();
        recyclerView = null;
        mRefreshView.removeAllViews();
        mRefreshView = null;
    }
}
