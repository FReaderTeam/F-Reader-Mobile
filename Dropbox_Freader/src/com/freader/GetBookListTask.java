package com.freader;

import java.util.List;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.AsyncTask;

import com.dropbox.sync.android.DbxAccountManager;
import com.dropbox.sync.android.DbxException;
import com.dropbox.sync.android.DbxFileInfo;
import com.dropbox.sync.android.DbxFileSystem;
import com.dropbox.sync.android.DbxPath;
import com.freader.utils.FileSystemUtils;

public class GetBookListTask extends AsyncTask<Void, Long, Boolean> {

	private final ProgressDialog mDialog;
	List<DbxFileInfo> entries;
	private boolean mCanceled;
	final private BookCollectionFragment booksFragment;
	DbxAccountManager mDbxAcctMgr;

	public GetBookListTask(BookCollectionFragment fragment) {
		this.booksFragment = fragment;
		// Set a Dialog while searching books.
		mDialog = new ProgressDialog(fragment.getActivity());
		mDialog.setMessage(booksFragment.getString(R.string.searching_books));
		mDialog.setButton(booksFragment.getString(R.string.cancel),
				new OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						mCanceled = true;
					}
				});
		mDialog.show();
	}

	@Override
	protected Boolean doInBackground(Void... params) {
		mDbxAcctMgr = booksFragment.getAccountManager();
		if (mCanceled) {
			return false;
		}
		mDbxAcctMgr = booksFragment.getAccountManager();
		// Search .fb2 files in dropbox
		try {
			DbxFileSystem fileSystem = DbxFileSystem.forAccount(mDbxAcctMgr
					.getLinkedAccount());
			entries = FileSystemUtils.getFb2Files(fileSystem, DbxPath.ROOT);
		} catch (DbxException e) {
			e.printStackTrace();
		}
		return true;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		mDialog.dismiss();
		if (result) {
			booksFragment.setBooks(entries);
		}
	}

}
