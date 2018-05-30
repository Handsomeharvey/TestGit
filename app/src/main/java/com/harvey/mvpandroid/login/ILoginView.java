package com.harvey.mvpandroid.login;


import com.harvey.mvpandroid.mvp.MvpBaseView;

/**
 * Created by hanhui on 2016/10/14 0014 09:07
 */
public interface ILoginView extends MvpBaseView<LoginModule> {

    void showEmailError();

    void showPasswordError();
}
