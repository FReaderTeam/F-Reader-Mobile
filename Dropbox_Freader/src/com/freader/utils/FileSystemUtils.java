package com.freader.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import com.dropbox.sync.android.DbxFileInfo;

import android.os.Environment;

public class FileSystemUtils {

	public static String APP_FOLDER = Environment.getExternalStorageDirectory()
			+ "/FReader";

	public static String BOOKS_FOLDER = APP_FOLDER + "/Books";

	/**
	 * Create folder Books in main app folder Freader to store books
	 */
	public static void createBooksFolder() {
		File folder = new File(APP_FOLDER);
		if (!folder.exists())
			folder.mkdir();
		folder = new File(BOOKS_FOLDER);
		if (!folder.exists())
			folder.mkdir();
	}

	public static String getXMLFileEncoding(byte[] xmlFile)
			throws UnsupportedEncodingException {
		byte[] headerBytes = new byte[1024];
		System.arraycopy(xmlFile, 0, headerBytes, 0, 1024);
		String header = new String(headerBytes, "ASCII");
		int endOfHeader = header.indexOf("?>");
		endOfHeader += 2;
		header = header.substring(0, endOfHeader);
		int encodingIndex = header.indexOf("encoding=\"");
		int encodingStart = encodingIndex + 10;
		int encodingEnd = header.indexOf("\"?>");
		String encoding = header.substring(encodingStart, encodingEnd);
		return encoding;
	}

	public static void deleteFile(String path) {
		File file = new File(path);
		if (file.exists()) {
			file.delete();
		}
	}

	public static String[] convertToFileNames(List<DbxFileInfo> mBooks) {
		String[] fileNames = new String[mBooks.size()];
		for (int i = 0; i < mBooks.size(); i++) {
			fileNames[i] = mBooks.get(i).path.getName();
		}
		return fileNames;
	}
	
	public static void writeFile(File file, byte[] content) throws IOException{
		FileOutputStream writer = new FileOutputStream(file);
		writer.write(content);
		writer.flush();
		writer.close();
	}
}
