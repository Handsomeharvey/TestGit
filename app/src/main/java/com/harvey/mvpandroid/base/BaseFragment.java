package com.harvey.mvpandroid.base;

import android.support.v4.app.Fragment;
import android.view.View;

import com.harvey.mvpandroid.mvp.MvpBasePresenter;
import com.harvey.mvpandroid.mvp.MvpBaseView;
import com.harvey.mvpandroid.mvp.MvpFragment;

/**
 * Created by hanhui on 2016/11/8 0008 11:01
 */
public abstract class BaseFragment<CV extends View, M, V extends MvpBaseView<M>, P extends MvpBasePresenter<V>>
		extends
			MvpFragment<CV, M, V, P> {

	// 添加fragment
	protected void addFragment(BaseFragment fragment) {
		if (null != fragment && mContext instanceof BaseActivity) {
			((BaseActivity) mContext).addFragment(fragment);
		}
	}

	// 添加fragment
	protected void addFragment(Fragment fragment, int layoutId) {
		if (null != fragment && mContext instanceof BaseActivity) {
			((BaseActivity) mContext).addFragment(fragment, layoutId);
		}
	}

	protected void replaceFragment(BaseFragment fragment) {
		if (null != fragment && mContext instanceof BaseActivity) {
			((BaseActivity) mContext).replaceFragment(fragment);
		}
	}

	// 移除fragment
	protected void removeFragment() {
		if (mContext instanceof BaseActivity)
			((BaseActivity) mContext).removeFragment();
	}

	/**
	 * 设置页面标题栏
	 *
	 * @param resId
	 *            标题
	 */
	public void setSubPageTitle(int resId) {
		if (mContext instanceof BaseActivity)
			((BaseActivity) mContext).setSubPageTitle(resId);
	}

	public void setTitleVisibility(boolean isShow) {
		if (mContext instanceof BaseActivity)
			((BaseActivity) mContext).setTitleVisibility(isShow);
	}
	/**
	 * 设置页面标题栏
	 *
	 * @param title
	 *            标题
	 */
	public void setSubPageTitle(CharSequence title) {
		if (mContext instanceof BaseActivity)
			((BaseActivity) mContext).setSubPageTitle(title);
	}

	public void setSubTitleMenu(boolean isShow) {
		if (mContext instanceof BaseActivity)
			((BaseActivity) mContext).setSubTitleMenu(isShow);
	}

	/**
	 * 设置页面标题栏菜单
	 */
	public void setSubTitleMenu(boolean isShow, int resId, View.OnClickListener onClickListener) {
		if (mContext instanceof BaseActivity)
			((BaseActivity) mContext).setSubTitleMenu(isShow, resId, onClickListener);
	}

	/**
	 * 设置页面标题栏
	 *
	 * @param title
	 *            标题
	 */
	public void setSubPageTitle(CharSequence title, boolean isShowBack) {
		if (mContext instanceof BaseActivity)
			((BaseActivity) mContext).setSubPageTitle(title, isShowBack);
	}

	/**
	 * 得到根Fragment(多层Fragment跳转的时候需要用这个方法启动activity)
	 *
	 * @return
	 */
	protected Fragment getRootFragment() {
		Fragment fragment = getParentFragment();
		if (fragment == null) {
			return this;
		}
		while (fragment.getParentFragment() != null) {
			fragment = fragment.getParentFragment();
		}
		return fragment;
	}
}
