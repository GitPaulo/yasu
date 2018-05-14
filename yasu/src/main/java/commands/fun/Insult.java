package commands.fun;

import commands.Command;
import commands.CommandCategory;
import commands.CommandScope;
import core.Client;
import core.DiscordMessage;
import java.util.HashMap;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import utilities.UtilityMethods;

public class Insult implements Command {
	private static final HashMap<String, String> subcommands = new HashMap<String, String>();

	static {
		subcommands.put("<user>", "User to be the target of the insult!");
	}

	public boolean called(String raw, String[] args, MessageReceivedEvent event) {
		if (args.length < 1) {
			event.getChannel().sendMessage(DiscordMessage.embedProblem(event.getAuthor(), "Invalid #arguments!"))
					.queue();
			return false;
		}
		return true;
	}

	public void action(String raw, String[] args, MessageReceivedEvent event) {
		User target = Client.getUserbyString(String.join(" ", args));
		if (target == null) {
			event.getChannel().sendMessage(
					DiscordMessage.embedProblem(event.getAuthor(), "Could not find user!\n String used: " + args[0]))
					.queue();
			return;
		}
		String result = UtilityMethods
				.requestReadAllURL("https://insult.mattbas.org/api/insult.txt?who=**" + target.getName() + "**");
		event.getChannel().sendMessage(result).queue();
	}

	public String getDescription() {
		return "Insults the targeted player!";
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
