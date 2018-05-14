package commands.developer;

import commands.Command;
import commands.CommandCategory;
import commands.CommandScope;
import core.Client;
import core.DiscordMessage;
import core.Yasu;
import java.util.HashMap;
import java.util.StringJoiner;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class BlackList implements Command {
	private static final HashMap<String, String> subcommands = new HashMap<String, String>();

	static {
		subcommands.put("add", "Adds a user to the bot blacklist!");
		subcommands.put("remove", "Removes a user from the bot blacklist!");
		subcommands.put("list all", "Displays all the user in the blacklist!");
	}

	public boolean called(String raw, String[] args, MessageReceivedEvent event) {
		if (!event.getAuthor().getId().equals("166176374036365312")) {
			event.getChannel()
					.sendMessage(DiscordMessage.embedProblem(event.getAuthor(), "Not Allowed to do that m8! Sorry :/"))
					.queue();
			return false;
		}
		if (args.length < 2) {
			event.getChannel().sendMessage(DiscordMessage.embedProblem(event.getAuthor(), "Invalid arguments!"))
					.queue();
			return false;
		}
		return true;
	}

	public void action(String raw, String[] args, MessageReceivedEvent event) {
		if ("list".equalsIgnoreCase(args[0])) {
			StringJoiner joiner = new StringJoiner("\n");
			int count = 0;
			for (String v : Yasu.bot.getBlackList()) {
				joiner.add("**" + count + "** - Name: " + v);
				count++;
			}
			event.getChannel()
					.sendMessage(DiscordMessage.embedBlackListMessage(event.getAuthor(), "List", joiner.toString()))
					.queue();
			return;
		}
		User target = Client.getUserbyString(args[1]);
		if (target == null) {
			event.getChannel().sendMessage(
					DiscordMessage.embedProblem(event.getAuthor(), "Could not find user!\n String used: " + args[0]))
					.queue();
			return;
		}
		if (target.getId().equals("166176374036365312")) {
			event.getChannel()
					.sendMessage(DiscordMessage.embedProblem(event.getAuthor(),
							"Cannot fucking target the developer(" + target.getName() + ") of the bot. You lit fam."))
					.queue();
			return;
		}
		if (target.getId().equals(Yasu.bot.getInstance().getSelfUser().getId())) {
			event.getChannel()
					.sendMessage(DiscordMessage.embedProblem(event.getAuthor(), "Trying to blacklist Nissan?! BAKA!"))
					.queue();
			return;
		}
		if ("add".equalsIgnoreCase(args[0])) {
			if (Yasu.bot.isBlackListed(target)) {
				event.getChannel().sendMessage(DiscordMessage.embedProblem(event.getAuthor(),
						"This user (" + target.getName() + ") is already in the blacklist!")).queue();
				return;
			}
			Yasu.bot.addBlackList(target);
			event.getChannel().sendMessage(DiscordMessage.embedBlackListMessage(event.getAuthor(), "Add",
					"Added **" + target.getName() + "** to the blacklist!")).queue();
		} else if ("remove".equalsIgnoreCase(args[0])) {
			if (!Yasu.bot.isBlackListed(target)) {
				event.getChannel().sendMessage(DiscordMessage.embedProblem(event.getAuthor(),
						"This user (" + target.getName() + ") is not in the blacklist!")).queue();
				return;
			}
			Yasu.bot.removeBlackList(target);
			event.getChannel().sendMessage(DiscordMessage.embedBlackListMessage(event.getAuthor(), "Remove",
					"Removed **" + target.getName() + "** from the blacklist!")).queue();
		} else {
			event.getChannel()
					.sendMessage(DiscordMessage.embedProblem(event.getAuthor(), "Invalid **first** argument!")).queue();
		}
	}

	public String getDescription() {
		return "Blacklist system of the bot.";
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
