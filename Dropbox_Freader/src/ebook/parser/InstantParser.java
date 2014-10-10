
package ebook.parser;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
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
		}
		if (SOP.fb2zipFile.matcher(this.eBook.fileName).matches()) {
			this.eBook.format = EBookFormat.FB2;
			this.parseFb2Zip();
		}
		if (SOP.epubFile.matcher(this.eBook.fileName).matches()) {
			this.eBook.format = EBookFormat.EPUB;
			this.parseEpub();
		} else {
			this.eBook.format = EBookFormat.UNSUPPORTED;
		}
	}

	public ArrayList<String> getBookBody() throws IOException {

		InputStream inputStream = new FileInputStream(this.eBook.fileName);
		try {
			Fb2InstantParser parser = new Fb2InstantParser(this.eBook,
					inputStream);
			return parser.getBookBody();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			inputStream.close();
		}
		return null;

	}

	private void parseFb2() {
		try {
			InputStream inputStream = new FileInputStream(this.eBook.fileName);
			Fb2InstantParser parser = new Fb2InstantParser(this.eBook,
					inputStream);
			parser.parse();
			inputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
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
			inputStream.close();
			zipFile.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void parseEpub() {
		try {
			EpubInstantParser parser = new EpubInstantParser(this.eBook);
			parser.parse();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
