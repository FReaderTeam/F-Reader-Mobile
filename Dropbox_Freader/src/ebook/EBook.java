package ebook;

import java.util.ArrayList;
import java.util.List;

/**
 * EBook - class that contains fields describing the parameters and 
 * properties of the e-book
 */
public class EBook {
	/**
	 * True, if the processing of the e-book was successful. 
	 * False if the e-book could not be processed.
	 */
	public boolean isOk;
	/**
	 * The name of the file containing the processing e-book
	 */
	public String fileName;
	/**
	 * The name of the internal format of the e-book
	 */
	public EBookFormat format;
	/**
	 * List of authors of the e-book
	 */
	public ArrayList<Person> authors;
	/**
	 * Title of the-ebook
	 */
	public String title;
	/**
	 * Genre of the book according to fb2 format
	 */
	public List<String> fb2Genres;
	/**
	 * the language in which the e-book was published
	 */
	public String language;
	/**
	 * the language of the e-book source
	 */
	public String srcLanguage;
	/**
	 * List of translators of the e-book
	 */
	public ArrayList<Person> translators;
	/**
	 * The name of the series, which includes the e-book
	 */
	public String sequenceName;
	/**
	 * Serial number of the e-book in the series
	 */
	public String sequenceNumber;
	/**
	 * Charset of the e-book text
	 */
	public String encoding;
	/**
	 * Brief summary of the e-book
	 */
	public String annotation;
	/**
	 * Picture of e-book cover
	 */
	public byte[] cover;
	
	public ArrayList<String> parsedBook;
	
	public boolean doExtractCover;
	/**
	 * The class constructor, fills the fields with null values
	 */
	public EBook() {
		this.authors = new ArrayList<Person>(3);
		this.fb2Genres = new ArrayList<String>(2);
		this.translators = new ArrayList<Person>(2);
		this.isOk = false;
	}
}
