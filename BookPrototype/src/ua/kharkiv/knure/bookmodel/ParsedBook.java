package ua.kharkiv.knure.bookmodel;

import java.util.ArrayList;

import ua.kharkiv.khnure.bookprototype.ScreenSlideActivity;
import android.widget.TextView;



public class ParsedBook extends Book{

	ArrayList<CharSequence> paragraphs;
	TextView textView;
	ScreenSlideActivity activity;
	
	int lines_num;
	
	public ParsedBook(String t, String a, ArrayList<CharSequence> p) {
		super(t, a);
		paragraphs = p;
	}
	
	public void setTextView(TextView tv){
		textView=tv;
	}
	
	public void setActivity(ScreenSlideActivity a){
		activity = a;
	}
	
	public void initPages(int lines_num, TextView textView) {
		this.lines_num = lines_num - 1;
		this.textView = textView;
		new BookFetchAsync().execute(this);
	}

}
