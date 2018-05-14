package commands.developer;

import commands.Command;
import commands.CommandCategory;
import commands.CommandScope;
import core.DiscordMessage;
import core.Yasu;
import java.util.HashMap;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Save implements Command {
	private static final HashMap<String, String> subcommands = new HashMap<String, String>();

	public boolean called(String raw, String[] args, MessageReceivedEvent event) {
		if (event.getAuthor().getId().equals("166176374036365312")) {
			return true;
		}
		event.getChannel()
				.sendMessage(DiscordMessage.embedProblem(event.getAuthor(), "Not Allowed to do that m8! Sorry :/"))
				.queue();
		return false;
	}

	public void action(String raw, String[] args, MessageReceivedEvent event) {
		Yasu.bot.saveData();
		event.getChannel().sendMessage(DiscordMessage.embedSimple(event.getAuthor(), "Saving Instance...",
				"Force saved instance data to storage!")).queue();
	}

	public void executed(boolean succ, MessageReceivedEvent event) {
	}

	public String getDescription() {
		return "Saves the bot data to storage";
	}

	public HashMap<String, String> getSubCommands() {
		return subcommands;
	}

	public CommandCategory getCategory() {
		return CommandCategory.DEVELOPER;
	}

	public CommandScope getScope() {
		return CommandScope.BOTH;
	}

	public boolean isListed() {
		return true;
	}
}
