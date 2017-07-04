package com.david.mytest.test.day_6_27_autolinetest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.david.mytest.R;

public class AutoLineTestActivity extends AppCompatActivity {
    private Button btn_add;
    private BamAutoLineList bal_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autoline_test);

//        btn_add = (Button) findViewById(R.id.btn_add);
//        bal_list = (BamAutoLineList) findViewById(R.id.bal_list);

        // 点击事件
//        btn_add.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // 实例化View
//                View item = createView();
//                // 把View放到控件里去
//                bal_list.addView(item);
//            }
//        });

        ((MyViewGroup) findViewById(R.id.myviewgroup)).setOnViewClickListener(new MyViewGroup.OnViewClickListener() {
            @Override
            public void OnViewClick(int position) {
                Toast.makeText(AutoLineTestActivity.this, "you clicked "+ position, Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * 初始化一个布局
     *
     * @return
     */
    private View createView() {
        // 实例化一个View，以放到List里
        View item = getLayoutInflater().inflate(R.layout.item, null);
        // 设置View里的文本值
        ((TextView) item.findViewById(R.id.tv_item)).setText(randomText());

        // 动画，如果需要可以打开，不需要则关闭
        if (true) {
            Animation anim = new ScaleAnimation(0, 1, 1, 1);
            anim.setDuration(300);
            item.setAnimation(anim);
        }

        return item;
    }

    /**
     * 产生随机字符串，
     * 为了让每个条目的文本不一样，
     * 正常开发不需要此方法
     *
     * @return 带有随机数的文本
     */
    private String randomText() {
        StringBuffer str = new StringBuffer("条目");

        int count = (int) (Math.random() * 10);
        for (int i = 0; i < count; i++) {
            str.append("" + (int) (Math.random() * 10));
        }
        return str.toString();
    }

}
