package com.david.mytest.test.dragrecyclerview.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.david.mytest.R;
import com.david.mytest.test.dragrecyclerview.adapter.RecyclerAdapter;
import com.david.mytest.test.dragrecyclerview.entity.Item;
import com.david.mytest.test.dragrecyclerview.helper.MyItemTouchCallback;
import com.david.mytest.test.dragrecyclerview.helper.OnRecyclerItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by david on 2016/4/12.
 */
public class MyListFragment extends Fragment{

    private List<Item> results = new ArrayList<Item>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        for (int i=0;i<3;i++){
            results.add(new Item(i*8+0,"美食", R.mipmap.ic_launcher));
            results.add(new Item(i*8+1,"电影", R.drawable.ic_home_black));
            results.add(new Item(i*8+2,"酒店", R.drawable.ic_settings_black));
            results.add(new Item(i*8+3,"KTV", R.drawable.ic_search));
            results.add(new Item(i*8+4,"外卖", R.drawable.ic_notifications));
            results.add(new Item(i*8+5,"彩票", R.drawable.ic_file_download_black));
            results.add(new Item(i*8+6,"电影", R.drawable.ic_favorite));
            results.add(new Item(i*8+7,"游戏", R.drawable.arrow_left));
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return new RecyclerView(container.getContext());
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final RecyclerAdapter adapter = new RecyclerAdapter(R.layout.item_list,results);

        RecyclerView recyclerView = (RecyclerView)view;
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        final ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new MyItemTouchCallback(adapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);

        recyclerView.addOnItemTouchListener(new OnRecyclerItemClickListener(recyclerView) {
            @Override
            public void onLongClick(RecyclerView.ViewHolder vh) {
//                if (vh.getLayoutPosition()!=results.size()-1) {
                    itemTouchHelper.startDrag(vh);
//                    VibratorUtil.Vibrate(getActivity(), 70);   //震动70ms
//                }
            }

            @Override
            public void onSwiped(int position) {
                adapter.onSwiped(position);//一般是list列表才能滑动删除
            }
        });
    }
}
