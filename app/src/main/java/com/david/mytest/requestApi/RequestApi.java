package com.david.mytest.requestApi;

import com.david.mytest.requestBean.LatestMsgBean;
import com.david.mytest.requestBean.ThemeDailyBean;
import com.david.mytest.requestBean.ThemesBean;

import io.reactivex.Flowable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by david on 2016/11/28.
 */

public interface RequestApi {
    //http://news-at.zhihu.com/api/4/news/latest
    @GET("news/latest")
    Call<LatestMsgBean> getLatestMsg();

    //http://news-at.zhihu.com/api/4/theme/12
    @GET("theme/{id}")
    Call<ThemeDailyBean> getThemeDaily(@Path("id") int id);

    //http://news-at.zhihu.com/api/4/themes
    @GET("themes")
    Flowable<ThemesBean> getThemes();


}
