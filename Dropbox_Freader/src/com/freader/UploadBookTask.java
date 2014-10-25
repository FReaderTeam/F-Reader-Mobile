package com.freader;

import java.io.File;
import java.io.IOException;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.dropbox.sync.android.DbxAccountManager;
import com.dropbox.sync.android.DbxException;
import com.dropbox.sync.android.DbxException.Unauthorized;
import com.dropbox.sync.android.DbxFile;
import com.dropbox.sync.android.DbxFileSystem;
import com.dropbox.sync.android.DbxPath;
import com.dropbox.sync.android.DbxPath.InvalidPathException;

public class UploadBookTask extends AsyncTask<Void, Long, Boolean> {
	private final ProgressDialog mDialog;
	DbxAccountManager mDbxAcctMgr;
	String mBookPath;
	String mBookName;
	Context mContext;

	public UploadBookTask(Context mContext, String mBookName, String mBookPath,
			DbxAccountManager mDbxAcctMgr) {
		this.mBookPath = mBookPath;
		this.mBookName = mBookName;
		this.mDbxAcctMgr = mDbxAcctMgr;
		this.mContext = mContext;

		mDialog = new ProgressDialog(mContext);
		mDialog.setMax(100);
		mDialog.setMessage("Uploading " + mBookName);
		mDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		mDialog.setProgress(0);

		mDialog.show();
	}

	@Override
	protected Boolean doInBackground(Void... params) {
		DbxFileSystem dbxFs = null;
		DbxFile testFile;
		try {
			dbxFs = DbxFileSystem.forAccount(mDbxAcctMgr.getLinkedAccount());
		} catch (Unauthorized e) {
			e.printStackTrace();
			return false;
		}
		try {
			testFile = dbxFs.create(new DbxPath("FReaderBooks/" + mBookName));
		} catch (InvalidPathException e) {
			e.printStackTrace();
			return false;
		} catch (DbxException e) {
			e.printStackTrace();
			return false;
		}
		File file = new File(mBookPath);
		try {
			testFile.writeFromExistingFile(file, false);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} finally {
			testFile.close();
		}
		return true;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		mDialog.dismiss();
		if (result) {
			Toast msg = Toast.makeText(mContext, "Uploading successfull!",
					Toast.LENGTH_LONG);
			msg.show();
		}
	}
}
