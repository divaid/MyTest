package com.david.mytest.test.wheeltest;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.Snackbar;

import com.david.mytest.R;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by weixi on 2017/6/30.
 */

public class WheelTestActivity extends Activity {
    private String[] PLANETS = {"水星", "金星", "地球", "木星", "火星", "天王星" ,"海王星"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wheel);

        WheelView wv = (WheelView) findViewById(R.id.wheel_view_wv);
        wv.setOffset(2);//设置偏移量为2，每次显示五条
        wv.setData(Arrays.asList(PLANETS));
        wv.setSelection(1);
        wv.setOnWheelListener(new WheelView.OnWheelListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {

            }
        });

        final RecyclerWheelView view = (RecyclerWheelView) findViewById(R.id.recycler_wheel);
        ArrayList<String> data = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            data.add("item "+ i);
        }
        view.setmOnSelectListener(new RecyclerWheelView.OnSelectListener() {
            @Override
            public void onSelect(int position) {
                Snackbar.make(view, "select "+ position, Snackbar.LENGTH_LONG).show();
            }
        });
        view.setData(data);
    }
}
