package com.freader.bookmodel;

import java.util.HashMap;
import java.util.ArrayList;

import com.freader.bookprototype.ScreenSlideWaiting;

import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.widget.TextView;
import android.annotation.SuppressLint;
import android.os.AsyncTask;

@SuppressLint("UseSparseArrays")
public class BookFetchAsync extends AsyncTask<ParsedBook, Void, ArrayList<CharSequence>> {

	private ScreenSlideWaiting activity;
	private HashMap<Integer, Integer> hm;
	
	
	@Override
	protected ArrayList<CharSequence> doInBackground(ParsedBook... params) {
		ArrayList<CharSequence> pages = new ArrayList<CharSequence>();
		int start_paragraph = 0;
		String page = new String();
		String add = " ", previous = add;
		int lines = 0;
		ArrayList<String> words;
		int p = 0;
		hm = new HashMap<Integer, Integer>();
		for (int k = start_paragraph; k < params[0].getSize(); k++) { // Start walking through paragraphs
			hm.put(k, p);
			words = getWordList(params[0].getParagraph(k)); // Split a paragraph to words
			for (int i = 0; i < words.size(); i++) {
				if (words.get(i).equals("%new-section")){
					page += add;						
					pages.add(page);
					p++;
					page = new String();
					lines = 0;
					continue;
				}
				if (words.get(i).equals("%title")){
					add+="    ";
				}
				else
					add += " " + words.get(i); // Add a word each time
				if (isTooLarge(add, params[0].textView)) { // Check if the line is longer than the page width
					if (lines == params[0].lines_num - 1) { // If page is over, create new page
						page += add;						
						add = words.get(i); // Add one word
						pages.add(page);
						p++;
						page = new String();
						lines = 0;
					} else {
						lines++;
						page += previous;
						add = "";
					}
				}
				previous = add;
			}
			if (lines < params[0].lines_num - 1) { // If there's enough space on page for another paragraph, go on.
				page += add;
				page += '\n';
				add = " ";
				lines++;
			} else { // If there isn't, initialize new page
				page += add;
				pages.add(page);
				p++;
				page = new String();
				lines = 0;
			}

		}
		if (page != "") {
			pages.add(page);
			p++;
		}
		
		
		Log.w("BookFetch", "Fetched, last page " + pages.get(pages.size() - 4));
		activity = params[0].activity;

		return pages;
	}

	private ArrayList<String> getWordList(String line) {
		ArrayList<String> result = new ArrayList<String>();
		int i = 0;
		String word;
		while (i < line.length()) {
			word = "";
			while (i < line.length() && line.charAt(i) == ' ') {
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

	protected void onPostExecute(ArrayList<CharSequence> pages) {
		Log.w("com.freader", "Book fetched");
		activity.callback(pages, hm);
	}

}
