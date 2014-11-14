package com.freader;

import static com.freader.utils.DropboxSettings.APP_KEY;
import static com.freader.utils.DropboxSettings.APP_SECRET;
import static com.freader.utils.DropboxSettings.PICKFILE_CANCEL_CODE;
import static com.freader.utils.DropboxSettings.PICKFILE_REQUEST_CODE;
import static com.freader.utils.DropboxSettings.REQUEST_LINK_TO_DBX;

import java.util.ArrayList;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.dropbox.sync.android.DbxAccountManager;
import com.dropbox.sync.android.DbxDatastore;
import com.dropbox.sync.android.DbxDatastoreManager;
import com.dropbox.sync.android.DbxException;
import com.dropbox.sync.android.DbxException.Unauthorized;
import com.freader.bookmodel.PagesHolder;
import com.freader.bookprototype.ScreenSlideWaiting;
import com.freader.utils.FileSystemUtils;
import com.freader.utils.ToastUtils;

public class AuthorizationActivity extends Activity {

	private static final String TITLE = "title";
	private static final String NAME = "name";
	private static final String PATH = "path";
	private static final String DB_PATH = "dbPath";

	// Dropbox
	public static DbxDatastoreManager dbxDatastoreManager;
	private DbxAccountManager mDbxAccountManager;

	// Model
	public static ArrayList<String> arr;
	public static DbxDatastore datastore;
	private boolean mLoggedIn;

	// Android widgets
	private Menu menu;
	private TextView listOfBooks;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setClearListView();
		FileSystemUtils.createBooksFolder();
		logIn();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		this.menu = menu;
		getMenuInflater().inflate(R.menu.main, menu);
		setLoggedIn(mDbxAccountManager.hasLinkedAccount());
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int selectedItemId = item.getItemId();
		switch (selectedItemId) {
		case R.id.mSubmit:
			onSubmit();
			return true;
		case R.id.mUpload:
			onUpload();
			return true;
		case R.id.refreshLibrary:
			onRefreshLibrabry();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case PICKFILE_REQUEST_CODE:
			onFilePicked(data, resultCode);
			break;
		case REQUEST_LINK_TO_DBX:
			setLoggedIn(true);
		}
	}

	private void onFilePicked(Intent data, int resultCode) {
		if (resultCode != PICKFILE_CANCEL_CODE) {
			String mFilePath = data.getStringExtra("filePickerPath");
			Log.e(mFilePath, "was choosen");
			String[] s = mFilePath.split("/");
			new UploadBookTask(AuthorizationActivity.this, s[s.length - 1],
					mFilePath, mDbxAccountManager).execute();
		}
	}

	private void onSubmit() {
		if (mLoggedIn) {
			logOut();
		} else {
			// Start the remote authentication
			setClearListView();
			FileSystemUtils.createBooksFolder();
			logIn();
		}
	}

	private void onUpload() {
		Intent pickerIntent = new Intent(AuthorizationActivity.this,
				BookPickerActivity.class);
		startActivityForResult(pickerIntent, PICKFILE_REQUEST_CODE);
	}

	private void onRefreshLibrabry() {
		setClearListView();
		listOfBooks.setText(R.string.list_of_books);
		FragmentTransaction transaction = getFragmentManager()
				.beginTransaction();
		transaction.add(R.id.books_fragment, new BookCollectionFragment(
				mDbxAccountManager, this));
		transaction.commit();
	}

	private void logOut() {
		mDbxAccountManager.unlink();
		setLoggedIn(false);
		setClearListView();
	}

	private void logIn() {
		mDbxAccountManager = DbxAccountManager.getInstance(
				getApplicationContext(), APP_KEY, APP_SECRET);
		if (!mDbxAccountManager.hasLinkedAccount())
			mDbxAccountManager.startLink((Activity) this, REQUEST_LINK_TO_DBX);
	}

	private void setClearListView() {
		setContentView(R.layout.login_activity);
		listOfBooks = (TextView) findViewById(R.id.list_of_books);
	}

	private void setLoggedIn(boolean loggedIn) {
		mLoggedIn = loggedIn;
		if (mLoggedIn) {
			setDatastoreForLinkedAccount();
			menu.getItem(0).setTitle(R.string.unlink_from_dropbox);
			listOfBooks.setText(R.string.list_of_books);
			showBookCollectionFragment();
		} else {
			menu.getItem(0).setTitle(R.string.link_from_dropbox);
			listOfBooks.setText(" ");
		}
	}

	private void showBookCollectionFragment() {
		FragmentTransaction transaction = getFragmentManager()
				.beginTransaction();
		transaction.add(R.id.books_fragment, new BookCollectionFragment(
				mDbxAccountManager, this));
		transaction.commit();
	}

	private void setDatastoreForLinkedAccount() {
		try {
			if (datastore == null) {
				dbxDatastoreManager = DbxDatastoreManager
						.forAccount(mDbxAccountManager.getLinkedAccount());
				datastore = dbxDatastoreManager.openDefaultDatastore();
			}
		} catch (Unauthorized e) {
			ToastUtils.showLongToast(this,
					getString(R.string.problem_with_authorization));
		} catch (DbxException e) {
			ToastUtils.showLongToast(this,
					getString(R.string.problem_with_opening_datastore));
		}
	}

	void startPageActivity(String path, String dbPath, String title,
			String name, ArrayList<String> paragraphs) {
		Intent intent = new Intent(this, ScreenSlideWaiting.class);
		intent.putExtra(TITLE, title);
		intent.putExtra(NAME, name);
		PagesHolder.getInstance().setParagraphs(paragraphs);
		intent.putExtra(PATH, path);
		intent.putExtra(DB_PATH, dbPath);
		startActivity(intent);
	}

}