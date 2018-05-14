package utilities;

import java.io.IOException;
import java.net.SocketTimeoutException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public final class UrbanDictionary {
	public static String define(String term) throws IOException {
		String definition = null;
		Document doc = null;
		try {
			doc = Jsoup.connect("http://www.urbandictionary.com/define.php?term=" + term).get();
			definition = doc.select("div.meaning").first().toString();
		} catch (SocketTimeoutException e) {
			definition = "*Error - ** there was a problem connecting with the webserver socket; (should work if you try again) Try again please!";
		}
		System.out.println(definition);
		System.out.println("*******************" + Jsoup.parse(definition).text() + "\n\n\n\n");

		return (Jsoup.parse(definition).text() != null) || (Jsoup.parse(definition).text().length() > 0)
				? Jsoup.parse(definition).text()
				: "[PROBLEM PARSING - CONTACT DEVELOPER]";
	}

	public static String examples(String term) throws IOException {
		String example = null;
		Document doc = null;
		try {
			doc = Jsoup.connect("http://www.urbandictionary.com/define.php?term=" + term).get();
			try {
				example = doc.select("div.example").first().toString();
			} catch (NullPointerException e) {
				return "No example(s) found!";
			}
		} catch (SocketTimeoutException e) {
			example = "**Error - ** there was a problem connecting with the webserver socket; (should work if you try again) Try again please!";
		}
		return (Jsoup.parse(example).text() != null) || (Jsoup.parse(example).text().length() > 0)
				? Jsoup.parse(example).text()
				: "No examples found.";
	}
}
