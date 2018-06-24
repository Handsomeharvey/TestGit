package com.harvey.mvpandroid.login;

import com.harvey.mvpandroid.mvp.MvpPresenter;

/**
 * Created by harvey on 2016/10/14 0014 09:25
 */
public interface LoginPresenter extends MvpPresenter<ILoginView> {

	void login(final String email, final String psw);

}
