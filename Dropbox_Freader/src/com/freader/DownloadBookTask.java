package com.freader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.DropboxAPI.DropboxFileInfo;
import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.exception.DropboxIOException;
import com.dropbox.client2.exception.DropboxParseException;
import com.dropbox.client2.exception.DropboxPartialFileException;
import com.dropbox.client2.exception.DropboxServerException;
import com.dropbox.client2.exception.DropboxUnlinkedException;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class DownloadBookTask extends AsyncTask<Void, Long, Boolean> {

	private Context mContext;
	private final ProgressDialog mDialog;
	private DropboxAPI<?> mApi;
	private boolean mCanceled;
	private String mErrorMsg;
	private String mBookName;
	private String appFolderPath;
	private BookCollectionFragment fragment;

	@SuppressWarnings("deprecation")
	public DownloadBookTask(Context context, DropboxAPI<?> api,
			String bookName, String appPath, BookCollectionFragment f) {
		mContext = context.getApplicationContext();
		mBookName = bookName;
		mApi = api;
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
		try {
			if (mCanceled) {
				return false;
			}
			// Creating new file
			File file = new File(appFolderPath + "/" + mBookName);
			// TODO delete this
			Log.i("AppFolderPath", "path" + appFolderPath);
			FileOutputStream outputStream = new FileOutputStream(file);
			// And downloading book into this file
			DropboxFileInfo info = mApi.getFile(mBookName, null, outputStream,
					null);
			// TODO delete this
			Log.i("DownloadingLog", "downloading" + info.getMetadata().path);
		} catch (DropboxUnlinkedException e) {
			// The AuthSession wasn't properly authenticated or user unlinked.
		} catch (DropboxPartialFileException e) {
			// We canceled the operation
			mErrorMsg = "Download canceled";
		} catch (DropboxServerException e) {
			// Server-side exception. These are examples of what could happen,
			// but we don't do anything special with them here.
			if (e.error == DropboxServerException._304_NOT_MODIFIED) {
				// won't happen since we don't pass in revision with metadata
			} else if (e.error == DropboxServerException._401_UNAUTHORIZED) {
				// Unauthorized, so we should unlink them. You may want to
				// automatically log the user out in this case.
			} else if (e.error == DropboxServerException._403_FORBIDDEN) {
				// Not allowed to access this
			} else if (e.error == DropboxServerException._404_NOT_FOUND) {
				// path not found (or if it was the thumbnail, can't be
				// thumbnailed)
			} else if (e.error == DropboxServerException._406_NOT_ACCEPTABLE) {
				// too many entries to return
			} else if (e.error == DropboxServerException._415_UNSUPPORTED_MEDIA) {
				// can't be thumbnailed
			} else if (e.error == DropboxServerException._507_INSUFFICIENT_STORAGE) {
				// user is over quota
			} else {
				// Something else
			}
			// This gets the Dropbox error, translated into the user's language
			mErrorMsg = e.body.userError;
			if (mErrorMsg == null) {
				mErrorMsg = e.body.error;
			}
		} catch (DropboxIOException e) {
			// Happens all the time, probably want to retry automatically.
			mErrorMsg = "Network error.  Try again.";
		} catch (DropboxParseException e) {
			// Probably due to Dropbox server restarting, should retry
			mErrorMsg = "Dropbox error.  Try again.";
		} catch (DropboxException e) {
			// Unknown error
			mErrorMsg = "Unknown error.  Try again.";
		} catch (FileNotFoundException e) {
			mErrorMsg = "File not found.";
			e.printStackTrace();
			// throws when downloading has been cancelled
		}
		return false;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		mDialog.dismiss();
		if (!result) {
			// Couldn't download it, so show an error
			showToast(mErrorMsg);
		} else {
			showToast("Succesfull!");
			fragment.callbackDBTask(appFolderPath + "/" + mBookName);
		}
	}

	private void showToast(String msg) {
		Toast error = Toast.makeText(mContext, msg, Toast.LENGTH_LONG);
		error.show();
	}
}
