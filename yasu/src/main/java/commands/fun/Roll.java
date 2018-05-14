package commands.fun;

import commands.Command;
import commands.CommandCategory;
import commands.CommandScope;
import core.DiscordMessage;
import java.awt.Color;
import java.util.HashMap;
import java.util.Random;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Roll implements Command {
	private static final HashMap<String, String> subcommands = new HashMap<String, String>();

	public boolean called(String raw, String[] args, MessageReceivedEvent event) {
		return true;
	}

	public void action(String raw, String[] args, MessageReceivedEvent event) {
		Random ran = new Random();
		int result = ran.nextInt(100);
		event.getChannel()
				.sendMessage(DiscordMessage.embedMessage(event.getAuthor(), "Roll!",
						"Rolled **" + result + "** out of 100!", Color.BLACK,
						"http://mccdc.com/wp-content/uploads/cutie_mark___dice_by_durpy-d4v3yif.png"))
				.queue();
	}

	public void executed(boolean succ, MessageReceivedEvent event) {
	}

	public String getDescription() {
		return "Rolls a dice. 100 sided";
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
