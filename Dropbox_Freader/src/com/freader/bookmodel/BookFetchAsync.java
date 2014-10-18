package com.freader.bookmodel;

import java.util.ArrayList;

import com.freader.bookprototype.ScreenSlideActivity;
import com.freader.bookprototype.ScreenSlideWaiting;

import android.util.Log;
import android.widget.TextView;
import android.os.AsyncTask;
import android.util.Log;
import java.util.HashMap;

public class BookFetchAsync extends AsyncTask<ParsedBook, Integer, PagedBook> {

	private ScreenSlideWaiting activity;

	@Override
	protected PagedBook doInBackground(ParsedBook... params) {
		ArrayList<String> pages = new ArrayList<String>();
		HashMap<Integer, Integer> hm = new HashMap<Integer, Integer>();
		int start_paragraph = 0;
		String page = new String();
		String add = " ", previous = add;
		int lines = 0;
		ArrayList<String> words;
		int p = 0;
		for (int k = start_paragraph; k < params[0].paragraphs.size(); k++) { // Start walking through paragraphs
			hm.put(k, p);
			words = getWordList(params[0].paragraphs.get(k)); // Split a paragraph to words
			for (int i = 0; i < words.size(); i++) {
				add += " " + words.get(i); // Add a word each time
				
				if (isTooLarge(add, params[0].textView)) { // Check if the line is longer than the page width
					if (lines == params[0].lines_num-1) { // If page is over, create new page
						page += add;
						add = words.get(i); // Add one missed word
						pages.add(page);
						p++;
						page = new String();
						lines = 0;
					}
					else{
						lines++;
						//page += add; //WORDS GET NOT COUNTED
						page += previous;
						add = "";
						previous = "";
					}
				}
				previous = add;
			}
			if (lines < params[0].lines_num - 1) { // If there's enough space on page for another paragraph, go on.
				page += add;
				page += '\n';
				add = " ";
				lines++;
				continue;
			} else { // If there isn't, initialize new page
				pages.add(page);
				p++;
				page = new String();
				lines = 0;
			}
		}
		Log.w("BookFetch", "Fetched, Pages num: " + String.valueOf(p));
		activity = params[0].activity;

		return new PagedBook(params[0].getAuthor(), params[0].getTitle(), pages);
	}

	private ArrayList<String> getWordList(String line) {
		ArrayList<String> result = new ArrayList<String>();
		int i = 0;
		String word;
		while (i < line.length()) {
			word = "";
			while (line.charAt(i) == ' ' && i < line.length()) {
				i++;
			}

			if (i == line.length())
				break;

			while (i < line.length() && line.charAt(i) != ' ')
				word += line.charAt(i++);
			result.add(word);
		}
		return result;
	}

	private boolean isTooLarge(String newText, TextView textView) {
		float textWidth = textView.getPaint().measureText(newText);

		if (textWidth >= textView.getMeasuredWidth()) {
			return true;
		}
		return false;
	}

	protected void onPostExecute(PagedBook pb) {
		Log.w("com.freader", "Book fetched");
		activity.callback(pb);
	}

}
