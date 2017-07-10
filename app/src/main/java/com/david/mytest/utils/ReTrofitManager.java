package com.david.mytest.utils;

import android.app.Activity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.david.mytest.request.OnRequestListener;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by weixi on 2017/7/7.
 */

public class ReTrofitManager {
    /**
     * 没有参数的请求
     * @param baseUrl 网址
     * @param onRequestListener 请求回调
     */
    public static void getResponse(String baseUrl, final OnRequestListener onRequestListener){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        com.david.mytest.requestApi.RequestApi requestApi = retrofit.create(com.david.mytest.requestApi.RequestApi.class);
        requestApi.getMsgs()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onRequestListener, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        onRequestListener.onError(throwable);
                    }
                });
    }


    /**
     * 使用Glide加载图片，TODO 开发时添加相应的重载方法
     */
    public static void disPlayImage(Activity activity, String url, ImageView view, int res, int errorRes){
        Glide.with(activity).
                load(url).centerCrop().
                placeholder(res).
                error(errorRes).
                into(view);
    }
}
