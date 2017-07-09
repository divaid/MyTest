package com.david.mytest.utils;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

import com.david.mytest.application.BaseApplication;

/**
 * Created by wb-cuiweixing on 2015/9/22.
 */
public class ToastUtil {

    private static Handler mHandler = new Handler();

    private static Toast mToast = null;

    private static Runnable r = new Runnable() {
        public void run() {
            mToast.cancel();
        }
    };

    public static Integer SUBMIT_SUCCESS = 1;
    public static Integer SUBMIT_FAILURE = 2;

    public static void showToast(Context mContext, String text) {
        if (mContext == null) {
            return;
        }
        mHandler.removeCallbacks(r);
        if (mToast != null)
            mToast.setText(text);
        else
            mToast = Toast.makeText(mContext, text, Toast.LENGTH_SHORT);
        mToast.show();
    }

    public static void showLong(Context mContext, String msg) {
        showToast(mContext, msg);
    }

    public static void showLong(String msg) {
        showToast(BaseApplication.mApplication, msg);
    }

    /**
     * 日常损耗提交之后展示成功失败的吐司
     *
     * @param type 类型：1.成功 ，2.失败
     */
    public static void showSubmitDoneToast(int type) {
//        if (type == 1) {
//            showImageToast(R.drawable.icon_subsuc, "提交完成");
//        } else {
//            showImageToast(R.drawable.icon_subfail, "提交失败，请重新提交！");
//        }
    }

    /**
     * 显示img+text的吐司
     * @param imgId
     * @param title
     */
    public static void showImageToast(int imgId, String title) {
//        View view = LayoutInflater.from(RFApplication.getInstance()).inflate(R.layout.widget_daily_loss_suc, null);
//        Toast toast = new Toast(RFApplication.getInstance());
//        TextView text = (TextView) view.findViewById(R.id.finish_dialog_text);
//        ImageView img = (ImageView) view.findViewById(R.id.finish_dialog_img);
//        toast.setGravity(Gravity.FILL, 0, 0);
//        text.setText(title);
//        img.setImageResource(imgId);
//        toast.setView(view);
//        toast.show();
    }
}
