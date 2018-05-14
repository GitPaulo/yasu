package commands.fun;

import commands.Command;
import commands.CommandCategory;
import commands.CommandScope;
import java.util.HashMap;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Yasusada implements Command {
	private static final HashMap<String, String> subcommands = new HashMap<String, String>();

	public boolean called(String raw, String[] args, MessageReceivedEvent event) {
		return false;
	}

	public void action(String raw, String[] args, MessageReceivedEvent event) {
	}

	public void executed(boolean succ, MessageReceivedEvent event) {
	}

	public String getDescription() {
		return "not sure what this will do yet.";
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
