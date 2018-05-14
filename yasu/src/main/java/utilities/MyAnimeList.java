package utilities;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public final class MyAnimeList {
	private static String url_anime = "https://myanimelist.net/anime.php?q=";
	private static String url_manga = "https://myanimelist.net/manga.php?q=";

	private static String internalSearch(String search) {
		String msg = "**Error** - API returned nothing :/";
		Document doc = null;
		try {
			doc = Jsoup.connect(search).get();
			Element result = doc.select("#content").first();

			msg = "Anime/Manga Score: " + result.select(
					"#content > table > tbody > tr > td:nth-child(2) > div.js-scrollfix-bottom-rel > table > tbody > tr:nth-child(1) > td > div.pb16 > div.di-t.w100.mt12 > div.anime-detail-header-stats.di-tc.va-t > div.stats-block.po-r.clearfix > div.fl-l.score")
					.text() + "\n"
					+ result.select(
							"#content > table > tbody > tr > td:nth-child(2) > div.js-scrollfix-bottom-rel > table > tbody > tr:nth-child(1) > td > div.pb16 > div.di-t.w100.mt12 > div.anime-detail-header-stats.di-tc.va-t > div.stats-block.po-r.clearfix > div.di-ib.ml12.pl20.pt8 > span.numbers.ranked")
							.text()
					+ "\n"
					+ result.select(
							"#content > table > tbody > tr > td:nth-child(2) > div.js-scrollfix-bottom-rel > table > tbody > tr:nth-child(1) > td > div.pb16 > div.di-t.w100.mt12 > div.anime-detail-header-stats.di-tc.va-t > div.stats-block.po-r.clearfix > div.di-ib.ml12.pl20.pt8 > span.numbers.popularity")
							.text()
					+ "\n"
					+ result.select(
							"#content > table > tbody > tr > td:nth-child(2) > div.js-scrollfix-bottom-rel > table > tbody > tr:nth-child(1) > td > div.pb16 > div.di-t.w100.mt12 > div.anime-detail-header-stats.di-tc.va-t > div.stats-block.po-r.clearfix > div.di-ib.ml12.pl20.pt8 > span.numbers.members")
							.text()
					+ "\n**Synopisis:**\n"
					+ doc.select(
							"#content > table > tbody > tr > td:nth-child(2) > div.js-scrollfix-bottom-rel > table > tbody > tr:nth-child(1) > td > span")
							.first().text()
					+ "\n";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return msg;
	}

	public static String search(String search, String type) {
		String msg = "**Error** - API returned nothing :/";
		Document doc = null;
		try {
			String url = type.equals("anime") ? url_anime : url_manga;
			String element1 = type.equals("anime") ? "a.hoverinfo_trigger.fw-b.fl-l" : "a.hoverinfo_trigger.fw-b";

			doc = Jsoup.connect(url + search).get();
			Element result = doc.select(element1).first();

			msg = "**" + result.text() + "**";
			msg = msg + "\n" + result.attr("abs:href") + "\n";

			msg = msg + internalSearch(result.attr("abs:href"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return msg;
	}
}
