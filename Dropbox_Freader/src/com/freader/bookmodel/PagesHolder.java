package com.freader.bookmodel;

import java.util.ArrayList;

public class PagesHolder {

	private ArrayList<String> paragraphs;

	private ArrayList<CharSequence> pages;

	private static final PagesHolder INSTANCE = new PagesHolder();

	public static PagesHolder getInstance() {
		return INSTANCE;
	}

	public ArrayList<String> getParagraphs() {
		return paragraphs;
	}

	public void setParagraphs(ArrayList<String> pages) {
		this.paragraphs = pages;
	}

	public ArrayList<CharSequence> getPages() {
		return pages;
	}

	public void setPages(ArrayList<CharSequence> pages) {
		this.pages = pages;
	}

}
