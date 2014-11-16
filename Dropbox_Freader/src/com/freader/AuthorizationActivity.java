package com.freader;

import static com.freader.utils.DropboxUtils.PICKFILE_CANCEL_CODE;
import static com.freader.utils.DropboxUtils.PICKFILE_REQUEST_CODE;
import static com.freader.utils.DropboxUtils.REQUEST_LINK_TO_DBX;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.freader.bookmodel.PagesHolder;
import com.freader.bookprototype.ScreenSlideWaiting;
import com.freader.utils.DropboxUtils;
import com.freader.utils.FileSystemUtils;

public class AuthorizationActivity extends Activity {

	private static final String TITLE = "title";
	private static final String NAME = "name";
	private static final String PATH = "path";
	private static final String DBX_PATH = "dbPath";

	private boolean isLoggedIn;

	// Android widgets
	private Menu menu;
	private TextView listOfBooks;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setClearListView();
		FileSystemUtils.createBooksFolder();
		onLogIn();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		this.menu = menu;
		getMenuInflater().inflate(R.menu.main, menu);
		setLoggedIn(DropboxUtils.isAuthorized());
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
			String[] partOfPath = mFilePath.split("/");
			String bookPath = partOfPath[partOfPath.length - 1];
			new UploadBookTask(this, bookPath, mFilePath).execute();
		}
	}

	private void onSubmit() {
		if (isLoggedIn) {
			onLogOut();
		} else {
			// Start the remote authentication
			setClearListView();
			FileSystemUtils.createBooksFolder();
			onLogIn();
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
		showBookCollectionFragment();
	}

	private void onLogOut() {
		DropboxUtils.logOut();
		setLoggedIn(false);
		setClearListView();
	}

	private void onLogIn() {
		DropboxUtils.logIn(this);
	}

	private void setClearListView() {
		setContentView(R.layout.login_activity);
		listOfBooks = (TextView) findViewById(R.id.list_of_books);
	}

	private void setLoggedIn(boolean loggedIn) {
		isLoggedIn = loggedIn;
		if (isLoggedIn) {
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
		Fragment fragment = new BookCollectionFragment(this);
		transaction.add(R.id.books_fragment, fragment);
		transaction.commit();
	}

	void startPageActivity(String path, String dbPath, String title,
			String name, ArrayList<String> paragraphs) {
		PagesHolder.getInstance().setParagraphs(paragraphs);
		Intent intent = new Intent(this, ScreenSlideWaiting.class);
		intent.putExtra(TITLE, title);
		intent.putExtra(NAME, name);
		intent.putExtra(PATH, path);
		intent.putExtra(DBX_PATH, dbPath);
		startActivity(intent);
	}
	
	@Override
	public void onBackPressed(){
		Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
	}

}