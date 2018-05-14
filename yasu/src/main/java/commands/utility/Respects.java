package commands.utility;

import commands.Command;
import commands.CommandCategory;
import commands.CommandScope;
import java.util.HashMap;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Respects implements Command {
	private static final HashMap<String, String> subcommands = new HashMap<String, String>();
	private static int total = 0;

	public boolean called(String raw, String[] args, MessageReceivedEvent event) {
		return true;
	}

	public void action(String raw, String[] args, MessageReceivedEvent event) {
		event.getChannel()
				.sendMessage(event.getAuthor().getAsMention() + " payed respects! \nRespects: **" + ++total + "**")
				.complete();
	}

	public void executed(boolean succ, MessageReceivedEvent event) {
	}

	public String getDescription() {
		return "Pays respects, to whatever!";
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
