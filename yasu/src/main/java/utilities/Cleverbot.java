package utilities;

import com.michaelwflaherty.cleverbotapi.CleverBotQuery;
import core.Yasu;
import java.io.IOException;
import net.dv8tion.jda.core.entities.User;

public final class Cleverbot {
	public static String getResponse(User u, String msg) {
		CleverBotQuery bot = new CleverBotQuery(Yasu.bot.getAPIKey("cleverbot"), msg);
		String response = null;
		try {
			bot.sendRequest();
			response = bot.getResponse();
		} catch (IOException e) {
			response = "API Error! Contact developer!";
		}
		return response;
	}
}
