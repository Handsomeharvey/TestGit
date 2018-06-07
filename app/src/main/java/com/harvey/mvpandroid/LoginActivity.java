package com.harvey.mvpandroid;

import android.annotation.TargetApi;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.harvey.mvpandroid.base.BaseActivity;
import com.harvey.mvpandroid.login.ILoginView;
import com.harvey.mvpandroid.login.LoginModule;
import com.harvey.mvpandroid.login.LoginPresenter;
import com.harvey.mvpandroid.login.LoginPresenterImpl;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BaseActivity<View, LoginModule, ILoginView, LoginPresenter>
		implements
			LoaderCallbacks<Cursor>,
			ILoginView {

	/**
	 * Id to identity READ_CONTACTS permission request.
	 */
	private static final int REQUEST_READ_CONTACTS = 0;

	// UI references.
	private AutoCompleteTextView mEmailView;
	private EditText mPasswordView;
	private Button mEmailSignInButton;

	@NonNull
	@Override
	public LoginPresenter createPresenter() {
		return new LoginPresenterImpl(this);
	}

	@Override
	protected int getContentViewId() {
		return R.layout.activity_login;
	}

	@Override
	protected void initHolder() {
		mEmailView = findViewById(R.id.email);
		mPasswordView = findViewById(R.id.password);
		mEmailSignInButton = findViewById(R.id.email_sign_in_button);
		setSubPageTitle("登录", false);
	}

	@Override
	protected void handleIntent(Intent intent) {

	}

	@Override
	public void initListener() {
		mEmailSignInButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				attemptLogin();
			}
		});
	}

	@Override
	protected void initData() {

	}

	/**
	 * Callback received when a permissions request has been completed.
	 */
	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
			@NonNull int[] grantResults) {
		if (requestCode == REQUEST_READ_CONTACTS) {
			if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				populateAutoComplete();
			}
		}
	}

	private void attemptLogin() {
		mEmailView.setError(null);
		mPasswordView.setError(null);
		final String email = mEmailView.getText().toString();
		final String password = mPasswordView.getText().toString();
		presenter.login(email, password);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
		return new CursorLoader(this, Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
				ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

				ContactsContract.Contacts.Data.MIMETYPE + " = ?",
				new String[]{ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE},
				ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
	}

	@Override
	public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
		List<String> emails = new ArrayList<>();
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			emails.add(cursor.getString(ProfileQuery.ADDRESS));
			cursor.moveToNext();
		}

		addEmailsToAutoComplete(emails);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> cursorLoader) {

	}

	private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
		ArrayAdapter<String> adapter = new ArrayAdapter<>(LoginActivity.this,
				android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

		mEmailView.setAdapter(adapter);
	}

	@Override
	public void showEmailError() {
		mEmailView.setError(getString(R.string.error_invalid_email));
		mEmailView.requestFocus();
	}

	@Override
	public void showPasswordError() {
		mPasswordView.setError(getString(R.string.error_invalid_password));
		mPasswordView.requestFocus();
	}

	@Override
	public void setData(LoginModule m) {
		Toast.makeText(this, "登录成功!", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void loadData(boolean pullToRefresh) {
		populateAutoComplete();
	}
	private void populateAutoComplete() {
		if (!mayRequestContacts()) {
			return;
		}
		getLoaderManager().initLoader(0, null, this);
	}

	private boolean mayRequestContacts() {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
			return true;
		}
		if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
			return true;
		}
		if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
			Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
					.setAction(android.R.string.ok, new View.OnClickListener() {
						@Override
						@TargetApi(Build.VERSION_CODES.M)
						public void onClick(View v) {
							requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
						}
					});
		} else {
			requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
		}
		return false;
	}
	private interface ProfileQuery {
		String[] PROJECTION = {ContactsContract.CommonDataKinds.Email.ADDRESS,
				ContactsContract.CommonDataKinds.Email.IS_PRIMARY,};

		int ADDRESS = 0;
		int IS_PRIMARY = 1;
	}
}
