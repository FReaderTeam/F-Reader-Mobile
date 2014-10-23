package com.freader.bookmodel;

import java.util.ArrayList;

import com.freader.bookprototype.ScreenSlideActivity;
import com.freader.bookprototype.ScreenSlideWaiting;

import android.util.Log;
import android.widget.TextView;



public class ParsedBook extends Book{

	ArrayList<String> paragraphs;
	TextView textView;
	ScreenSlideWaiting activity;
	
	int lines_num;
	
	public ParsedBook(String t, String a, ArrayList<String> p) {
		super(t, a);
		paragraphs = p;
	}
	
	public void initPages(int lines_num, TextView textView) {
		this.lines_num = lines_num - 2;
		this.textView = textView;
		Log.w("linesnum",String.valueOf(lines_num));
		new BookFetchAsync().execute(this);
	}
	
	public CharSequence getFirstPages(){
		CharSequence result = " ";
		Log.w("freader",String.valueOf(paragraphs.size()));
		for (int i = 0; i<10; i++){
			if (i==paragraphs.size())
				break;
			result= result + paragraphs.get(i);
		}
		return result;
	}
	
	public void setTextView(TextView textView) {
		this.textView = textView;
	}

	public void setActivity(ScreenSlideWaiting screenSlideWaiting) {
		this.activity = screenSlideWaiting;
	}

}
