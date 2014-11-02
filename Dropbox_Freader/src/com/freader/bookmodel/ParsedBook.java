package com.freader.bookmodel;

import java.util.ArrayList;

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
	

	
	public void initPages(int lines_num) {
		this.lines_num = lines_num - 2;
		Log.w("linesnum",String.valueOf(lines_num));
		new BookFetchAsync().execute(this);
	}
	
	public CharSequence getFirstPages(){
		CharSequence result = " ";
		Log.w("freader",String.valueOf(getSize()));
		for (int i = 0; i<10; i++){
			if (i==getSize())
				break;
			result= result + getParagraph(i);
		}
		return result;
	}
	
	public void setTextView(TextView textView) {
		this.textView = textView;
	}

	public void setActivity(ScreenSlideWaiting screenSlideWaiting) {
		this.activity = screenSlideWaiting;
	}

	public int getSize(){
		return paragraphs.size();
	}
	
	public String getParagraph(int index){
		return paragraphs.get(index);
	}


}
