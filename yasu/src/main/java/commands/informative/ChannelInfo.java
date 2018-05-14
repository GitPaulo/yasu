package commands.informative;

import commands.Command;
import commands.CommandCategory;
import commands.CommandScope;
import core.DiscordMessage;
import java.util.HashMap;
import java.util.List;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class ChannelInfo implements Command {
	private static final HashMap<String, String> subcommands = new HashMap<String, String>();

	public boolean called(String raw, String[] args, MessageReceivedEvent event) {
		return true;
	}

	public void action(String raw, String[] args, MessageReceivedEvent event) {
		MessageChannel c = event.getChannel();
		c.sendMessage(DiscordMessage.embedSimple(event.getAuthor(), "Channel Information",
				"**Name:** " + c.getName() + "\n**Channel ID: ** " + c.getId() + "\n**Type**: " + c.getType()
						+ "\n**Creation Time:** " + c.getCreationTime() + "\n**Latest Message ID:** "
						+ c.getLatestMessageId() + "\n**#Pinned Messages: **"
						+ ((List<?>) c.getPinnedMessages().complete()).size()))
				.queue();
	}

	public void executed(boolean succ, MessageReceivedEvent event) {
	}

	public String getDescription() {
		return "Displays current channel information.";
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
