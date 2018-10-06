package com.david.mytest.utils;

import android.app.Application;
import android.view.View;
import android.widget.Toast;

import com.david.mytest.application.BaseApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cuiweixing on 2018/7/25.
 *
 * when the message is already in list, then it will just toast one
 */

public class ToastUtils implements View.OnAttachStateChangeListener {
    private static ToastUtils toastUtils;
    private Toast toast;
    private final List<String> messages;
    private ToastUtils(){
        toast = Toast.makeText(BaseApplication.mApplication, "", Toast.LENGTH_LONG);
        toast.getView().addOnAttachStateChangeListener(this);
        messages = new ArrayList<>();
    }
    public static ToastUtils getInstance(){
        if (toastUtils == null) {
            synchronized (ToastUtils.class) {
                if (toastUtils == null) {
                    toastUtils = new ToastUtils();
                }
            }
        }
        return toastUtils;
    }

    public synchronized void showLongToast(String msg){
        if (toast.getDuration() != Toast.LENGTH_LONG) {
            toast.setDuration(Toast.LENGTH_LONG);
        }
        show(msg);
    }

    public synchronized void showShortToast(String msg){
        toast.setDuration(Toast.LENGTH_SHORT);
        show(msg);
    }

    private synchronized void show(String msg){
        synchronized (messages) {
            boolean showToast = false;
            if (messages.size() == 0)
                showToast = true;
            if (!messages.contains(msg)) {
                messages.add(0, msg);
            }
            if (showToast) {
                toast.setText(messages.get(messages.size() - 1));
                toast.show();
                toast.show();
            }
        }
    }


    private synchronized void removeMessage(){
        synchronized (messages){
            if (messages.size() > 0) {
                messages.remove(messages.size() - 1);
            }
        }
    }

    @Override
    public void onViewAttachedToWindow(View v) {

    }

    @Override
    public void onViewDetachedFromWindow(View v) {
        removeMessage();
        if (messages.size() > 0){
            toast.setText(messages.get(messages.size() - 1));
            toast.show();
        }
    }
}
