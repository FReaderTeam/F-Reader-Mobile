package ua.kharkiv.knure.bookmodel;

import java.util.ArrayList;

import ua.kharkiv.khnure.bookprototype.ScreenSlideActivity;
import android.widget.TextView;



public class ParsedBook extends Book{

	ArrayList<String> paragraphs;
	TextView textView;
	ScreenSlideActivity activity;
	
	int lines_num;
	
	public ParsedBook(String t, String a, ArrayList<String> p) {
		super(t, a);
		paragraphs = p;
	}
	
	public void initPages(int lines_num, TextView textView) {
		this.lines_num = lines_num - 1;
		this.textView = textView;

	}
	
	public void setTextView(TextView textView) {
		this.textView = textView;
	}

	public void setActivity(ScreenSlideActivity activity) {
		this.activity = activity;
	}

}
