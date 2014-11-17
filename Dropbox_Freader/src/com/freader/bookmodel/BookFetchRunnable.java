package com.freader.bookmodel;

import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.os.HandlerThread;
import android.text.TextPaint;
import android.util.Log;
import android.os.Process;

@SuppressLint({ "UseSparseArrays", "NewApi" })
public class BookFetchRunnable extends HandlerThread {

	private static final float SCREEN_COEF = 0.75f;
	private static final float lineSpacingMultiplier = 1.2f;
	private static final int lineSpacingExtra = 0;
	
	private static final int PRIORITY = Process.THREAD_PRIORITY_BACKGROUND
			+ Process.THREAD_PRIORITY_MORE_FAVORABLE;
	
	private ParsedBook parsedBook;

	public BookFetchRunnable(ParsedBook parsedBook) {
		super("thefetch", PRIORITY);
		this.parsedBook = parsedBook;
	}

	@Override
	public void run() {
		
		PageSplitter pageSplitter = new PageSplitter(
				parsedBook.textView.getWidth(),
				(int) (parsedBook.textView.getHeight() * SCREEN_COEF), lineSpacingMultiplier, lineSpacingExtra);
		Log.w("freader", String.valueOf(parsedBook.textView.getHeight()));
		HashMap<Integer, Integer> paragraphsToPages = new HashMap<Integer, Integer>();
		TextPaint textPaint = parsedBook.textView.getPaint();
		for (int i = 0; i < parsedBook.paragraphs.size(); i++) {
			paragraphsToPages.put(i, pageSplitter.getPageNumber());
			pageSplitter.appendParagraph(parsedBook.paragraphs.get(i),
					textPaint);
		}

		Log.w("freader", paragraphsToPages.toString());
		parsedBook.activity.callback(
				(ArrayList<CharSequence>) pageSplitter.getPages(),
				paragraphsToPages);
	}
}
