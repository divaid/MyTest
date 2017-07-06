package com.david.mytest.activity.mine.newwidget;

import android.support.v7.widget.Toolbar;
import android.widget.LinearLayout;

import com.david.mytest.R;
import com.david.mytest.activity.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CoordinatorLayoutActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.activity_coordinator_layout)
    LinearLayout activityCoordinatorLayout;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_coordinator_layout;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);

        toolbar.setTitle("CoordinatorLayout");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void initData() {

    }

}
