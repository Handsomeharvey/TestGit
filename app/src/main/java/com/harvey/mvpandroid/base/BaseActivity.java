package com.harvey.mvpandroid.base;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.harvey.mvpandroid.R;
import com.harvey.mvpandroid.mvp.MvpActivity;
import com.harvey.mvpandroid.mvp.MvpBaseView;
import com.harvey.mvpandroid.mvp.MvpPresenter;
import com.harvey.mvpandroid.utils.StatusBarUtil;

import java.util.List;

/**
 * Created by hanhui on 2016/11/8 0008 11:01
 */
public abstract class BaseActivity<CV extends View, M, V extends MvpBaseView<M>, P extends MvpPresenter<V>>
		extends
			MvpActivity<CV, M, V, P> {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 默认沉侵状态栏
		StatusBarUtil.immersive(this);
		initFragment();
	}

	// 获取第一个fragment界面复杂的不用此种方法
	protected Fragment getFirstFragment() {
		return null;
	}

	// 布局中Fragment的ID
	protected int getFragmentContentId() {
		return R.id.fragment_container;
	}
	/**
	 * 设置页面标题栏
	 *
	 * @param title
	 *            标题
	 */
	public void setSubPageTitle(CharSequence title) {
		setSubPageTitle(title, true);
	}

	/**
	 * 设置页面标题栏
	 */
	public void setSubPageTitle(int resId) {
		setSubPageTitle(getString(resId), true);
	}

	public void setSubTitleMenu(boolean isShow) {
		setSubTitleMenu(isShow, "", null);
	}

	/**
	 * 设置页面标题栏菜单
	 */
	public void setSubTitleMenu(boolean isShow, int resId, View.OnClickListener onClickListener) {
		setSubTitleMenu(isShow, getString(resId), onClickListener);
	}

	/**
	 * 设置页面标题栏菜单
	 */
	public void setSubTitleMenu(boolean isShow, CharSequence name, View.OnClickListener onClickListener) {
		TextView tvTitleMenu = findViewById(R.id.tv_title_menu);
		if (tvTitleMenu == null)
			return;
		if (isShow) {
			tvTitleMenu.setText(name);
			tvTitleMenu.setVisibility(View.VISIBLE);
			if (onClickListener != null)
				tvTitleMenu.setOnClickListener(onClickListener);
		} else {
			tvTitleMenu.setVisibility(View.INVISIBLE);
		}
	}

	// 存钱罐APP标题风格修改
	public void setTitleVisibility(boolean isShow) {
		RelativeLayout title = findViewById(R.id.rlt_title);
		if (title != null) {
			final int statusHeight = StatusBarUtil.getStatusBarHeight(this);
			TypedArray actionbarSizeTypedArray = obtainStyledAttributes(new int[]{android.R.attr.actionBarSize});
			float actionBarHeight = actionbarSizeTypedArray.getDimension(0, 0);
			title.getLayoutParams().height = (int) (statusHeight + actionBarHeight);
			title.setPadding(0, statusHeight, 0, 0);
			title.setVisibility(isShow ? View.VISIBLE : View.GONE);
		}
	}

	/**
	 * 设置页面标题栏
	 *
	 * @param title
	 *            标题
	 */
	public void setSubPageTitle(CharSequence title, boolean isShowBack) {
		setTitleVisibility(true);
		TextView tv = findViewById(R.id.tv_title);
		if (tv != null) {
			tv.setText(title);
		}
		if (findViewById(R.id.ll_back) == null)
			return;
		if (isShowBack) {
			findViewById(R.id.ll_back).setVisibility(View.VISIBLE);
			findViewById(R.id.ll_back).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					removeFragment();
				}
			});
		} else {
			findViewById(R.id.ll_back).setVisibility(View.INVISIBLE);
		}
	}

	protected void initFragment() {
		List<Fragment> fragments = getSupportFragmentManager().getFragments();
		if (null == fragments || fragments.size() == 0) {
			Fragment firstFragment = getFirstFragment();
			if (null != firstFragment) {
				addFragment(firstFragment);
			}
		}
	}

	/**
	 * 添加fragment 到contentId 上
	 * 
	 * @param fragment
	 */
	protected void addFragment(Fragment fragment) {
		if (fragment != null) {
			getSupportFragmentManager().beginTransaction()
					.setCustomAnimations(R.anim.push_left_in, R.anim.push_left_out, R.anim.back_left_in,
							R.anim.back_right_out)
					.replace(getFragmentContentId(), fragment, fragment.getClass().getSimpleName())
					.addToBackStack(fragment.getClass().getSimpleName()).commitAllowingStateLoss();
		}
	}

	/**
	 * 添加fragment
	 * 
	 * @param fragment
	 * @param layoutId
	 */
	protected void addFragment(Fragment fragment, int layoutId) {
		getSupportFragmentManager().beginTransaction().replace(layoutId, fragment, fragment.getClass().getSimpleName())
				.commitAllowingStateLoss();
	}

	/**
	 * 替换content Fragment
	 * 
	 * @param fragment
	 */
	protected void replaceFragment(Fragment fragment) {
		List<Fragment> lisFragments = getSupportFragmentManager().getFragments();
		if (lisFragments != null && lisFragments.size() > 0) {
			for (Fragment fragment1 : lisFragments) {
				getSupportFragmentManager().beginTransaction().remove(fragment1);
			}
		}
		getSupportFragmentManager().beginTransaction()
				.setCustomAnimations(R.anim.push_left_in, R.anim.push_left_out, R.anim.back_left_in,
						R.anim.back_right_out)
				.replace(getFragmentContentId(), fragment, fragment.getClass().getSimpleName())
				.commitAllowingStateLoss();
	}

	/**
	 * 移除fragment
	 */
	public void removeFragment() {
		if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
			getSupportFragmentManager().popBackStack();
		} else {
			finish();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (KeyEvent.KEYCODE_BACK == keyCode) {
			if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
				finish();
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
}
