package commands.utility;

import commands.Command;
import commands.CommandCategory;
import commands.CommandScope;
import core.DiscordMessage;
import java.awt.Color;
import java.util.HashMap;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import utilities.SystemInfo;

public class Diagnostics implements Command {
	private static final HashMap<String, String> subcommands = new HashMap<String, String>();

	public boolean called(String raw, String[] args, MessageReceivedEvent event) {
		return true;
	}

	public void action(String raw, String[] args, MessageReceivedEvent event) {
		SystemInfo info = new SystemInfo();
		event.getChannel()
				.sendMessage(DiscordMessage.embedMessage(event.getAuthor(), "Host Machine Diagnostics",
						"```xl\n" + info.osInfo() + "```\n```xl\n" + info.memInfo() + "```", Color.BLACK,
						"http://www.freeiconspng.com/uploads/web-server-icon-11.png"))

				.queue();
	}

	public void executed(boolean succ, MessageReceivedEvent event) {
	}

	public String getDescription() {
		return "Provides a list of diagnostics about the bot and its machine.";
	}

	public HashMap<String, String> getSubCommands() {
		return subcommands;
	}

	public CommandCategory getCategory() {
		return CommandCategory.INFORMATIVE;
	}

	public CommandScope getScope() {
		return CommandScope.BOTH;
	}

	public boolean isListed() {
		return true;
	}
}
