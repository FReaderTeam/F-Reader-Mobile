package com.freader;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.dropbox.sync.android.DbxAccountManager;
import com.dropbox.sync.android.DbxException;
import com.dropbox.sync.android.DbxException.Unauthorized;
import com.dropbox.sync.android.DbxFileInfo;
import com.dropbox.sync.android.DbxFileSystem;
import com.dropbox.sync.android.DbxPath;
import com.freader.parser.EBook;
import com.freader.utils.FileSystemUtils;
import com.freader.utils.SharedPreferencesUtils;

public class BookCollectionFragment extends Fragment {

	// Dropbox
	DbxAccountManager mDbxAcctMgr;

	AlertDialog.Builder confirmDialog;
	private int listBookPosition;

	// Model
	private ListView mBookListView;
	private List<DbxFileInfo> mBooks;
	private AuthorizationActivity authActivity;
	private Button mLastOpenedBook;
	private SharedPreferences sharedPreferences;
	private int lastOpenedBookPosition;

	public BookCollectionFragment() {
	}

	public BookCollectionFragment(DbxAccountManager mDbxAcctMgr,
			AuthorizationActivity a_activity) {
		this.mDbxAcctMgr = mDbxAcctMgr;
		this.authActivity = a_activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater
				.inflate(R.layout.book_collection, container, false);
		sharedPreferences = SharedPreferencesUtils
				.getSharedPreferences(getActivity());

		SwipeRefreshLayout mSwipeRefreshLayout = (SwipeRefreshLayout) view
				.findViewById(R.id.refresh_book_collection);
		mSwipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				updateBook();
			}
		});

		mLastOpenedBook = (Button) view.findViewById(R.id.last_opened_book);
		mLastOpenedBook.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				lastOpenedBookPosition = SharedPreferencesUtils
						.getPosition(sharedPreferences);
				openBook(lastOpenedBookPosition);
				return false;
			}
		});

		mBookListView = (ListView) view.findViewById(R.id.book_list);
		mBookListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> a, View v, int position,
					long id) {
				SharedPreferencesUtils
						.savePosition(sharedPreferences, position);
				openBook(position);
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

	private void openBook(int position) {
		mBooks.get(position).path.getName();
		String bookName = mBooks.get(position).path.getName();
		DbxPath bookPath = mBooks.get(position).path;
		new DownloadAndParseBookTask(getActivity(), bookName, bookPath, this)
				.execute();
	}

	private void updateBook() {
		this.onResume();
	}

	private void deleteBook() {
		// deleting from DropBox
		try {
			DbxFileSystem sys = DbxFileSystem.forAccount(mDbxAcctMgr
					.getLinkedAccount());
			sys.delete(mBooks.get(listBookPosition).path);
			Toast.makeText(getActivity(), R.string.successful,
					Toast.LENGTH_LONG).show();
			this.onResume();
		} catch (Unauthorized e) {
			e.printStackTrace();
		} catch (DbxException e) {
			e.printStackTrace();
		}
		// delete from storage
		String bookPath = FileSystemUtils.BOOKS_FOLDER + "/"
				+ mBooks.get(listBookPosition).path.getName();
		FileSystemUtils.deleteFile(bookPath);
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
		this.mBooks = books;
		Collections.sort(mBooks, new Comparator<DbxFileInfo>() {
			@Override
			public int compare(DbxFileInfo one, DbxFileInfo another) {
				return another.modifiedTime.compareTo(one.modifiedTime);
			}
		});
		BookListAdapter adapter = new BookListAdapter(getActivity(), mBooks);
		mBookListView.setAdapter(adapter);
	}

	public void callbackDBTask(EBook ebook, String dbPath) {
		StringBuilder name = new StringBuilder();
		if (ebook.authors.get(0).middleName == null)
			name.append(ebook.authors.get(0).firstName).append(" ")
					.append(ebook.authors.get(0).lastName);
		else
			name.append(ebook.authors.get(0).firstName).append(" ")
					.append(ebook.authors.get(0).middleName).append(" ")
					.append(ebook.authors.get(0).lastName);
		authActivity.startPageActivity(dbPath, dbPath, ebook.title,
				name.toString(), ebook.parsedBook);
	}
}
