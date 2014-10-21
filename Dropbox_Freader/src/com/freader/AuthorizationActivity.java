package com.freader;

import java.io.File;
import java.util.ArrayList;

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

import com.freader.bookprototype.ScreenSlideWaiting;

public class AuthorizationActivity extends Activity {

	final static private String appKey = "vahra3f0bhvq3pu";
	final static private String appSecret = "4ubx866o44ayo0s";

	private static final int PICKFILE_RESULT_CODE = 1;

	private static final int REQUEST_LINK_TO_DBX = 0;
	private DbxAccountManager mDbxAcctMgr;

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

		mDbxAcctMgr = DbxAccountManager.getInstance(getApplicationContext(),
				appKey, appSecret);

		if (!mDbxAcctMgr.hasLinkedAccount())
			mDbxAcctMgr.startLink((Activity) this, REQUEST_LINK_TO_DBX);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		this.menu = menu;
		getMenuInflater().inflate(R.menu.main, menu);
		setLoggedIn(mDbxAcctMgr.hasLinkedAccount());
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
				mDbxAcctMgr.startLink((Activity) this, REQUEST_LINK_TO_DBX);
			}
			return true;
		case R.id.mUpload:
			Intent pickerIntent = new Intent(AuthorizationActivity.this,
					BookPickerActivity.class);
			startActivityForResult(pickerIntent, PICKFILE_RESULT_CODE);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case PICKFILE_RESULT_CODE:
			String mFilePath = data.getStringExtra("filePickerPath");
			Log.e(mFilePath, "was choosen");
			// TODO call upload async task
			String [] s = mFilePath.split("/");
			Log.e(s[s.length-1], "file name");
			new UploadBookTask(AuthorizationActivity.this, s[s.length-1], mFilePath,
					mDbxAcctMgr).execute();
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
		boolean success = true;
		if (!folder.exists())
			success = folder.mkdir();
		folder = new File(Environment.getExternalStorageDirectory()
				+ "/FReader/Books");
		success = true;
		if (!folder.exists())
			success = folder.mkdir();
	}

	@Override
	protected void onResume() {
		super.onResume();

	}

	private void logOut() {
		mDbxAcctMgr.unlink();
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
			menu.getItem(0).setTitle("Unlink from Dropbox");
			listOfBooks.setText("List of books:");
			FragmentTransaction transaction = getFragmentManager()
					.beginTransaction();
			transaction.add(R.id.books_fragment, new BookCollectionFragment(
					mDbxAcctMgr, Environment.getExternalStorageDirectory()
							+ "/FReader/Books", this));
			transaction.commit();

		} else {
			menu.getItem(0).setTitle("Link from Dropbox");
			listOfBooks.setText(" ");
		}
	}

	private void showToast(String msg) {
		Toast error = Toast.makeText(this, msg, Toast.LENGTH_LONG);
		error.show();
	}

	void startPageActivity(String path, String title, String name,
			ArrayList<String> arr) {
		Intent intent = new Intent(this, ScreenSlideWaiting.class);
		intent.putExtra("title", title);
		intent.putExtra("name", name);
		intent.putExtra("book", arr);
		intent.putExtra("path", path);
		Log.w("Test", "Before activity call");
		startActivity(intent);
	}
}