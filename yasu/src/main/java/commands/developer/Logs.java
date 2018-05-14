package commands.developer;

import commands.Command;
import commands.CommandCategory;
import commands.CommandScope;
import core.DiscordMessage;
import core.Logger;
import java.awt.Color;
import java.util.HashMap;
import java.util.StringJoiner;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Logs implements Command {
	private static final HashMap<String, String> subcommands = new HashMap<String, String>();

	static {
		subcommands.put("<amount>", "A integer from 0 to 100 that tells how many lines to display.");
	}

	public boolean called(String raw, String[] args, MessageReceivedEvent event) {
		if (!event.getAuthor().getId().equals("166176374036365312")) {
			event.getChannel()
					.sendMessage(DiscordMessage.embedProblem(event.getAuthor(), "Not Allowed to do that m8! Sorry :/"))
					.queue();
			return false;
		}
		if (args.length < 1) {
			event.getChannel().sendMessage(DiscordMessage.embedProblem(event.getAuthor(), "Invalid arguments!"))
					.queue();
			return false;
		}
		return true;
	}

	public void action(String raw, String[] args, MessageReceivedEvent event) {
		int amount = 0;
		try {
			amount = Integer.parseInt(args[0]);
		} catch (NumberFormatException ne) {
			event.getChannel().sendMessage(
					DiscordMessage.embedProblem(event.getAuthor(), "Invalid *first* argument! Must be a number."))
					.queue();
			return;
		}
		String[] logs = Logger.getLogs();
		StringJoiner joiner = new StringJoiner("\n");
		int n = Logger.getHead();
		if (amount > n) {
			event.getChannel().sendMessage(DiscordMessage.embedProblem(event.getAuthor(),
					"Not that many temporary logs! Current head pointer: " + n)).queue();
			return;
		}
		for (int i = n; i >= n - amount; i--) {
			joiner.add("`[" + i + "] " + logs[i] + "`");
		}
		event.getChannel()
				.sendMessage(DiscordMessage.embedMessage(event.getAuthor(), "Logs (Latest " + amount + ")",
						joiner.toString(), Color.black,
						"http://www.free-icons-download.net/images/log-file-format-icon-73676.png"))
				.queue();
	}

	public String getDescription() {
		return "Displays current in memory bot log history!";
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

	public void executed(boolean succ, MessageReceivedEvent event) {
	}
}
