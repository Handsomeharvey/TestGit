package com.harvey.mvpandroid.mvp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.harvey.mvpandroid.R;

/**
 * A Fragment that uses a {@link MvpPresenter} to implement a
 * Model-View-Presenter architecture
 *
 */
public abstract class MvpFragment<CV extends View, M, V extends MvpBaseView<M>, P extends MvpBasePresenter<V>>
		extends
			Fragment
		implements
			MvpBaseView<M> {
	protected View loadingView;
	protected CV contentView;
	protected TextView errorView;
	protected P presenter;
	protected Context mContext;

	@NonNull
	public abstract P createPresenter();

	public abstract View getCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

	public abstract void initHolder(final View view);

	public abstract void bindListener(final View view);

	public abstract void initData();

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		this.mContext = context;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return getCreateContentView(inflater, container, savedInstanceState);
	}

	@CallSuper
	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		if (presenter == null)
			presenter = createPresenter();
		initBaseHolder(view);
		initHolder(view);
		bindListener(view);
		initData();
		loadData(false);
	}

	private void initBaseHolder(final View view) {
		loadingView = view.findViewById(R.id.loadingView);
		contentView = view.findViewById(R.id.contentView);
		errorView = view.findViewById(R.id.errorView);
		if (errorView != null)
			errorView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					onErrorViewClicked();
				}
			});
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

	/**
	 * Called if the error view has been clicked. To disable clicking on the
	 * errorView use <code>errorView.setClickable(false)</code>
	 */
	protected void onErrorViewClicked() {
		loadData(false);
	}

	@Override
	public void showError(int errorCode, String errorMsg, boolean pullToRefresh) {
		if (pullToRefresh) {
			if (contentView != null && contentView instanceof SwipeRefreshLayout)
				((SwipeRefreshLayout) contentView).setRefreshing(false);
			Toast.makeText(getActivity(), errorMsg, Toast.LENGTH_SHORT).show();

		} else {
			if (errorView == null) {
				Toast.makeText(getActivity(), errorMsg, Toast.LENGTH_SHORT).show();
				return;
			}
			if (!TextUtils.isEmpty(errorMsg))
				errorView.setText(errorMsg);
			animateErrorViewIn();
		}
	}

	/**
	 * Animates the error view in (instead of displaying content view / loading
	 * view)
	 */
	protected void animateErrorViewIn() {
		MvpAnimator.showErrorView(loadingView, contentView, errorView);
	}

	@Override
	public void setData(M data) {
		if (contentView != null && contentView instanceof SwipeRefreshLayout)
			((SwipeRefreshLayout) contentView).setRefreshing(false);
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		if (presenter != null)
			presenter.detachView();
		presenter = null;
		loadingView = null;
		contentView = null;
		errorView = null;
	}
}
