package com.david.mytest.update;

/**
 * Created by david on 2016-8-6.
 */
public interface UpdateDownLoadListener {

    void onStarted();

    void onProgressChanged(int progress);

    void onFinished(float fileSize);

    void onFailure(String errorMessage);
}
