package commands.informative;

import commands.Command;
import commands.CommandCategory;
import commands.CommandScope;
import core.Client;
import core.DiscordMessage;
import java.awt.Color;
import java.util.HashMap;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class UserInfo implements Command {
	private static final HashMap<String, String> subcommands = new HashMap<String, String>();

	static {
		subcommands.put("<user>", "An ID or exact name, not nickname, for target user.");
	}

	public boolean called(String raw, String[] args, MessageReceivedEvent event) {
		if (args.length < 1) {
			event.getChannel().sendMessage(DiscordMessage.embedProblem(event.getAuthor(), "Invalid arguments!"))
					.queue();
			return false;
		}
		return true;
	}

	public void action(String raw, String[] args, MessageReceivedEvent event) {
		User target = Client.getUserbyString(args[0]);
		if (target == null) {
			event.getChannel().sendMessage(
					DiscordMessage.embedProblem(event.getAuthor(), "Could not find user!\n String used: " + args[0]))
					.queue();
			return;
		}
		event.getChannel()
				.sendMessage(DiscordMessage.embedMessage(event.getAuthor(), "Information about: " + target.getName(),
						"**Name:** " + target.getName() + "\n**IsBot:** " + target.isBot() + "\n**ID:** "
								+ target.getId() + "\n**Discriminator:** " + target.getDiscriminator()
								+ "\n**Mutual Guilds with target:** " + target.getMutualGuilds().size() + "\n",
						Color.gray, target.getAvatarUrl()))
				.queue();
	}

	public String getDescription() {
		return "Display information about a user!";
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

	public void executed(boolean succ, MessageReceivedEvent event) {
	}
}
