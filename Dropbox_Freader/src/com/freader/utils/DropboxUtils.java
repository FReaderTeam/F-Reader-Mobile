package com.freader.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.apache.commons.io.IOUtils;

import android.app.Activity;

import com.dropbox.sync.android.DbxAccountManager;
import com.dropbox.sync.android.DbxDatastore;
import com.dropbox.sync.android.DbxDatastoreManager;
import com.dropbox.sync.android.DbxException;
import com.dropbox.sync.android.DbxFields;
import com.dropbox.sync.android.DbxRecord;
import com.dropbox.sync.android.DbxTable;
import com.dropbox.sync.android.DbxException.Unauthorized;
import com.dropbox.sync.android.DbxFile;
import com.dropbox.sync.android.DbxFileInfo;
import com.dropbox.sync.android.DbxFileSystem;
import com.dropbox.sync.android.DbxPath;

public abstract class DropboxUtils {

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

	// DATASTORE FIELDS
	private static final String BOOK_PATH_FIELD_NAME = "bookFullPath";
	private static final String PARAGRAPH_FIELD_NAME = "paragraph";
	private static final String TABLE_NAME = "position_android";

	private static DbxAccountManager accountManager;

	private static DbxDatastoreManager datastoreManager;

	private static DbxDatastore defaultDatastore;

	private static DbxFileSystem fileSystem;

	public static void logOut() {
		accountManager.unlink();
		fileSystem = null;
		defaultDatastore.close();
		defaultDatastore = null;
		datastoreManager = null;
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
		if (fileSystem == null) {
			fileSystem = DbxFileSystem.forAccount(accountManager
					.getLinkedAccount());
		}
		return fileSystem;
	}

	public static void writeFileToDropbox(String srcPath, String dstPath)
			throws IOException {
		DbxFileSystem dbxFs = getFileSystem();
		DbxFile dbxFile = null;
		try {
			DbxPath dstDbxPath = new DbxPath(dstPath);
			dbxFile = dbxFs.create(dstDbxPath);
			File androidFile = new File(srcPath);
			dbxFile.writeFromExistingFile(androidFile, false);
		} finally {
			if (dbxFile != null) {
				dbxFile.close();
			}
		}

	}

	public static byte[] readFileFromDropbox(DbxPath path) throws IOException {
		DbxFileSystem sys = DropboxUtils.getFileSystem();
		DbxFile file = sys.open(path);
		InputStream input = file.getReadStream();
		try {
			byte[] content = IOUtils.toByteArray(input);
			return content;
		} finally {
			input.close();
		}
	}

	public static List<DbxFileInfo> getFb2Files(DbxPath path)
			throws DbxException {
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

	public static synchronized void savePosition(String bookFullPath,
			long absoluteParagraph) throws DbxException {
		DbxDatastore datastore = getDefaultDatastore();
		DbxTable positionTable = datastore.getTable(TABLE_NAME);
		DbxFields queryParams = new DbxFields().set(BOOK_PATH_FIELD_NAME,
				bookFullPath);
		DbxTable.QueryResult results = positionTable.query(queryParams);
		List<DbxRecord> records = results.asList();
		for (DbxRecord record : records) {
			record.deleteRecord();
		}
		positionTable.insert().set(BOOK_PATH_FIELD_NAME, bookFullPath)
				.set(PARAGRAPH_FIELD_NAME, absoluteParagraph);
		datastore.sync();
	}

	public static int getPosition(String bookFullPath)
			throws DbxException {
		DbxDatastore datastore = getDefaultDatastore();
		DbxTable positionTable = datastore.getTable(TABLE_NAME);
		DbxFields queryParams = new DbxFields().set(BOOK_PATH_FIELD_NAME,
				bookFullPath);
		DbxTable.QueryResult results = positionTable.query(queryParams);
		try {
			DbxRecord firstResult = results.iterator().next();
			return (int)firstResult.getLong(PARAGRAPH_FIELD_NAME);
		} catch (NoSuchElementException e) {
			return 1;
		}
	}

}
