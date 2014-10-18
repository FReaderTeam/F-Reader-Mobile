
package ebook.parser;

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
	public void parse(String fileName) {
		this.parse(fileName, false);
	}

	/**
	 * @param fileName
	 * @param extractCover
	 * @return
	 */
	public void parse(String fileName, boolean extractCover) {
		this.eBook = new EBook();
		this.eBook.fileName = fileName;
		this.eBook.doExtractCover = extractCover;
		this.eBook.isOk = false;
		this.parseFile();
	}

	abstract protected void parseFile();

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
