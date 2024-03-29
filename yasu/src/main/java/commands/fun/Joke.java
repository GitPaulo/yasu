package commands.fun;

import commands.Command;
import commands.CommandCategory;
import commands.CommandScope;
import core.DiscordMessage;
import java.awt.Color;
import java.util.HashMap;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.json.JSONObject;
import utilities.UtilityMethods;

public class Joke implements Command {
	private static final HashMap<String, String> subcommands = new HashMap<String, String>();

	public boolean called(String raw, String[] args, MessageReceivedEvent event) {
		return true;
	}

	public void action(String raw, String[] args, MessageReceivedEvent event) {
		String link = "http://api.icndb.com/jokes/random";
		String response = UtilityMethods.requestReadAllURL(link);

		JSONObject json = new JSONObject(response);
		String joke = json.getJSONObject("value").getString("joke");

		event.getChannel().sendMessage(DiscordMessage.embedMessage(event.getAuthor(), "Joke!", joke, Color.MAGENTA,
				"https://d30y9cdsu7xlg0.cloudfront.net/png/30623-200.png")).queue();
	}

	public void executed(boolean succ, MessageReceivedEvent event) {
	}

	public String getDescription() {
		return "Displays a joke.";
	}

	public HashMap<String, String> getSubCommands() {
		return subcommands;
	}

	public CommandCategory getCategory() {
		return CommandCategory.FUN;
	}

	public CommandScope getScope() {
		return CommandScope.BOTH;
	}

	public boolean isListed() {
		return true;
	}
}
