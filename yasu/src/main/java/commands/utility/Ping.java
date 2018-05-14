package commands.utility;

import commands.Command;
import commands.CommandCategory;
import commands.CommandScope;
import core.DiscordMessage;
import java.awt.Color;
import java.util.HashMap;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Ping implements Command {
	private static final HashMap<String, String> subcommands = new HashMap<String, String>();
	private long t0;

	public boolean called(String raw, String[] args, MessageReceivedEvent event) {
		this.t0 = System.currentTimeMillis();
		return true;
	}

	public void action(String raw, String[] args, MessageReceivedEvent event) {
		event.getChannel()
				.sendMessage(DiscordMessage.embedMessage(event.getAuthor(), "PONG!",
						"I took **" + (System.currentTimeMillis() - this.t0) + "ms** to answer that!\n My latency: "
								+ event.getJDA().getPing() + "ms",
						Color.white, "http://www.yorozuyasoul.com/images/omake/omake_ep4-1.jpg"))
				.queue();
	}

	public void executed(boolean succ, MessageReceivedEvent event) {
	}

	public String getDescription() {
		return "Ping, pong!";
	}

	public CommandScope getScope() {
		return CommandScope.BOTH;
	}

	public CommandCategory getCategory() {
		return CommandCategory.UTILITY;
	}

	public boolean isListed() {
		return true;
	}

	public HashMap<String, String> getSubCommands() {
		return subcommands;
	}
}
