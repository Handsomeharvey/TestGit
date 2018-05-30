package com.harvey.mvpandroid.mvp;

import android.support.annotation.UiThread;

/**
 * Created by hanhui on 2016/10/13 0013 09:41
 */
public interface MvpPresenter<V extends MvpView> {

    /**
     * Set or attach the view to this presenter
     */
    @UiThread
    void attachView(V view);

    /**
     * Will be called if the view has been destroyed. Typically this method will be invoked from
     */
    @UiThread
    void detachView();
}
