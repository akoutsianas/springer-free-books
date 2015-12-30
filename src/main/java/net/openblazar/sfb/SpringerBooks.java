package net.openblazar.sfb;

import org.apache.commons.io.FileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class SpringerBooks {

	public static final int ITEM_TITLE = 0;
	public static final int PUB_TITLE = 1;
	public static final int SERIE_TITLE = 2;
	public static final int VOLUME = 3;
	public static final int ISSUE = 4;
	public static final int DOI = 5;
	public static final int AUTHORS = 6;
	public static final int YEAR = 7;
	public static final int BOOK_URL = 8;
	public static final int TYPE = 9;

	public static void main(String[] args) {
		if (args.length == 0) {
			System.out.println("Missing arguments, available args: \n");
			System.out.println(" [1.] paths to search results");
			return;
		}

		SpringerBooks springerBooks = new SpringerBooks();
		springerBooks.proccesResults(args);
	}

	public void proccesResults(String[] paths) {
		for (String path : paths) {
			int counter = 0;
			try (BufferedReader br = new BufferedReader(new FileReader(path))) {
				String currentLine;

				while ((currentLine = br.readLine()) != null) {
					try {
						if (counter == 0) {
							counter++;
							continue;
						}

						downloadFile(currentLine);
						counter++;
					} catch (IOException e) {
						System.err.println("Could not download book " + currentLine);
						e.printStackTrace();
					}
				}
			} catch (IOException e) {
				System.err.println("Couln not read file " + path);
			}
		}
	}

	public void downloadFile(String line) throws IOException {
		String[] elements = line.split(",");
		FileUtils.copyURLToFile(toURL(elements[BOOK_URL]), toFile(elements[ITEM_TITLE],
				elements[AUTHORS], elements[YEAR]));
	}

	public URL toURL(String path) throws MalformedURLException {
		path = path.replaceAll("^\"|\"$", "");
		path = path.replaceAll("book", "content/pdf");
		return new URL(path + ".pdf");
	}

	public File toFile(String title, String authors, String year) {
		title = title.replaceAll("^\"|\"$", "");
		authors = authors.replaceAll("^\"|\"$", "");
		year = year.replaceAll("^\"|\"$", "");
		return new File(title + " - " + authors + "(" + year + ").pdf");
	}

}
