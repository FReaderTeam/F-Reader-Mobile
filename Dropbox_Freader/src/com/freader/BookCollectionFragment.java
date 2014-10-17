package com.freader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
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
import ebook.*;
import ebook.parser.*;

public class BookCollectionFragment extends Fragment {

	private ListView mBookListView;
	private List<Entry> mBooks;
	private DropboxAPI<AndroidAuthSession> mApi;
	private String mAppPath;

	public BookCollectionFragment(DropboxAPI<AndroidAuthSession> mApi,
			String path) {
		this.mApi = mApi;
		this.mAppPath = path;
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
				Object obj = mBookListView.getItemAtPosition(position);
				String obj_itemDetails = (String) obj;
				new DownloadBookTask(getActivity(), mApi, obj_itemDetails,
						mAppPath, BookCollectionFragment.this).execute();
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
	
	public void callbackDBTask(String bookPath)
	{
		//TODO parse
		Parser parser = new InstantParser();
		EBook ebook = parser.parse(bookPath);
		ArrayList<String> arr = new ArrayList<String>();
		if (ebook.isOk) {
			try {
				arr = parser.getBookBody();
			} catch (FileNotFoundException e) {
			} catch (IOException e) {
			}
		}
		
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
}
