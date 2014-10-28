package com.freader.bookmodel;

import java.util.HashMap;
import java.util.ArrayList;

import java.util.HashMap;
import com.freader.bookprototype.ScreenSlideActivity;
import com.freader.bookprototype.ScreenSlideWaiting;

import android.util.Log;
import android.widget.TextView;
import android.os.AsyncTask;
import android.util.Log;

public class BookFetchAsync extends AsyncTask<ParsedBook, Integer, PagedBook> {

	private ScreenSlideWaiting activity;

	@Override
	protected PagedBook doInBackground(ParsedBook... params) {
		ArrayList<String> pages = new ArrayList<String>();
		int startParagraph = 0;
		StringBuilder page = new StringBuilder(), add = new StringBuilder(" "), previous = add;
		int lines = 0;
		String[] words;
		int pageNumber = 0;
		HashMap<Integer, Integer> paragraphsToPage = new HashMap<Integer, Integer>();
		// Start walking through paragraphs
		for (int par = startParagraph; par < params[0].paragraphs.size(); par++) {
			paragraphsToPage.put(par, pageNumber);
			// Split a paragraph to words
			words = params[0].paragraphs.get(par).split(" ");
			for (String word : words) {
				// Add a word each time
				add.append(" ").append(word);
				// Check if the line is longer than the page width
				if (isTooLarge(add, params[0].textView)) {
					// If page is over, create new page
					if (lines == params[0].lines_num - 1) {
						page.append(add);
						add.setLength(0);
						add.append(word); // Add one missed word
						pages.add(page.toString());
						pageNumber++;
						page.setLength(0);
						lines = 0;
					} else {
						lines++;
						page.append(previous);
						add.setLength(0);
					}
				}
				previous = add;
			}
			// If there's enough space on page for another paragraph, go on.
			if (lines < params[0].lines_num - 1) {
				page.append(add).append('\n');
				add.append(" ");
				lines++;
				continue;
				// If there isn't, initialize new page
			} else {
				page.append(add);
				pages.add(page.toString());
				pageNumber++;
				page.setLength(0);
				lines = 0;
			}

		}
		if (page.length() != 0) {
			pages.add(page.toString());
			pageNumber++;
		}
		Log.w("BookFetch", "Fetched, last page " + pages.get(pages.size() - 1));
		activity = params[0].activity;
		return new PagedBook(params[0].getAuthor(), params[0].getTitle(),
				pages, paragraphsToPage);
	}

	private boolean isTooLarge(StringBuilder newText, TextView textView) {
		float textWidth = textView.getPaint().measureText(newText.toString());

		if (textWidth >= textView.getMeasuredWidth()) {
			return true;
		}
		return false;
	}

	protected void onPostExecute(PagedBook pagedBook) {
		Log.w("com.freader", "Book fetched");
		activity.callback(pagedBook);
	}

}
