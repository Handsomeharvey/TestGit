package com.harvey.mvpandroid.login;

import android.os.Handler;
import android.text.TextUtils;

import com.harvey.mvpandroid.mvp.MvpBasePresenter;
import com.harvey.mvpandroid.utils.PatternUtils;

import java.util.concurrent.TimeUnit;

/**
 * Created by hanhui on 2016/10/14 0014 09:25
 */
public class LoginPresenterImpl extends MvpBasePresenter<ILoginView> implements LoginPresenter {

	public LoginPresenterImpl(ILoginView v) {
		attachView(v);
	}

	@Override
	public void login(String email, String psw) {
		if (!PatternUtils.matchEmail(email)) {
			getView().showEmailError();
		} else if (TextUtils.isEmpty(psw)) {
			getView().showPasswordError();
		} else {
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					if (isViewAttached()) {
						getView().setData(new LoginModule());
					}
				}
			}, TimeUnit.SECONDS.toMillis(3));

		}
	}

}
