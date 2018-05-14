package commands.utility;

import commands.Command;
import commands.CommandCategory;
import commands.CommandScope;
import core.DiscordMessage;
import java.awt.Color;
import java.util.HashMap;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import utilities.YahooWeather;

public class Weather implements Command {
	private static final HashMap<String, String> subcommands = new HashMap<String, String>();

	static {
		subcommands.put("<keywords>", "A set of keywords to be parsed for weather.");
	}

	public boolean called(String raw, String[] args, MessageReceivedEvent event) {
		if (args.length == 0) {
			event.getChannel().sendMessage(DiscordMessage.embedProblem(event.getAuthor(), "Invalid arguments!"))
					.queue();
			return false;
		}
		return true;
	}

	public void action(String raw, String[] args, MessageReceivedEvent event) {
		String msg = "";

		Message message = (Message) event.getChannel().sendMessage(DiscordMessage.embedMessage(event.getAuthor(),
				"Weather forcast for: " + String.join(" ", args),
				"**Give me a second please... checking weather data for:** " + String.join(" ", args), Color.GREEN,
				"http://www.blogjuniordoiphone.com.br/wp-content/uploads/2014/10/app-tempo-yahoo-blog-juniirdoiphone.png"))
				.complete();
		try {
			msg = YahooWeather.forecast(String.join(" ", args));
		} catch (Exception e) {
			event.getChannel().sendMessage(
					DiscordMessage.embedProblem(event.getAuthor(), "Problem with weather API! Try again please!"))
					.queue();
			e.printStackTrace();
			return;
		}
		message.editMessage(DiscordMessage.embedMessage(event.getAuthor(),
				"Weather forcast for: " + String.join(" ", args), msg, Color.GREEN,
				"http://www.blogjuniordoiphone.com.br/wp-content/uploads/2014/10/app-tempo-yahoo-blog-juniirdoiphone.png"))
				.queue();
	}

	public String getDescription() {
		return "Gets weather information about a place to chat.";
	}

	public HashMap<String, String> getSubCommands() {
		return subcommands;
	}

	public CommandCategory getCategory() {
		return CommandCategory.UTILITY;
	}

	public CommandScope getScope() {
		return CommandScope.BOTH;
	}

	public boolean isListed() {
		return true;
	}

	public void executed(boolean succ, MessageReceivedEvent event) {
	}
}
