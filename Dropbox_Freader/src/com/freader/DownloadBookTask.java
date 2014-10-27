package com.freader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.dropbox.sync.android.DbxAccountManager;
import com.dropbox.sync.android.DbxException;
import com.dropbox.sync.android.DbxException.Unauthorized;
import com.dropbox.sync.android.DbxFile;
import com.dropbox.sync.android.DbxFileInfo;
import com.dropbox.sync.android.DbxFileSystem;
import com.dropbox.sync.android.DbxPath;

public class DownloadBookTask extends AsyncTask<Void, Long, Boolean> {

	private Context mContext;
	private final ProgressDialog mDialog;
	// private DropboxAPI<?> mApi;
	private boolean mCanceled;
	private String mErrorMsg;
	private String mBookName;
	private DbxPath mBookPath;
	private String appFolderPath;
	private BookCollectionFragment fragment;
	DbxAccountManager mDbxAcctMgr;

	public DownloadBookTask(Context context, String bookName, DbxPath bookPath,
			String appPath, BookCollectionFragment f) {
		mContext = context.getApplicationContext();
		mBookName = bookName;
		mBookPath = bookPath;
		mDbxAcctMgr = f.getAccountManager();
		appFolderPath = appPath;
		fragment = f;
		mDialog = new ProgressDialog(context);
		mDialog.setMessage("Downloading");
		mDialog.setButton("Cancel", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				mCanceled = true;
				mErrorMsg = "Canceled";
				// delete book from storage
				File file = new File(appFolderPath + "/" + mBookName);
				file.delete();
			}
		});
		mDialog.show();
	}

	@Override
	protected Boolean doInBackground(Void... params) {
		File file = new File(appFolderPath + "/" + mBookName);

		DbxFile testFile = null;
		try {
			DbxFileSystem sys = DbxFileSystem.forAccount(mDbxAcctMgr
					.getLinkedAccount());
			testFile = sys.open(mBookPath);
			String contents = testFile.readString();
			FileWriter writer = new FileWriter(file);
			writer.write(contents);
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			mErrorMsg = "File not found!";
			return false;
		} catch (Unauthorized e) {
			mErrorMsg = "Problem with authorization!";
			return false;
		} catch (DbxException e) {
			mErrorMsg = "Error!";
			return false;
		} catch (IOException e) {
			mErrorMsg = "Can't write file to storage!";
			return false;
		} finally {
			testFile.close();
		}
		return true;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		mDialog.dismiss();
		if (!result) {
			// Couldn't download it, so show an error
			showToast(mErrorMsg);
		} else {
			showToast("Succesfull! " + mBookPath.toString());
			fragment.callbackDBTask(appFolderPath + "/" + mBookName, mBookPath.toString());
		}
	}

	private void showToast(String msg) {
		Toast error = Toast.makeText(mContext, msg, Toast.LENGTH_LONG);
		error.show();
	}
}