package com.david.mytest.request;

/**
 * Created by weixi on 2017/7/7.
 */

public interface IOnRequestListener<T> {
    void onError(Throwable response);
}
