package ebook;

/**
 * EBookFormat - Enumerations that contains a description of the format 
 * structure of the e-book
 */
public enum EBookFormat {
	/**
	 *  The format of the e-book is not supported in current version 
	 */
	UNSUPPORTED,
	/**
	 * Electronic publication - is a free and open e-book standard by 
	 * the International Digital Publishing Forum (IDPF) 
	 */
	EPUB,
	/**
	 * FictionBook is an open XML-based e-book format.
	 */
	FB2
}
