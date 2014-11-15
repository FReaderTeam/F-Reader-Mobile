package com.freader.utils;

import static com.freader.utils.DropboxUtils.APP_KEY;
import static com.freader.utils.DropboxUtils.APP_SECRET;
import static com.freader.utils.DropboxUtils.REQUEST_LINK_TO_DBX;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;

import com.dropbox.sync.android.DbxAccountManager;
import com.dropbox.sync.android.DbxDatastore;
import com.dropbox.sync.android.DbxDatastoreManager;
import com.dropbox.sync.android.DbxException;
import com.dropbox.sync.android.DbxFile;
import com.dropbox.sync.android.DbxFileInfo;
import com.dropbox.sync.android.DbxPath;
import com.dropbox.sync.android.DbxException.Unauthorized;
import com.dropbox.sync.android.DbxPath.InvalidPathException;
import com.dropbox.sync.android.DbxFileSystem;
import com.freader.AuthorizationActivity;

import android.app.Activity;
import android.content.Context;

public class DropboxUtils {
	// AUTH INFORMATION
	public static final String APP_KEY = "k3wrgzqc4uzpulv";
	public static final String APP_SECRET = "2b7cj3w87d6w203";
	public static final String ACCOUNT_PREFS_NAME = "prefs";
	public static final String ACCESS_KEY_NAME = "ACCESS_KEY";
	public static final String ACCESS_SECRET_NAME = "ACCESS_SECRET";
	public static final boolean USE_OAUTH1 = false;

	// REQUEST CODES
	public static final int PICKFILE_REQUEST_CODE = 1;
	public static final int REQUEST_LINK_TO_DBX = 0;
	public static final int PICKFILE_CANCEL_CODE = 2;

	private static DbxAccountManager accountManager;

	private static DbxDatastoreManager datastoreManager;

	private static DbxDatastore defaultDatastore;
	
	private static DbxFileSystem fileSystem;

	public static void logOut() {
		accountManager.unlink();
	}

	public static void logIn(Activity activity) {
		accountManager = DbxAccountManager.getInstance(
				activity.getApplicationContext(), APP_KEY, APP_SECRET);
		if (!accountManager.hasLinkedAccount())
			accountManager.startLink(activity, REQUEST_LINK_TO_DBX);
	}

	public static DbxDatastore getDefaultDatastore() throws DbxException {
		if (defaultDatastore == null) {
			datastoreManager = DbxDatastoreManager.forAccount(accountManager
					.getLinkedAccount());
			defaultDatastore = datastoreManager.openDefaultDatastore();
		}
		return defaultDatastore;
	}

	public static boolean isAuthorized() {
		return accountManager.hasLinkedAccount();
	}

	public static DbxFileSystem getFileSystem() throws Unauthorized {
		if(fileSystem == null){
			fileSystem = DbxFileSystem.forAccount(accountManager.getLinkedAccount());
		}
		return fileSystem;
	}

	public static void writeFileToDropbox(String srcPath, String dstPath)
			throws IOException {
		DbxFileSystem dbxFs = getFileSystem();
		DbxFile dbxFile = null;
		try{
			DbxPath dstDbxPath = new DbxPath("/FReaderBooks/" + dstPath);
			dbxFile = dbxFs.create(dstDbxPath);
			File androidFile = new File(srcPath);
			dbxFile.writeFromExistingFile(androidFile, false);
		}finally {
			if(dbxFile != null){
				dbxFile.close();
			}
		}
		
	}
	
	public static byte[] readFileFromDropbox(DbxPath path) throws IOException{
		DbxFileSystem sys = DropboxUtils.getFileSystem();
		DbxFile file = sys.open(path);
		InputStream input = file.getReadStream();
		try{
			byte[] content = IOUtils.toByteArray(input);
			return content;
		}finally{
			input.close();
		}
		
		
	}
	
	public static List<DbxFileInfo> getFb2Files(DbxPath path) throws DbxException {
		DbxFileSystem fileSystem = getFileSystem();
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
	
}
