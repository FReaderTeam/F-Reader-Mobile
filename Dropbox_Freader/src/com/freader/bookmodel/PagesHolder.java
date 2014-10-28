package com.freader.bookmodel;

import java.util.ArrayList;

public class PagesHolder {
	private ArrayList<String> pages;

	public ArrayList<String> getData() {
		return pages;
	}

	public void setData(ArrayList<String> pages) {
		this.pages = pages;
	}
	
	public boolean hasPages(){
		return pages != null;
	}

	private static final PagesHolder holder = new PagesHolder();

	public static PagesHolder getInstance() {
		return holder;
	}
}
