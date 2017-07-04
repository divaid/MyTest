package com.david.mytest.test.snaphelpertest;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.david.mytest.R;

import java.util.List;

/**
 * Created by hiwhitley on 2016/9/4.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ItemViewHolder> {
    private List<Integer> mImages;

    public MyAdapter(List<Integer> images) {
        this.mImages = images;
    }


    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_card, parent, false);
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, final int position) {
        holder.mImageView.setImageResource(mImages.get(position));
    }

    @Override
    public int getItemCount() {
        return mImages.size();
    }

    @Override
    public int getItemViewType(int position) {//设置第一个跟最后一个为占位view
        return position == 0 || position == mImages.size() - 1 ? 1 : 0;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        private ImageView mImageView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.imageView);
        }
    }
    class SpaceViewHolder extends RecyclerView.ViewHolder{

        public SpaceViewHolder(View itemView) {
            super(itemView);
//            itemView.setLayoutParams(new ViewGroup.LayoutParams());
        }
    }
}
