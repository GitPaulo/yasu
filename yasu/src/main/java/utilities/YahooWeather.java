package utilities;

import org.json.JSONArray;
import org.json.JSONObject;

public final class YahooWeather {
	private static String url_key = "http://api.wunderground.com/api/511f69780721c5fc/conditions/q/";

	public static String forecast(String location) throws Exception {
		StringBuilder ret = new StringBuilder("");
		location = location.replace(" ", "_").trim();

		String json = JsonUrlParse.readUrl(url_key + location + ".json");
		JSONObject obj = new JSONObject(json);
		if (obj.getJSONObject("response").has("error")) {
			return

			"ERROR:" + obj.getJSONObject("response").getJSONObject("error").getString("type") + " \nDescription: \n"
					+ obj.getJSONObject("response").getJSONObject("error").getString("description");
		}
		if (obj.getJSONObject("response").has("results")) {
			JSONArray arr = obj.getJSONObject("response").getJSONArray("results");
			ret.append("Multiple results for: '" + location + "' please refine search. Results are:\n");
			for (int i = 0; i < arr.length(); i++) {
				ret.append("Results for: \n" + arr.getJSONObject(i).getString("name") + ", "
						+ arr.getJSONObject(i).getString("city") + ",  Country: "
						+ arr.getJSONObject(i).getString("country") + "\n");
			}
		} else {
			ret.append("**Result for:** "
					+ obj.getJSONObject("current_observation").getJSONObject("display_location").getString("full")
					+ "\n**Weather:** " + obj.getJSONObject("current_observation").getString("weather")
					+ "\n**Temperature:** " + obj.getJSONObject("current_observation").getString("temperature_string")
					+ "\n**Humidity:** " + obj.getJSONObject("current_observation").getString("relative_humidity")
					+ "\n**Wind:** " + obj.getJSONObject("current_observation").getString("wind_string")
					+ "\n**Update Date:** " + obj.getJSONObject("current_observation").getString("observation_time")
					+ "\n");
		}
		if (ret.length() > 2000) {
			ret.append(ret.substring(0, 1990) + " (...)");
		}
		return ret.toString();
	}
}
