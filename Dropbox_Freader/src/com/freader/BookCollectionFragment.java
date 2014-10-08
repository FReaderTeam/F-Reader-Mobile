package com.freader;

import java.util.List;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.DropboxAPI.Entry;
import com.dropbox.client2.android.AndroidAuthSession;

public class BookCollectionFragment extends Fragment {

	private ListView mBookListView;
	private List<Entry> mBooks;
	private DropboxAPI<AndroidAuthSession> mApi;
	
	public BookCollectionFragment(DropboxAPI<AndroidAuthSession> mApi) {
		this.mApi = mApi;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.book_collection, container, false);
        mBookListView = (ListView) view.findViewById(R.id.book_list);
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

	public void setBooks(List<Entry> books){
		this.mBooks = books;
		String[] fileNames = new String[mBooks.size()];
		for(int i = 0; i < mBooks.size(); i++){
			fileNames[i] = mBooks.get(i).fileName();
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, fileNames);
		mBookListView.setAdapter(adapter);
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
