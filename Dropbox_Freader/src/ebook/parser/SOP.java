package ebook.parser;

import java.util.regex.Pattern;

/**
 * Store of Patterns
 */
class SOP {
	protected static Pattern fb2File;
	protected static Pattern fb2zipFile;
	protected static Pattern epubFile;
	protected static Pattern opfFile;
	protected static Pattern xmlEncoding;
	protected static Pattern fb2FirstName;
	protected static Pattern fb2MiddleName;
	protected static Pattern fb2LastName;
	protected static Pattern fb2Author;
	protected static Pattern fb2BookTitle;
	protected static Pattern fb2genre;
	protected static Pattern fb2Language;
	protected static Pattern fb2Sequence;
	protected static Pattern fb2SequenceName;
	protected static Pattern fb2SequenceNumber;
	protected static Pattern fb2Annotation;
	protected static Pattern fb2CoverName;
	protected static Pattern fb2Body;
	protected static Pattern fb2Section;
	protected static Pattern fb2Title;
	protected static Pattern fb2Paragraph;
	protected static Pattern fb2Epigraph;
	protected static Pattern fb2EpigraphAuthor;
	protected static Pattern fb2PreBodyEpigraphs;
	protected static Pattern epubDescription;
	protected static Pattern epubTitle;
	protected static Pattern epubAuthor;
	protected static Pattern epubLanguage;
	protected static Pattern epubGenre;
	protected static Pattern epubCover;	
	//
	//
	static {
		fb2File = Pattern.compile("(?i).*fb2$");
		fb2zipFile = Pattern.compile("(?i).*fb2\\.zip$");
		epubFile = Pattern.compile("(?i).*epub$");
		opfFile = Pattern.compile("(?i).*opf$");
		xmlEncoding = Pattern.compile("(?i).*encoding=[\"'](.*?)[\"'].*");
		fb2FirstName = Pattern.compile("(?s)<first-name>(.*)</first-name>");
		fb2MiddleName = Pattern.compile("(?s)<middle-name>(.*)</middle-name>");
		fb2LastName = Pattern.compile("(?s)<last-name>(.*)</last-name>");
		fb2Author = Pattern.compile("(?s)<author>(.*?)</author>");
		fb2BookTitle = Pattern.compile("(?s)<book-title>(.*?)</book-title>");
		fb2genre = Pattern.compile("(?s)<genre>(.*?)</genre>");
		fb2Language = Pattern.compile("(?s)<lang>(.*?)</lang>");
		fb2Sequence = Pattern.compile("(?s)<sequence(.*)>");
		fb2SequenceName = Pattern.compile("name=\"(.*?)\"");
		fb2SequenceNumber = Pattern.compile("number=\"(.*?)\"");
		fb2Annotation = Pattern.compile("(?s)<annotation>(.*?)</annotation>");
		fb2CoverName = Pattern
				.compile("(?s)<coverpage>.*href=\"#(.*?)\".*</coverpage>");
		fb2Body = Pattern.compile("(?s)<body>(.*?)</body>");
		fb2Section = Pattern.compile("(?s)<section>(.*?)</section>");
		fb2Title = Pattern.compile("(?s)<title>(.*?)</title>");
		fb2Paragraph = Pattern
				.compile("(?s)(<p>(.*?)</p>)|(?s)(<subtitle>(.*?)</subtitle>)|(?s)(<text-author>(.*?)</text-author>)");
		fb2Epigraph = Pattern.compile("(?s)<epigraph>(.*?)</epigraph>");
		fb2EpigraphAuthor = Pattern
				.compile("(?s)<text-author>(.*?)</text-author>");
		fb2PreBodyEpigraphs = Pattern.compile("(?s)</title>(.*?)<section>");

		epubDescription = Pattern
				.compile("(?s)<dc:description>(.*?)</dc:description>");
		epubTitle = Pattern.compile("(?s)<dc:title>(.*?)</dc:title>");
		epubAuthor = Pattern.compile("(?s)<dc:creator.*?>(.*?)</dc:creator>");
		epubLanguage = Pattern
				.compile("(?s)<dc:language.*?>(.*?)</dc:language>");
		epubGenre = Pattern.compile("(?s)<dc:subject.*?>(.*?)</dc:subject>");
		epubCover = Pattern.compile("(?s)<embeddedcover>(.*?)</embeddedcover>");
	}

}
