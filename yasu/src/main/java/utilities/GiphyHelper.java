package utilities;

import at.mukprojects.giphy4j.Giphy;
import at.mukprojects.giphy4j.entity.giphy.GiphyData;
import at.mukprojects.giphy4j.exception.GiphyException;
import core.Yasu;
import java.util.List;

public final class GiphyHelper {
	private final Giphy giphy;
	private final String search;

	public GiphyHelper(String search) {
		this.giphy = new Giphy(Yasu.bot.getAPIKey("giphy"));
		this.search = search;
	}

	public String[] getImages(int number) {
		String[] urls = null;
		try {
			int i = 0;
			List<GiphyData> data = this.giphy.search(this.search, number, 0).getDataList();
			urls = new String[data.size()];
			System.out.println(data.size() + "its size");
			for (GiphyData v : data) {
				urls[i] = v.getImages().getOriginal().getUrl();
				i++;
			}
		} catch (GiphyException e) {
			e.printStackTrace();
		}
		return urls;
	}

	public String random() {
		try {
			return this.giphy.searchRandom(this.search).getData().getImageOriginalUrl();
		} catch (GiphyException e) {
			e.printStackTrace();
		}
		return null;
	}
}
