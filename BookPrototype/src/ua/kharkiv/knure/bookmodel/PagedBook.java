package ua.kharkiv.knure.bookmodel;

import java.util.ArrayList;

public class PagedBook extends Book {

	private ArrayList<CharSequence> pages;
	private int current_page;
	
	public PagedBook(String t, String a, ArrayList<CharSequence> p) {
		super(t, a);
		pages = p;
		current_page = 1;
	}
	
	public int getPagesNum(){
		return pages.size();
	}
	
	public CharSequence getCurrentPage(){
		return pages.get(current_page-1);
	}
	
	public CharSequence getPage(int index){
		current_page = index;
		return pages.get(index-1);
	}
	
}
