package ua.kharkiv.knure.bookmodel;

import java.util.ArrayList;

public class PagedBook extends Book {

	private ArrayList<String> pages;
	private int current_page;
	
	public PagedBook(String t, String a, ArrayList<String> p) {
		super(t, a);
		pages = p;
		current_page = 1;
	}
	
	public int getPagesNum(){
		return pages.size();
	}
	
	public String getCurrentPage(){
		return pages.get(current_page-1);
	}
	
	public String getPage(int index){
		current_page = index;
		return pages.get(index-1);
	}
	
}
