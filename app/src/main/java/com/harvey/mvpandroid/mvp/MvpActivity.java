package com.harvey.mvpandroid.mvp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.harvey.mvpandroid.R;

/**
 * Created by hanhui on 2016/11/8 0008 11:01
 */
public abstract class MvpActivity<CV extends View, M, V extends MvpBaseView<M>, P extends MvpPresenter<V>>
		extends
			AppCompatActivity
		implements
			MvpBaseView<M> {

	protected View loadingView;
	protected CV contentView;
	protected TextView errorView;
	protected P presenter;
	@NonNull
	protected abstract P createPresenter();

	protected abstract int getContentViewId();

	protected abstract void initHolder();

	protected abstract void handleIntent(Intent intent);

	protected abstract void initListener();

	protected abstract void initData();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		presenter = createPresenter();
		setContentView(getContentViewId());
		initHolder();
		if (null != getIntent())
			handleIntent(getIntent());
		initListener();
		initData();
		loadData(false);
	}

	@CallSuper
	@Override
	public void onContentChanged() {
		super.onContentChanged();
		loadingView = findViewById(R.id.loadingView);
		contentView = findViewById(R.id.contentView);
		errorView = findViewById(R.id.errorView);
		if (errorView != null) {
			errorView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					onErrorViewClicked();
				}
			});
		}
	}

	protected void onErrorViewClicked() {
		loadData(false);
	}

	@Override
	public void showLoading(boolean pullToRefresh) {
		if (!pullToRefresh) {
			animateLoadingViewIn();
		}
	}

	/**
	 * Override this method if you want to provide your own animation for
	 * showing the loading view
	 */
	protected void animateLoadingViewIn() {
		MvpAnimator.showLoading(loadingView, contentView, errorView);
	}

	@Override
	public void showContent() {
		animateContentViewIn();
	}

	/**
	 * Called to animate from loading view to content view
	 */
	protected void animateContentViewIn() {
		MvpAnimator.showContent(loadingView, contentView, errorView);
	}

	@Override
	public void showError(int errorCode, String errorMsg, boolean pullToRefresh) {
		if (pullToRefresh) {
			if (contentView != null && contentView instanceof SwipeRefreshLayout)
				((SwipeRefreshLayout) contentView).setRefreshing(false);
			Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show();
		} else {
			if (errorView == null) {
				Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show();
				return;
			}
			if (!TextUtils.isEmpty(errorMsg))
				errorView.setText(errorMsg);
			animateErrorViewIn();
		}
	}

	@Override
	public void setData(M m) {
	}
	/**
	 * Animates the error view in (instead of displaying content view / loading
	 * view)
	 */
	protected void animateErrorViewIn() {
		MvpAnimator.showErrorView(loadingView, contentView, errorView);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (presenter != null)
			presenter.detachView();
		presenter = null;
		loadingView = null;
		contentView = null;
		errorView = null;
	}
}
