package ua.kharkiv.knure.bookmodel;

public class Book {
	private String title;
	private String author;

	public Book(String t, String a) {
		title = t;
		author = a;
	}

	public String getTitle() {
		return title;
	}

	public String getAuthor() {
		return author;
	}

}
