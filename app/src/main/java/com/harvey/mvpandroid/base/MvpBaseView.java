package com.harvey.mvpandroid.base;


import android.support.annotation.UiThread;

/**
 * * Created by hanhui on 2016/10/13 0013 09:41
 */
public interface MvpBaseView<M> extends MvpView {

    @UiThread
    void showLoading(boolean pullToRefresh);

    @UiThread
    void showError(int errorCode, String errorMsg, boolean pullToRefresh);

    @UiThread
    void setData(M data);

    @UiThread
    void showContent();

    @UiThread
    void loadData(boolean pullToRefresh);
}
