package com.david.mytest.request;

import com.david.mytest.utils.NetWorkUtils;

import io.reactivex.functions.Consumer;

/**
 * 对于Retrofit的回调分发
 * Created by weixi on 2017/7/7.
 */

public abstract class OnRequestListener<T> implements IOnRequestListener,Consumer<T> {
    @Override
    public void accept(T o) throws Exception {
        onSuccess(o);
    }

    @Override
    public void onError(Throwable throwable) {
        ErrorResponse response=new ErrorResponse();
        response.setMsg(throwable.getMessage());
        if (NetWorkUtils.isNetworkAvailable()){//后台返回错误
            onError(response);
        } else {//网络错误
            onSystemError(response);
        }
    }

    public abstract void onSuccess(T o);
    public abstract void onError(ErrorResponse o);
    public abstract void onSystemError(ErrorResponse o);
}
