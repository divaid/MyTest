package com.david.mytest.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.david.mytest.activity.main.MainActivity;
import com.david.mytest.R;
import com.david.mytest.activity.mine.ActivityTestMyWidget;
import com.david.mytest.activity.mine.Android6NewWidget;
import com.david.mytest.activity.mine.AnimateTestActivity;
import com.david.mytest.activity.mine.DownLoadActivity;
import com.david.mytest.activity.mine.DragLayoutTestActivity;
import com.david.mytest.activity.mine.PopupTestActivity;
import com.david.mytest.activity.mine.RxJavaTestActivity;
import com.david.mytest.activity.mine.SettingsActivity;
import com.david.mytest.activity.mine.AllCheckActivity;
import com.david.mytest.activity.mine.SingleCheckActivity;
import com.david.mytest.activity.mine.newwidget.TestFragmentActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by david on 2016/6/29.
 * <p/>
 * 侧拉菜单界面
 */
public class LeftDrawerFragment extends BaseFragment {

    @BindView(R.id.settings)
    LinearLayout mSettings;
    @BindView(R.id.tv_popupwindow)
    TextView tvPopupwindow;
    @BindView(R.id.tv_download)
    TextView tvDownload;
    @BindView(R.id.tv_test)
    TextView tvTest;
    @BindView(R.id.tv_test1)
    TextView tvTest1;
    @BindView(R.id.tv_activity_anim)
    TextView tvActivityAnim;
    @BindView(R.id.tv_activity_new_widget)
    TextView tvNewWidget;
    @BindView(R.id.my_widget)
    TextView tvMyWidget;
    @BindView(R.id.tv_activity_RxJava)
    TextView tvActivityRxJava;
    private MainActivity mainActivity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_left_menu, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {
        mainActivity = (MainActivity) getActivity();
    }

    @OnClick({R.id.tv_drag,R.id.tv_activity_animtest, R.id.my_widget, R.id.settings, R.id.tv_popupwindow, R.id.tv_download, R.id.tv_test, R.id.tv_activity_anim, R.id.tv_test1, R.id.tv_activity_new_widget,R.id.tv_activity_RxJava})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.settings:     //设置页面
                openActivity(getContext(), SettingsActivity.class);
                mainActivity.drawerLayout.closeDrawer(Gravity.LEFT);    //关闭侧滑菜单
                break;
            case R.id.tv_drag:     //设置页面
                openActivity(getContext(), DragLayoutTestActivity.class);
                mainActivity.drawerLayout.closeDrawer(Gravity.LEFT);    //关闭侧滑菜单
                break;
            case R.id.tv_popupwindow:   //PopupWindow测试页面
                openActivity(getContext(), PopupTestActivity.class);
                mainActivity.drawerLayout.closeDrawer(Gravity.LEFT);
                break;
            case R.id.tv_download:  //断点续传下载
                openActivity(getContext(), DownLoadActivity.class);
                mainActivity.drawerLayout.closeDrawer(Gravity.LEFT);
                break;

            case R.id.tv_test:  //ListView实现全选
                openActivity(getContext(), AllCheckActivity.class);
                mainActivity.drawerLayout.closeDrawer(Gravity.LEFT);
                break;

            case R.id.tv_test1:     //ListView实现单选
                openActivity(getContext(), SingleCheckActivity.class);
                mainActivity.drawerLayout.closeDrawer(Gravity.LEFT);
                break;

            case R.id.tv_activity_anim:     //Fragment测试
                openActivity(getContext(), TestFragmentActivity.class);
                mainActivity.drawerLayout.closeDrawer(Gravity.LEFT);
                break;

            case R.id.tv_activity_new_widget:   //Android6.0新控件测试
                openActivity(getContext(), Android6NewWidget.class);
                mainActivity.drawerLayout.closeDrawer(Gravity.LEFT);
                break;
            case R.id.my_widget:
                openActivity(getContext(), ActivityTestMyWidget.class);
                mainActivity.drawerLayout.closeDrawer(Gravity.LEFT);
                break;
            case R.id.tv_activity_RxJava:   //RxJava测试
                openActivity(getContext(),RxJavaTestActivity.class);
                mainActivity.drawerLayout.closeDrawer(Gravity.LEFT);
                break;
            case R.id.tv_activity_animtest:
                openActivity(getContext(),AnimateTestActivity.class);
                mainActivity.drawerLayout.closeDrawer(Gravity.LEFT);
                break;

        }
    }


    public void openActivity(Context context, Class clazz) {
        Intent intent = new Intent(context, clazz);
        startActivity(intent);
    }
}
