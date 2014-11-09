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

	
	private ParsedBook parsedBook;

	public BookFetchRunnable(ParsedBook parsedBook) {
		super("thefetch", Process.THREAD_PRIORITY_BACKGROUND + Process.THREAD_PRIORITY_MORE_FAVORABLE);
		this.parsedBook = parsedBook;
	}

	

	@Override
	public void run() {
		
		
		PageSplitter pageSplitter = new PageSplitter(
				parsedBook.textView.getWidth(),
				(int) (parsedBook.textView.getHeight()*0.8), 1.2f, 0);
		Log.w("freader", String.valueOf(parsedBook.textView.getHeight()));
		HashMap<Integer, Integer> paragraphsToPages = new HashMap<Integer, Integer>();
		TextPaint textPaint = parsedBook.textView.getPaint();
		for (int i = 0; i < parsedBook.paragraphs.size(); i++) {
			paragraphsToPages.put(i, pageSplitter.getPageNumber());
			pageSplitter
					.appendParagraph(parsedBook.paragraphs.get(i), textPaint);
		}

		Log.w("freader",paragraphsToPages.toString());
		parsedBook.activity.callback((ArrayList<CharSequence>) pageSplitter.getPages(), paragraphsToPages);
	}

}
