package commands.fun;

import commands.Command;
import commands.CommandCategory;
import commands.CommandScope;
import core.DiscordMessage;
import java.awt.Color;
import java.util.HashMap;
import java.util.Random;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Flip implements Command {
	private static final HashMap<String, String> subcommands = new HashMap<String, String>();

	static {
		subcommands.put("heads", "Sets your coin value to heads.");
		subcommands.put("tails", "Sets your coin value to tails.");
	}

	public boolean called(String raw, String[] args, MessageReceivedEvent event) {
		if (args.length < 1) {
			event.getChannel().sendMessage(DiscordMessage.embedProblem(event.getAuthor(), "Invalid #arguments!"))
					.queue();
			return false;
		}
		if (!subcommands.containsKey(args[0].toLowerCase())) {
			event.getChannel()
					.sendMessage(event.getMessage().getAuthor().getAsMention() + " it's either heads or tails! D:<")
					.queue();
			return false;
		}
		return true;
	}

	public void action(String raw, String[] args, MessageReceivedEvent event) {
		Random randomNum = new Random();
		String result = (String) subcommands.keySet().toArray()[randomNum.nextInt(2)];
		String input = args[0];
		if (result.equalsIgnoreCase(input)) {
			result = result + "! - **Congratulations**";
		} else {
			result = result + "! - **You suck m8!**";
		}
		event.getChannel()
				.sendMessage(DiscordMessage.embedMessage(event.getAuthor(), "Flip!",
						event.getMessage().getAuthor().getAsMention() + " " + result, Color.DARK_GRAY,
						"http://diysolarpanelsv.com/images/clipart-coins-transparent-background-22.png"))
				.queue();
	}

	public String getDescription() {
		return "Flips a coin, and checks if the input validates";
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

	public void executed(boolean succ, MessageReceivedEvent event) {
	}
}
