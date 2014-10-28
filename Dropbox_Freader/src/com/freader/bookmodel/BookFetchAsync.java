package com.freader.bookmodel;

import java.util.HashMap;
import java.util.ArrayList;

import com.freader.bookprototype.ScreenSlideWaiting;

import android.util.Log;
import android.widget.TextView;
import android.annotation.SuppressLint;
import android.os.AsyncTask;

@SuppressLint("UseSparseArrays")
public class BookFetchAsync extends AsyncTask<ParsedBook, Void, ArrayList<CharSequence>> {

	private ScreenSlideWaiting activity;
	private HashMap<Integer, Integer> paragraphsToPages;
	private int bufferSize = 1024*10;
	
	@Override
	protected ArrayList<CharSequence> doInBackground(ParsedBook... params) {
		ArrayList<CharSequence> pages = new ArrayList<CharSequence>();
		int startParagraph = 0;
		StringBuilder pageBuilder = new StringBuilder(bufferSize),
					  add = new StringBuilder(" "), 
					  previous = add;
		int lines = 0;
		String[] words;
		int pageNumber = 0;
		paragraphsToPages = new HashMap<Integer, Integer>();
		 // Start walking through paragraphs
		for (int par = startParagraph; par < params[0].getSize(); par++) {
			paragraphsToPages.put(par, pageNumber);
			// Split a paragraph to words
			words = params[0].getParagraph(par).split(" "); 
			for (String word : words) {
				if (word.equals("%new-section")){
					pageBuilder.append(add);						
					pages.add(pageBuilder.toString());
					pageNumber++;
					pageBuilder.setLength(0);
					lines = 0;
					continue;
				}
				if (word.equals("%title")){
					add.append("\t");
				}
				// Add a word each time
				else {
					add.append(" ").append(word); 
				}
				// Check if the line is longer than the page width
				if (isTooLarge(add, params[0].textView)) { 
					// If page is over, create new page
					if (lines == params[0].lines_num - 1) { 
						pageBuilder.append(add);						
						add.setLength(0);
						add.append(word); // Add one word
						pages.add(pageBuilder.toString());
						pageNumber++;
						pageBuilder.setLength(0);
						lines = 0;
					} else {
						lines++;
						pageBuilder.append(previous);
						add.setLength(0);
					}
				}
				previous = add;
			}
			// If there's enough space on page for another paragraph, go on.
			if (lines < params[0].lines_num - 1) { 
				pageBuilder.append(add);
				pageBuilder.append('\n');
				add.setLength(0);
				add.append(" ");
				lines++;
				
			} // If there isn't, initialize new page
			else { 
				pageBuilder.append(add);
				pages.add(pageBuilder.toString());
				pageNumber++;
				pageBuilder.setLength(0);
				lines = 0;
			}

		}
		if (pageBuilder.length() != 0) {
			pages.add(pageBuilder.toString());
			pageNumber++;
		}
		
		
		Log.w("BookFetch", "Fetched, last page " + pages.get(pages.size() - 4));
		activity = params[0].activity;

		return pages;
	}

	private boolean isTooLarge(StringBuilder newText, TextView textView) {
		float textWidth = textView.getPaint().measureText(newText.toString());

		if (textWidth >= textView.getMeasuredWidth()) {
			return true;
		}
		return false;
	}

	protected void onPostExecute(ArrayList<CharSequence> pages) {
		Log.w("com.freader", "Book fetched");
		activity.callback(pages, paragraphsToPages);
	}

}
