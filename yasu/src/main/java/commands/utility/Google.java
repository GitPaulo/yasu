package commands.utility;

import commands.Command;
import commands.CommandCategory;
import commands.CommandScope;
import core.DiscordMessage;
import java.awt.Color;
import java.util.HashMap;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Google implements Command {
	private static final HashMap<String, String> subcommands = new HashMap<String, String>();

	static {
		subcommands.put("img", "First argument. Sets the search for a image search.");
		subcommands.put("web", "First argument. Sets the search for a web search.");
	}

	public boolean called(String raw, String[] args, MessageReceivedEvent event) {
		if (args.length < 2) {
			event.getChannel().sendMessage(DiscordMessage.embedProblem(event.getAuthor(), "Invalid arguments!"))
					.queue();
			return false;
		}
		return true;
	}

	public void action(String raw, String[] args, MessageReceivedEvent event) {
		String str = "";
		String type = args[0];
		args[0] = "";
		String query = String.join(" ", args);
		switch (type) {
		case "img":
			str = "https://www.google.pt/search?site=&tbm=isch&q=" + query.trim().replace(" ", "+");
			break;
		case "web":
			str = "https://www.google.pt/search?q=" + query.trim().replace(" ", "+");
			break;
		default:
			event.getChannel()
					.sendMessage(DiscordMessage.embedProblem(event.getAuthor(), "Invalid **first** argument!")).queue();
			return;
		}
		event.getChannel().sendMessage(DiscordMessage.embedMessage(event.getAuthor(), "Google Search: " + query, str,
				Color.WHITE, "http://www.seomofo.com/downloads/new-google-logo-knockoff.png")).queue();
	}

	public String getDescription() {
		return "Appends arguments to a google search!";
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
