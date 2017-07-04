package com.david.mytest.test.dragrecyclerview.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.david.mytest.R;


/**
 * Created by david on 2016/4/12.
 */
public class MainFragment extends Fragment implements View.OnClickListener{

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_drag_recyeler,container,false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.list).setOnClickListener(this);
        view.findViewById(R.id.grid).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        ((View.OnClickListener)getActivity()).onClick(v);
    }
}
