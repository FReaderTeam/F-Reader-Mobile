package com.freader;

import java.util.ArrayList;
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

public class GetBookListTask extends AsyncTask<Void, Long, Boolean> {

	private final ProgressDialog mDialog;
	// private List<Entry> entries;
	List<DbxFileInfo> entries;
	private boolean mCanceled;
	final private BookCollectionFragment booksFragment;
	DbxAccountManager mDbxAcctMgr;

	public GetBookListTask(BookCollectionFragment fragment) {
		this.booksFragment = fragment;
		// Set a Dialog while searching books.
		mDialog = new ProgressDialog(fragment.getActivity());
		mDialog.setMessage("Searching books");
		mDialog.setButton("Cancel", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				mCanceled = true;
				// booksFragment.onCancelled();
			}
		});
		mDialog.show();
	}

	@Override
	protected Boolean doInBackground(Void... params) {
		mDbxAcctMgr = booksFragment.getAccountManager();

		// try {
		if (mCanceled) {
			return false;
		}
		// }
		mDbxAcctMgr = booksFragment.getAccountManager();
		// Search .fb2 files in dropbox
		try {
			entries = getFb2Files(DbxPath.ROOT);
		} catch (DbxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;

	}

	private List<DbxFileInfo> getFb2Files(DbxPath path) throws DbxException {
		DbxFileSystem fileSystem = DbxFileSystem.forAccount(mDbxAcctMgr
				.getLinkedAccount());
		List<DbxFileInfo> list = fileSystem.listFolder(path);
		List<DbxFileInfo> result = new ArrayList<DbxFileInfo>();
		for (DbxFileInfo file : list) {
			if (file.path.toString().endsWith(".fb2")) {
				result.add(file);
			}
			if (file.isFolder) {
				result.addAll(getFb2Files(file.path));
			}
		}
		return result;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		mDialog.dismiss();
		if (result) {
			booksFragment.setBooks(entries);
		}
	}

}
