package ua.kharkiv.knure.bookmodel;

import java.util.ArrayList;
import android.widget.TextView;
import android.os.AsyncTask;
import ua.kharkiv.khnure.bookprototype.ScreenSlideActivity;

public class BookFetchAsync extends AsyncTask<ParsedBook, Integer, PagedBook> {

	private ScreenSlideActivity activity;
	
	@Override
	protected PagedBook doInBackground(ParsedBook... params) {
		ArrayList<CharSequence> pages = new ArrayList<CharSequence>();
		int start_paragraph = 0;
		String page = new String();
		String add = "  ";

		int lines = 0;
		ArrayList<String> words;
		int p = 0;
		for (int k = start_paragraph; k < params[0].paragraphs.size(); k++) { //Start walking through paragraphs
			words = getWordList(params[0].paragraphs.get(k)); //Split a paragraph to words
			for (int i = 0; i < words.size(); i++) {
				add += words.get(i); //Add a word each time
				if (isTooLarge(add, params[0].textView)) { //Check if the line is longer than the page width
					lines++;
					if (lines == params[0].lines_num) { //If page is over, create new page
						page += add; 
						add = words.get(i); //Add one missed word
						pages.set(p++, page);
						page = new String();
						lines = 0;
					}
				}
			}
			if (lines < params[0].lines_num - 1) { //If there's enough space on page for another paragraph, go on.
				page += add;
				page += '\n';
				add = "  ";
				continue;
			} else { //If there isn't, initialize new page
				pages.set(p++, page);
				page = new String();
				lines = 0;
			}
		}
		activity=params[0].activity;
		return new PagedBook(params[0].getAuthor(), params[0].getTitle(), pages);
	}
	
	private ArrayList<String> getWordList(CharSequence charSequence) {
		ArrayList<String> result = new ArrayList<String>();
		int i = 0;
		String word;
		while (i < charSequence.length()) {
			word = "";
			if (charSequence.charAt(i) == ' ')
				i++;
			while (charSequence.charAt(i) != ' ' && i < charSequence.length())
				word += charSequence.charAt(i++);
			result.add(word);
		}
		return result;
	}
	
	private boolean isTooLarge(String newText, TextView textView) {
		float textWidth = textView.getPaint().measureText(newText);
		return (textWidth >= textView.getMeasuredWidth());
	}

	 protected void onPostExecute(PagedBook pb) {
		 activity.callback(pb);
     }


}
