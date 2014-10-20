package com.freader;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
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

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.DropboxAPI.Entry;
import com.dropbox.client2.android.AndroidAuthSession;
import com.freader.bookmodel.ParsedBook;
import com.freader.bookprototype.ScreenSlideActivity;
import ebook.*;
import ebook.parser.*;

public class BookCollectionFragment extends Fragment {

	private ListView mBookListView;
	private List<Entry> mBooks;
	private DropboxAPI<AndroidAuthSession> mApi;
	private String mAppPath;
	private AuthorizationActivity a_activity;

	public BookCollectionFragment(DropboxAPI<AndroidAuthSession> mApi,
			String path, AuthorizationActivity a_activity) {
		this.mApi = mApi;
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
				mBooks.get(position).fileName();
				Object obj = mBookListView.getItemAtPosition(position);
				String obj_itemDetails = (String) obj;
				new DownloadBookTask(getActivity(), mApi, mBooks.get(position)
						.fileName(), mBooks.get(position).path, mAppPath,
						BookCollectionFragment.this).execute();
			}
		});
		return view;
	}

	public DropboxAPI<AndroidAuthSession> getDropboxAPI() {
		return mApi;
	}

	@Override
	public void onResume() {
		super.onResume();
		new GetBookListTask(BookCollectionFragment.this).execute();
	}

	public void setBooks(List<Entry> books) {
		this.mBooks = books;
		String[] fileNames = new String[mBooks.size()];
		for (int i = 0; i < mBooks.size(); i++) {
			fileNames[i] = mBooks.get(i).path;// TODO get pathes from somewhere
			// else, like datastore
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_list_item_1, fileNames);
		mBookListView.setAdapter(adapter);
	}

	public void callbackDBTask(String bookPath) {
		// TODO parse
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
		a_activity.startPageActivity(bookPath, name, ebook.title, ebook.parsedBook);
	}

	public void onDropboxUnlinkedException() {
		// TODO Auto-generated method stub

	}

	public void onDropboxPartialFileException() {
		// TODO Auto-generated method stub

	}

	public void onDropboxServerException(String error) {
		// TODO Auto-generated method stub

	}

	public void onDropboxIOException() {
		// TODO Auto-generated method stub
	}

	public void onDropboxParseException() {
		// TODO Auto-generated method stub

	}

	public void onDropboxException() {
		// TODO Auto-generated method stub

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
