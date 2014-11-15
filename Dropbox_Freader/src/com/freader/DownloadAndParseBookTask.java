package com.freader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.io.IOUtils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.AsyncTask;

import com.dropbox.sync.android.DbxException;
import com.dropbox.sync.android.DbxException.Unauthorized;
import com.dropbox.sync.android.DbxPath;
import com.freader.parser.EBook;
import com.freader.parser.SimpleFb2Parser;
import com.freader.utils.DropboxUtils;
import com.freader.utils.FileSystemUtils;
import com.freader.utils.ToastUtils;

public class DownloadAndParseBookTask extends AsyncTask<Void, Long, Boolean> {

	private final Context mContext;
	private final ProgressDialog mDialog;
	private int mErrorMsgId;
	private final String mBookName;
	private final DbxPath mBookPath;
	private final BookCollectionFragment fragment;
	private Boolean needToDownload;
	private File file;
	private EBook eBook;

	public DownloadAndParseBookTask(Context context, String bookName,
			DbxPath bookPath, BookCollectionFragment f) {
		mContext = context.getApplicationContext();
		mBookName = bookName;
		mBookPath = bookPath;
		fragment = f;
		needToDownload = true;
		file = new File(FileSystemUtils.BOOKS_FOLDER + "/" + mBookName);
		if (file.exists() && file.length() > 0) {
			needToDownload = false;
			mDialog = null;
		} else {
			mDialog = new ProgressDialog(context);
			mDialog.setMessage(mContext.getString(R.string.downloading));
			mDialog.setButton(mContext.getString(R.string.cancel),
					new OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							mErrorMsgId = R.string.canceled;
							String path = FileSystemUtils.BOOKS_FOLDER + "/"
									+ mBookName;
							FileSystemUtils.deleteFile(path);
						}
					});
			mDialog.show();
		}
	}

	@Override
	protected Boolean doInBackground(Void... params) {
		try {
			byte[] content;
			if (needToDownload) {
				content = DropboxUtils.readFileFromDropbox(mBookPath);
				FileSystemUtils.writeFile(file, content);
			} else {
				content = IOUtils.toByteArray(new FileInputStream(file));
			}
			String encoding = FileSystemUtils.getXMLFileEncoding(content);
			String bookContent = new String(content, encoding);
			eBook = SimpleFb2Parser.parse(bookContent);
			return true;
		} catch (FileNotFoundException e) {
			mErrorMsgId = R.string.file_not_found;
		} catch (Unauthorized e) {
			mErrorMsgId = R.string.problem_with_authorization;
		} catch (DbxException e) {
			mErrorMsgId = R.string.error;
		} catch (IOException e) {
			mErrorMsgId = R.string.can_not_write_file_to_storage;
		}
		return false;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		if(mDialog != null){
			mDialog.dismiss();
		}
		if (!result) {
			ToastUtils.showLongToast(mContext, mErrorMsgId);
		} else {
			if (needToDownload) {
				String success = R.string.successful + mBookPath.toString();
				ToastUtils.showLongToast(mContext, success);
			}
			fragment.callbackDBTask(eBook, mBookPath.toString());
		}
	}
}