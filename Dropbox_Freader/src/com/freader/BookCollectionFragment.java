package com.freader;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.dropbox.sync.android.DbxAccountManager;
import com.dropbox.sync.android.DbxFile;
import com.dropbox.sync.android.DbxFileInfo;
import com.dropbox.sync.android.DbxFileSystem;
import com.dropbox.sync.android.DbxPath;
import com.freader.bookmodel.ParsedBook;
import com.freader.bookprototype.ScreenSlideActivity;
import ebook.*;
import ebook.parser.*;

public class BookCollectionFragment extends Fragment {

	private ListView mBookListView;
	private List<DbxFileInfo> mBooks;

	DbxAccountManager mDbxAcctMgr;
	private String mAppPath;
	private AuthorizationActivity a_activity;

	public BookCollectionFragment() {
	}

	public BookCollectionFragment(DbxAccountManager mDbxAcctMgr, String path,
			AuthorizationActivity a_activity) {
		this.mDbxAcctMgr = mDbxAcctMgr;
		this.mAppPath = path;
		this.a_activity = a_activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater
				.inflate(R.layout.book_collection, container, false);
		mBookListView = (ListView) view.findViewById(R.id.book_list);
		mBookListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> a, View v, int position,
					long id) {
				mBooks.get(position).path.getName();
				Object obj = mBookListView.getItemAtPosition(position);
				String obj_itemDetails = (String) obj;
				new DownloadBookTask(getActivity(), mBooks.get(position).path
						.getName(), mBooks.get(position).path, mAppPath,
						BookCollectionFragment.this).execute();
			}
		});

		return view;
	}

	protected DbxAccountManager getAccountManager() {
		return this.mDbxAcctMgr;
	}

	@Override
	public void onResume() {
		super.onResume();
		new GetBookListTask(BookCollectionFragment.this).execute();
	}

	public void setBooks(List<DbxFileInfo> books) {
		Collections.sort(books, new Comparator<DbxFileInfo>() {
			@Override
			public int compare(DbxFileInfo lhs, DbxFileInfo rhs) {
				return rhs.modifiedTime.compareTo(lhs.modifiedTime);
			}
		});
		this.mBooks = books;
		String[] fileNames = new String[mBooks.size()];
		for (int i = 0; i < mBooks.size(); i++) {
			fileNames[i] = mBooks.get(i).path.getName();// TODO get pathes from
														// somewhere
			// else, like datastore
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_list_item_1, fileNames);
		mBookListView.setAdapter(adapter);
	}

	public void callbackDBTask(String bookPath, String dbPath) {
		// parse
		AsyncParser ap = new AsyncParser();
		ap.execute(bookPath);
		EBook ebook = new EBook();
		try {
			ebook = ap.get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		String name = ebook.authors.get(0).firstName + " "
				+ ebook.authors.get(0).middleName + " "
				+ ebook.authors.get(0).lastName;
		a_activity.startPageActivity(bookPath, dbPath, name, ebook.title,
				ebook.parsedBook);
	}

	public void onCancelled() {

	}

	private class AsyncParser extends AsyncTask<String, Boolean, EBook> {
		private ProgressDialog progressDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = new ProgressDialog(getActivity());
			progressDialog.setMessage("Opening book. Please wait...");
			progressDialog.setIndeterminate(true);
			progressDialog.show();
		}

		@Override
		protected EBook doInBackground(String... params) {
			Parser parser = new InstantParser();
			parser.parse(params[0]);
			return parser.getEBoook();
		}

		@Override
		protected void onPostExecute(EBook result) {
			super.onPostExecute(result);
			progressDialog.hide();
		}
	}
}
