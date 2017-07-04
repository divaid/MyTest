package com.david.mytest.test.decor_6_15;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.david.mytest.R;

/**
 * Created by weixi on 2017/6/15.
 */

public class DecorTestActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decor_test);
        findViewById(R.id.tv_decor).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textView =new TextView(DecorTestActivity.this);
                textView.setText("test decorview");
                textView.setBackgroundColor(Color.RED);
                textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 300));
                ((ViewGroup) getWindow().getDecorView()).addView(textView);
            }
        });
    }
}
