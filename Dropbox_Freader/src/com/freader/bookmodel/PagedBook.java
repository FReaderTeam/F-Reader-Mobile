package com.freader.bookmodel;

import java.util.ArrayList;
import java.util.HashMap;

public class PagedBook extends Book {

	private ArrayList<String> pages;
	private int current_page;
	private HashMap<Integer, Integer> paragraphs_to_pages;
	
	public PagedBook(String t, String a, ArrayList<String> p) {
		super(t, a);
		pages = p;
		current_page = 1;
	}
	
	public int getPagesNum(){
		return pages.size();
	}
	
	public HashMap<Integer, Integer> getHashMap(){
		return paragraphs_to_pages;
	}
	
	public ArrayList<String> getPages(){
		return pages;
	}
	
	public String getCurrentPage(){
		return pages.get(current_page-1);
	}
	
	public String getPage(int index){
		current_page = index;
		return pages.get(index-1);
	}
	
}
