
package ebook.parser;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import ebook.EBook;

/**
 * Parser - abstract class from which the e-books handlers are created
 */
abstract public class Parser {
	protected EBook eBook;

	/**
	 * Handles the e-book extracts contained therein meta-information
	 * 
	 * @param fileName
	 *            - the name of the file to be processed
	 * @return - instance of the class EBook with the fields filled with e-book
	 *         meta-information
	 */
	public EBook parse(String fileName) {
		return this.parse(fileName, false);
	}

	/**
	 * @param fileName
	 * @param extractCover
	 * @return
	 */
	public EBook parse(String fileName, boolean extractCover) {
		this.eBook = new EBook();
		this.eBook.fileName = fileName;
		this.eBook.doExtractCover = extractCover;
		this.eBook.isOk = false;
		this.parseFile();
		return this.eBook;
	}

	abstract protected void parseFile();

	abstract public ArrayList<String> getBookBody()
			throws FileNotFoundException, IOException;

	/**
	 * Returns instance of the class EBook with the fields filled with e-book
	 * meta-information
	 * 
	 * @return - instance of the class EBook
	 */
	public EBook getEBoook() {
		return this.eBook;
	}
}
