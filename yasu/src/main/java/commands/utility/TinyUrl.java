package commands.utility;

import commands.Command;
import commands.CommandCategory;
import commands.CommandScope;
import core.DiscordMessage;
import java.util.HashMap;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import utilities.ShortenURL;

public class TinyUrl implements Command {
	private static final HashMap<String, String> subcommands = new HashMap<String, String>();

	public boolean called(String raw, String[] args, MessageReceivedEvent event) {
		if (args.length != 1) {
			event.getChannel().sendMessage(DiscordMessage.embedProblem(event.getAuthor(), "Invalid arguments!"))
					.queue();
			return false;
		}
		return true;
	}

	public void action(String raw, String[] args, MessageReceivedEvent event) {
		event.getChannel().sendMessage(ShortenURL.get(args[0])).queue();
	}

	public void executed(boolean succ, MessageReceivedEvent event) {
	}

	public String getDescription() {
		return "Shortens the given URL.";
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
}
