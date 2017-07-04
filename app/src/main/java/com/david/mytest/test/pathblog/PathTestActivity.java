
package com.david.mytest.test.pathblog;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.ViewGroup.LayoutParams;

public class PathTestActivity extends Activity {

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case START_ANIM:
                    mView.startPathAnim(2000);
                    mHandler.sendEmptyMessageDelayed(START_ANIM, 3000);
                    break;

                default:
                    break;
            }
        };
    };
    private static final int START_ANIM = 0;
    private DynamicHeartView mView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mView = new DynamicHeartView(this);
        setContentView(mView, new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT));
        mHandler.sendEmptyMessage(START_ANIM);
    }

}
