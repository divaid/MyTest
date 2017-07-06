package com.david.mytest.data;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.david.mytest.R;
import com.david.mytest.activity.DetailActivity;
import com.david.mytest.requestBean.NewsMsgBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.david.mytest.activity.DetailActivity.NEWS_ID;

/**
 * Created by david on 2016/7/8.
 */
public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> implements View.OnClickListener{

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_NORMAL = 1;
    private Context mContext;
    private List<com.david.mytest.requestBean.NewsMsgBean.StoriesBean> mData;
    private LayoutInflater mLayoutInflater;
    private View mHeaderView;


    public NewsAdapter(Context context, List<com.david.mytest.requestBean.NewsMsgBean.StoriesBean> data) {
        this.mContext = context;
        this.mData = data;
        this.mLayoutInflater = LayoutInflater.from(context);
    }

    /**
     * 为RecycleView添加头布局
     *
     * @param headerView 添加的头布局
     */
    public void addHeaderView(View headerView) {
        this.mHeaderView = headerView;
    }

    @Override
    public int getItemCount() {
        return mHeaderView == null ? mData.size() : mData.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (mHeaderView == null)
            return TYPE_NORMAL;
        if (position == 0)
            return TYPE_HEADER;
        return TYPE_NORMAL;
    }

    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mHeaderView != null && viewType == TYPE_HEADER)
            return new NewsViewHolder(mHeaderView);
        View view = mLayoutInflater.inflate(R.layout.item_news, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NewsViewHolder holder, int position) {

        if (getItemViewType(position) == TYPE_HEADER) return;
        int realPosition = getRealPosition(holder);
        final com.david.mytest.requestBean.NewsMsgBean.StoriesBean bean = mData.get(realPosition);
        holder.tv_content.setText(bean.getTitle());
        com.david.mytest.utils.OkHttpManager.displayImage(bean.getImages().get(0), holder.iv_img);
        //为CardView添加点击事件,来替代RecycleView的item点击事件
        holder.item_card.setTag(position);
        holder.item_card.setOnClickListener(this);
    }

    /**
     * 判断是否添加了头布局后,得到当前真实的位置
     */
    int getRealPosition(RecyclerView.ViewHolder holder) {
        int position = holder.getLayoutPosition();
        return mHeaderView == null ? position : position - 1;
    }

    @Override
    public void onClick(View v) {
        NewsMsgBean.StoriesBean bean = mData.get((Integer) v.getTag());
        Intent intent = new Intent(mContext, DetailActivity.class);
        intent.putExtra(NEWS_ID,bean.getId());
        mContext.startActivity(intent);
    }

    class NewsViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_content)
        TextView tv_content;
        @BindView(R.id.iv_img)
        ImageView iv_img;
        @BindView(R.id.item_card)
        CardView item_card;

        NewsViewHolder(View itemView) {
            super(itemView);
            if (itemView == mHeaderView)
                return;
            ButterKnife.bind(this, itemView);
        }
    }
}
