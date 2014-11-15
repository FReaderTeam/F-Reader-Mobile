package com.freader.bookmodel;

import java.util.ArrayList;

import com.freader.bookprototype.ScreenSlideWaiting;

import android.util.Log;
import android.widget.TextView;

public class ParsedBook extends Book {

	private static final int MAX_FIRST_PAGES_COUNT = 10;
	final ArrayList<String> paragraphs;
	TextView textView;
	ScreenSlideWaiting activity;

	int lines_num;

	public ParsedBook(String title, String author, ArrayList<String> parapgraphs) {
		super(title, author);
		paragraphs = parapgraphs;
	}

	public CharSequence getFirstPages() {
		CharSequence result = " ";
		Log.w("freader", String.valueOf(getSize()));
		for (int i = 0; i < MAX_FIRST_PAGES_COUNT; i++) {
			if (i == getSize())
				break;
			result = result + getParagraph(i);
		}
		return result;
	}

	public void setTextView(TextView textView) {
		this.textView = textView;
	}

	public void setActivity(ScreenSlideWaiting screenSlideWaiting) {
		this.activity = screenSlideWaiting;
	}

	public int getSize() {
		return paragraphs.size();
	}

	public String getParagraph(int index) {
		return paragraphs.get(index);
	}

}
