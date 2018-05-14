package commands.administrative;

import commands.Command;
import commands.CommandCategory;
import commands.CommandScope;
import java.util.HashMap;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Say implements Command {
	private static final HashMap<String, String> subcommands = new HashMap<String, String>();

	public boolean called(String raw, String[] args, MessageReceivedEvent event) {
		return true;
	}

	public void action(String raw, String[] args, MessageReceivedEvent event) {
		event.getChannel().sendMessage(String.join(" ", args)).queue();
	}

	public void executed(boolean succ, MessageReceivedEvent event) {
	}

	public String getDescription() {
		return "Says something as the bot user.";
	}

	public HashMap<String, String> getSubCommands() {
		return subcommands;
	}

	public CommandCategory getCategory() {
		return CommandCategory.ADMINISTRATIVE;
	}

	public CommandScope getScope() {
		return CommandScope.BOTH;
	}

	public boolean isListed() {
		return true;
	}
}
