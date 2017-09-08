package com.harvey.mvpandroid.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
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
 * @author Hannes Dorfmann
 * @since 1.0.0
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
	protected MvpActivity mActivity;

	public abstract View getCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

	public void initHolder(final View view) {
		loadingView = view.findViewById(R.id.loadingView);
		contentView = (CV) view.findViewById(R.id.contentView);
		errorView = (TextView) view.findViewById(R.id.errorView);
		if (errorView != null)
			errorView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					onErrorViewClicked();
				}
			});
	}

	public abstract void bindListener(final View view);

	public abstract void initData();

	protected MvpActivity getHoldingActivity() {
		return mActivity;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.mActivity = (MvpActivity) activity;
	}

	@NonNull
	public abstract P createPresenter();

	@CallSuper
	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		if (presenter == null)
			presenter = createPresenter();
		initHolder(view);
		bindListener(view);
		initData();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return getCreateContentView(inflater, container, savedInstanceState);
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
			if (errorView == null)
				return;
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
	public void loadData(boolean pullToRefresh) {

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
