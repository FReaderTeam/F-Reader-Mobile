package com.freader;

import java.io.IOException;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.freader.utils.DropboxUtils;
import com.freader.utils.ToastUtils;

public class UploadBookTask extends AsyncTask<Void, Long, Boolean> {

	private final ProgressDialog mDialog;
	private final String mBookPath;
	private final String mBookName;
	private final Context mContext;

	public UploadBookTask(Context mContext, String mBookName, String mBookPath) {
		this.mBookPath = mBookPath;
		this.mBookName = mBookName;
		this.mContext = mContext;

		mDialog = new ProgressDialog(mContext);
		mDialog.setMax(100);
		mDialog.setMessage(mContext.getString(R.string.uploading) + mBookName);
		mDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		mDialog.setProgress(0);

		mDialog.show();
	}

	@Override
	protected Boolean doInBackground(Void... params) {
		String srcPath = mBookPath;
		String dstPath = "/FReaderBooks/" + mBookName;
		try {
			DropboxUtils.writeFileToDropbox(srcPath, dstPath);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		mDialog.dismiss();
		if (result) {
			ToastUtils.showLongToast(mContext, R.string.uploading_successful);
		}
	}
}
