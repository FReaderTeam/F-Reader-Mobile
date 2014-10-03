package ua.kharkiv.nure.freader;

import android.graphics.Bitmap;

public class Book {

	private String name;
	private String author;
	private Bitmap bit;

	public Book(){}
	public Book(String name, String author){
		this.name=name;
		this.author=author;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAuthor(){
		return author;
	}
	
	public void setAuthor(String author){
		this.author = author;
	}

	public Bitmap getBitmap() {
		return bit;
	}

	public void setBitmap(Bitmap bitmap) {
		bit = bitmap;
	}

}
