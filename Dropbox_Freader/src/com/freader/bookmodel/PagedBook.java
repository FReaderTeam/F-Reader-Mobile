package com.freader.bookmodel;

import java.util.HashMap;
import java.util.ArrayList;

public class PagedBook extends Book {

	public static ArrayList<String> pages;
	private int currentPage;
	private HashMap<Integer, Integer> paragraphsToPages;
	
	public PagedBook(String t, String a, ArrayList<String> p, HashMap<Integer,Integer> paragraphs_to_pages) {
		super(t, a);
		pages = p;
		currentPage = 1;
		this.paragraphsToPages = paragraphs_to_pages;
	}
	
	public int getPagesNum(){
		return pages.size();
	}
	
	public ArrayList<String> getPages(){
		return pages;
	}
	
	public String getCurrentPage(){
		return pages.get(currentPage-1);
	}
	
	public HashMap<Integer, Integer> getHashMap(){
		return paragraphsToPages;
	}
	
	public String getPage(int index){
		currentPage = index;
		return pages.get(index-1);
	}
	
}
