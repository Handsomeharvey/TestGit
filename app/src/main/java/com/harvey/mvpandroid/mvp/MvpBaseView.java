package com.harvey.mvpandroid.mvp;

import android.support.annotation.UiThread;

/**
 * * Created by harvey on 2016/10/13 0013 09:41
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
