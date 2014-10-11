package ua.kharkiv.knure.bookmodel;

import java.util.ArrayList;
import android.widget.TextView;
import android.os.AsyncTask;
import ua.kharkiv.khnure.bookprototype.ScreenSlideActivity;

public class BookFetchAsync extends AsyncTask<ParsedBook, Integer, PagedBook> {

	private ScreenSlideActivity activity;
	
	@Override
	protected PagedBook doInBackground(ParsedBook... params) {
		ArrayList<String> pages = new ArrayList<String>();
		int start_paragraph = 0;
		String page = new String();
		String add = "  ";

		int lines = 0;
		ArrayList<String> words;
		int p = 0;
		for (int k = start_paragraph; k < params[0].paragraphs.size(); k++) {
			words = getWordList(params[0].paragraphs.get(k));
			for (int i = 0; i < words.size(); i++) {
				add += words.get(i);
				if (isTooLarge(add, params[0].textView)) {
					lines++;
					if (lines == params[0].lines_num) {
						page += add;
						break;
					}
				}
			}
			if (lines < params[0].lines_num - 1) {
				page += add;
				page += '\n';
				add = "  ";
				continue;
			} else {
				pages.set(p, page);
				page = new String();
				lines = 0;
				p++;
			}
		}
		activity=params[0].activity;
		return new PagedBook(params[0].getAuthor(), params[0].getTitle(), pages);
	}
	
	private ArrayList<String> getWordList(String line) {
		ArrayList<String> result = new ArrayList<String>();
		int i = 0;
		String word;
		while (i < line.length()) {
			word = "";
			if (line.charAt(i) == ' ')
				i++;
			while (line.charAt(i) != ' ' && i < line.length())
				word += line.charAt(i++);
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
