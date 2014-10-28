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
import com.dropbox.sync.android.DbxDatastoreManager;
import com.dropbox.sync.android.DbxException.Unauthorized;
import com.freader.bookmodel.PagesHolder;
import com.freader.bookprototype.ScreenSlideWaiting;
import static com.freader.dao.DropboxSettings.*;

public class AuthorizationActivity extends Activity {

	//Dropbox
	public static DbxDatastoreManager dbxDatastoreManager;
	private DbxAccountManager mDbxAccountManager;
	
	// Model
	public static ArrayList<String> arr;
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
		mDbxAccountManager = DbxAccountManager.getInstance(getApplicationContext(),
				APP_KEY, APP_SECRET);
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
				mDbxAccountManager.startLink((Activity) this, REQUEST_LINK_TO_DBX);
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
			new UploadBookTask(AuthorizationActivity.this, s[s.length-1], mFilePath,
					mDbxAccountManager).execute();
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
				dbxDatastoreManager = DbxDatastoreManager
						.forAccount(mDbxAccountManager.getLinkedAccount());
			} catch (Unauthorized e) {
				showToast("Problem with authorization!");
			}
			menu.getItem(0).setTitle("Unlink from Dropbox");
			listOfBooks.setText("List of books:");
			FragmentTransaction transaction = getFragmentManager()
					.beginTransaction();
			transaction.add(R.id.books_fragment, new BookCollectionFragment(
					mDbxAccountManager, Environment.getExternalStorageDirectory()
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

	void startPageActivity(String path, String dbPath, String title,
			String name, ArrayList<String> paragraphs) {
		Intent intent = new Intent(this, ScreenSlideWaiting.class);
		intent.putExtra("title", title);
		intent.putExtra("name", name);
		PagesHolder.getInstance().setParagraphs(paragraphs);
		intent.putExtra("path", path);
		intent.putExtra("dbPath",dbPath);
		Log.w("Test", "Before activity call");
		startActivity(intent);
		
	}
}