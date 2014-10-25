package com.freader.bookmodel;

import java.util.HashMap;
import java.util.ArrayList;

public class PagedBook extends Book {

	public static ArrayList<String> pages;
	private int current_page;
	private HashMap<Integer, Integer> paragraphs_to_pages;
	
	public PagedBook(String t, String a, ArrayList<String> p, HashMap<Integer,Integer> paragraphs_to_pages) {
		super(t, a);
		pages = p;
		current_page = 1;
		this.paragraphs_to_pages = paragraphs_to_pages;
	}
	
	public int getPagesNum(){
		return pages.size();
	}
	
	public ArrayList<String> getPages(){
		return pages;
	}
	
	public String getCurrentPage(){
		return pages.get(current_page-1);
	}
	
	public HashMap<Integer, Integer> getHashMap(){
		return paragraphs_to_pages;
	}
	
	public String getPage(int index){
		current_page = index;
		return pages.get(index-1);
	}
	
}
