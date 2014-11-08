package com.freader;

import java.io.File;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.dropbox.sync.android.DbxAccountManager;
import com.dropbox.sync.android.DbxException;
import com.dropbox.sync.android.DbxException.Unauthorized;
import com.dropbox.sync.android.DbxFileInfo;
import com.dropbox.sync.android.DbxFileSystem;
import com.freader.parser.*;


public class BookCollectionFragment extends Fragment {

	// Dropbox
	DbxAccountManager mDbxAcctMgr;

	AlertDialog.Builder confirmDialog;
	private int listBookPosition;

	// Model
	private ListView mBookListView;
	private List<DbxFileInfo> mBooks;
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
		SwipeRefreshLayout mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(
				 R.id.refresh_book_collection);
		mSwipeRefreshLayout.setColorSchemeColors(Color.BLUE, Color.BLACK, Color.BLACK, Color.BLACK);
	    mSwipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
	    	
			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				updateBook();
			}
		});
	    
		mBookListView = (ListView) view.findViewById(R.id.book_list);
		
		mBookListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> a, View v, int position,
					long id) {
				mBooks.get(position).path.getName();
				new DownloadAndParseBookTask(getActivity(), mBooks.get(position).path
						.getName(), mBooks.get(position).path, mAppPath,
						BookCollectionFragment.this).execute();
			}
		});

		mBookListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				listBookPosition = position;
				registerForContextMenu(mBookListView);
				return false;
			}
		});

		// Alert dialog
		confirmDialog = new AlertDialog.Builder(this.getActivity());
		confirmDialog.setMessage(R.string.are_you_sure);
		confirmDialog.setPositiveButton(R.string.yes, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				deleteBook();
			}
		});
		confirmDialog.setNegativeButton(R.string.no, new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				Toast.makeText(getActivity(), R.string.delete_was_chancelled,
						Toast.LENGTH_LONG).show();
			}
		});
		return view;
	}

	private void updateBook(){
		this.onResume();
	}
	
	private void deleteBook() {
		// deleting from DropBox
		try {
			DbxFileSystem sys = DbxFileSystem.forAccount(mDbxAcctMgr
					.getLinkedAccount());
			sys.delete(mBooks.get(listBookPosition).path);
			Toast.makeText(getActivity(), R.string.successful, Toast.LENGTH_LONG)
					.show();
			this.onResume();
		} catch (Unauthorized e) {
			e.printStackTrace();
		} catch (DbxException e) {
			e.printStackTrace();
		}
		// delete from storage
		File f = new File(mAppPath + "/"
				+ mBooks.get(listBookPosition).path.getName());
		if (f.exists())
			f.delete();
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add(R.string.delete);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		confirmDialog.show();
		return true;
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

	public void callbackDBTask(EBook ebook, String dbPath) {
		StringBuilder name = new StringBuilder();
		if (ebook.authors.get(0).middleName == null)
			name.append(ebook.authors.get(0).firstName)
			.append(" ")
			.append(ebook.authors.get(0).lastName);
		else
			name.append(ebook.authors.get(0).firstName)
			.append(" ")
			.append(ebook.authors.get(0).middleName)
			.append(" ").
			append(ebook.authors.get(0).lastName);
		a_activity.startPageActivity(dbPath, dbPath, name.toString(), ebook.title,
				ebook.parsedBook);
	}

	public void onCancelled() {

	}
}
