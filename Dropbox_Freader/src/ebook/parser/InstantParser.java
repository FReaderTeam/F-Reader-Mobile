package ebook.parser;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import ebook.EBookFormat;

/**
 * InstantParser - very fast, instant handler of the information contained in
 * the files of e-books
 */
public class InstantParser extends ebook.parser.Parser {
	protected void parseFile() {
		if (SOP.fb2File.matcher(this.eBook.fileName).matches()) {
			this.eBook.format = EBookFormat.FB2;
			this.parseFb2();
		} else if (SOP.fb2zipFile.matcher(this.eBook.fileName).matches()) {
			this.eBook.format = EBookFormat.FB2;
			this.parseFb2Zip();
		} else {
			this.eBook.format = EBookFormat.UNSUPPORTED;
		}
	}

	private void parseFb2() {
		try {
			InputStream inputStream = new FileInputStream(this.eBook.fileName);
			Fb2InstantParser parser = new Fb2InstantParser(this.eBook,
					inputStream);
			parser.parse();
			this.eBook.parsedBook = parser.getBookBody();
			inputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void parseFb2Zip() {
		try {
			ZipFile zipFile = new ZipFile(this.eBook.fileName);
			ZipEntry entry = zipFile.entries().nextElement();
			InputStream inputStream = zipFile.getInputStream(entry);
			Fb2InstantParser parser = new Fb2InstantParser(this.eBook,
					inputStream);
			parser.parse();
			this.eBook.parsedBook = parser.getBookBody();
			inputStream.close();
			zipFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
