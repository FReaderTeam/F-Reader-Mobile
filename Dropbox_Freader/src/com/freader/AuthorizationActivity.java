package com.freader;

import java.io.File;
import java.util.ArrayList;

import javax.inject.Inject;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import com.dropbox.sync.android.DbxAccountManager;
import com.dropbox.sync.android.DbxDatastore;
import com.dropbox.sync.android.DbxDatastoreManager;
import com.dropbox.sync.android.DbxException;
import com.dropbox.sync.android.DbxException.Unauthorized;
import com.freader.bookmodel.PagesHolder;
import com.freader.bookprototype.ScreenSlideWaiting;
import com.freader.di.InjectingActivity;

import static com.freader.dao.DropboxSettings.*;

public class AuthorizationActivity extends InjectingActivity {

	private static final String TITLE = "title";
	private static final String NAME = "name";
	private static final String PATH = "path";
	private static final String DB_PATH = "dbPath";

	// Dropbox
	public static DbxDatastoreManager dbxDatastoreManager;
	
	@Inject
	DbxAccountManager mDbxAccountManager;

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
		// Basic Android widgets
		setClearListView();
		createFolder();
		// mDbxAccountManager = DbxAccountManager.getInstance(
		// getApplicationContext(), APP_KEY, APP_SECRET);
		if (!mDbxAccountManager.hasLinkedAccount())
			mDbxAccountManager.startLink((Activity) this, REQUEST_LINK_TO_DBX);
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
		switch (item.getItemId()) {
		case R.id.mSubmit:
			if (mLoggedIn) {
				logOut();
			} else {
				// Start the remote authentication
				setClearListView();
				createFolder();
				mDbxAccountManager = DbxAccountManager.getInstance(
						getApplicationContext(), APP_KEY, APP_SECRET);
			}
			return true;
		case R.id.mUpload:
			Intent pickerIntent = new Intent(AuthorizationActivity.this,
					BookPickerActivity.class);
			startActivityForResult(pickerIntent, PICKFILE_REQUEST_CODE);
			return true;
		case R.id.refreshLibrary:
			setClearListView();
			listOfBooks.setText(R.string.list_of_books);
			FragmentTransaction transaction = getFragmentManager()
					.beginTransaction();
			transaction.add(
					R.id.books_fragment,
					new BookCollectionFragment(mDbxAccountManager, Environment
							.getExternalStorageDirectory() + "/FReader/Books",
							this));
			transaction.commit();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case PICKFILE_REQUEST_CODE:
			if (resultCode != PICKFILE_CANCEL_CODE) {
				String mFilePath = data.getStringExtra("filePickerPath");
				Log.e(mFilePath, "was choosen");
				String[] s = mFilePath.split("/");
				new UploadBookTask(AuthorizationActivity.this, s[s.length - 1],
						mFilePath, mDbxAccountManager).execute();
			}
			break;
		case REQUEST_LINK_TO_DBX:
			mLoggedIn = true;
			setLoggedIn(mLoggedIn);
		}
	}

	private void createFolder() {
		// Create folder Books in main app folder Freader to store books
		File folder = new File(Environment.getExternalStorageDirectory()
				+ "/FReader");
		if (!folder.exists())
			folder.mkdir();
		folder = new File(Environment.getExternalStorageDirectory()
				+ "/FReader/Books");
		if (!folder.exists())
			folder.mkdir();
	}

	private void logOut() {
		mDbxAccountManager.unlink();
		setLoggedIn(false);
		setClearListView();
	}

	private void setClearListView() {
		setContentView(R.layout.login_activity);
		listOfBooks = (TextView) findViewById(R.id.list_of_books);
	}

	private void setLoggedIn(boolean loggedIn) {
		mLoggedIn = loggedIn;
		if (loggedIn) {
			try {
				if (datastore == null) {
					dbxDatastoreManager = DbxDatastoreManager
							.forAccount(mDbxAccountManager.getLinkedAccount());
					datastore = dbxDatastoreManager.openDefaultDatastore();
				}
			} catch (Unauthorized e) {
				showToast(getString(R.string.problem_with_authorization));
			} catch (DbxException e) {
				showToast(getString(R.string.problem_with_opening_datastore));
			}
			menu.getItem(0).setTitle(R.string.unlink_from_dropbox);
			listOfBooks.setText(R.string.list_of_books);
			FragmentTransaction transaction = getFragmentManager()
					.beginTransaction();
			transaction.add(
					R.id.books_fragment,
					new BookCollectionFragment(mDbxAccountManager, Environment
							.getExternalStorageDirectory() + "/FReader/Books",
							this));
			transaction.commit();
		} else {
			menu.getItem(0).setTitle(R.string.link_from_dropbox);
			listOfBooks.setText(" ");
		}
	}

	private void showToast(String msg) {
		Toast error = Toast.makeText(this, msg, Toast.LENGTH_LONG);
		error.show();
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