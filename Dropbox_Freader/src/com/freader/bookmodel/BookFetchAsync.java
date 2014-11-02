package com.freader.bookmodel;

import java.util.HashMap;
import java.util.ArrayList;

import com.freader.bookprototype.ScreenSlideWaiting;

import android.text.TextPaint;
import android.util.Log;
import android.annotation.SuppressLint;
import android.os.AsyncTask;

@SuppressLint("UseSparseArrays")
public class BookFetchAsync extends
		AsyncTask<ParsedBook, Void, ArrayList<CharSequence>> {

	private ScreenSlideWaiting activity;
	private HashMap<Integer, Integer> paragraphsToPages;

	// private int bufferSize = 1024*10;

	@Override
	protected ArrayList<CharSequence> doInBackground(ParsedBook... params) {

		PageSplitter pageSplitter = new PageSplitter(
				params[0].textView.getWidth(),
				(int) (params[0].textView.getHeight() * 0.80),
				params[0].textView.getLineSpacingMultiplier(),
				(int) params[0].textView.getLineSpacingExtra());
		paragraphsToPages = new HashMap<Integer, Integer>();
		TextPaint textPaint = params[0].textView.getPaint();
		for (int i = 0; i < params[0].paragraphs.size(); i++) {
			paragraphsToPages.put(i, pageSplitter.getPageNumber());
			pageSplitter
					.append("\n\t" + params[0].paragraphs.get(i), textPaint);
		}
		activity = params[0].activity;
		Log.w("freader",paragraphsToPages.toString());
		return (ArrayList<CharSequence>) pageSplitter.getPages();
	}

	protected void onPostExecute(ArrayList<CharSequence> pages) {
		Log.w("com.freader", "Book fetched");
		activity.callback(pages, paragraphsToPages);
	}

}
