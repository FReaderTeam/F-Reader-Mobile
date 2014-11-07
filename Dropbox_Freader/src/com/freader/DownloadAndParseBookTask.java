package com.freader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.AsyncTask;
import android.provider.DocumentsContract.Document;
import android.widget.Toast;

import com.dropbox.sync.android.DbxAccountManager;
import com.dropbox.sync.android.DbxException;
import com.dropbox.sync.android.DbxException.Unauthorized;
import com.dropbox.sync.android.DbxFile;
import com.dropbox.sync.android.DbxFileSystem;
import com.dropbox.sync.android.DbxPath;
import com.freader.parser.EBook;
import com.freader.parser.SimpleFb2Parser;

public class DownloadAndParseBookTask extends AsyncTask<Void, Long, Boolean> {

	private Context mContext;
	private final ProgressDialog mDialog;
	private boolean mCanceled;
	private String mErrorMsg;
	private String mBookName;
	private DbxPath mBookPath;
	private String appFolderPath;
	private BookCollectionFragment fragment;
	private Boolean needToDownload;
	private File file;
	private EBook eBook;
	DbxAccountManager mDbxAcctMgr;

	public DownloadAndParseBookTask(Context context, String bookName,
			DbxPath bookPath, String appPath, BookCollectionFragment f) {
		mContext = context.getApplicationContext();
		mBookName = bookName;
		mBookPath = bookPath;
		mDbxAcctMgr = f.getAccountManager();
		appFolderPath = appPath;
		fragment = f;
		needToDownload = true;
		mDialog = new ProgressDialog(context);
		file = new File(appFolderPath + "/" + mBookName);
		if (file.exists() && file.length() > 0)
			needToDownload = false;
		else {
			mDialog.setMessage(mContext.getString(R.string.downloading));
			mDialog.setButton(mContext.getString(R.string.cancel), new OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					mCanceled = true;
					mErrorMsg = mContext.getString(R.string.canceled);
					// delete book from storage
					File file = new File(appFolderPath + "/" + mBookName);
					file.delete();
				}
			});
			mDialog.show();
		}
	}

	@Override
	protected Boolean doInBackground(Void... params) {
		DbxFile testFile = null;
		try {
			byte[] content;
			if (needToDownload) {
				DbxFileSystem sys = DbxFileSystem.forAccount(mDbxAcctMgr
						.getLinkedAccount());
				testFile = sys.open(mBookPath);
				InputStream input = testFile.getReadStream();
				content = IOUtils.toByteArray(input);
				input.close();
				FileOutputStream writer = new FileOutputStream(file);
				writer.write(content);
				writer.flush();
				writer.close();
			} else {
				content = IOUtils.toByteArray(new FileInputStream(file));
			}
			String encoding = getEncoding(content);
			String bookContent = new String(content,encoding);
			eBook = SimpleFb2Parser.parse(bookContent);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			mErrorMsg = mContext.getString(R.string.file_not_found);
			return false;
		} catch (Unauthorized e) {
			mErrorMsg = mContext.getString(R.string.problem_with_authorization);
			return false;
		} catch (DbxException e) {
			mErrorMsg = mContext.getString(R.string.error);
			return false;
		} catch (IOException e) {
			mErrorMsg = mContext.getString(R.string.can_not_write_file_to_storage);
			return false;
		} finally {
			if (needToDownload)
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
			if (needToDownload){
				//showToast(R.string.successful + mBookPath.toString());
				Toast error = Toast.makeText(mContext, 
						R.string.successful + mBookPath.toString(), 
						Toast.LENGTH_LONG);
				error.show();
			}
			fragment.callbackDBTask(eBook, mBookPath.toString());
		}
	}

	private String getEncoding(byte[] xmlBytes)
			throws UnsupportedEncodingException {
		byte[] headerBytes = new byte[1024];
		System.arraycopy(xmlBytes, 0, headerBytes, 0, 1024);
		String header = new String(headerBytes, "ASCII");
		int endOfHeader = header.indexOf("?>");
		endOfHeader += 2;
		header = header.substring(0,endOfHeader);
		int encodingIndex = header.indexOf("encoding=\"");
		int encodingStart = encodingIndex + 10;
		int encodingEnd = header.indexOf("\"?>");
		String encoding = header.substring(encodingStart, encodingEnd);
		return encoding;
	}

	private void showToast(String msg) {
		Toast error = Toast.makeText(mContext, msg, Toast.LENGTH_LONG);
		error.show();
	}
}