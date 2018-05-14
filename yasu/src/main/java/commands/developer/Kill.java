package commands.developer;

import commands.Command;
import commands.CommandCategory;
import commands.CommandScope;
import core.DiscordMessage;
import core.Yasu;
import java.util.HashMap;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Kill implements Command {
	private static final HashMap<String, String> subcommands = new HashMap<String, String>();

	public boolean called(String raw, String[] args, MessageReceivedEvent event) {
		if (event.getAuthor().getId().equals("166176374036365312")) {
			return true;
		}
		event.getChannel()
				.sendMessage(DiscordMessage.embedProblem(event.getAuthor(), "Not Allowed to do that m8! Sorry :/"))
				.queue();
		return false;
	}

	public void action(String raw, String[] args, MessageReceivedEvent event) {
		JDA nis = Yasu.bot.getInstance();
		for (Guild g : nis.getGuilds()) {
			g.getDefaultChannel().sendMessage(DiscordMessage.embedNotification(nis.getSelfUser(),
					"Shutting down bot! [Shutdown command was recieved]")).complete();
		}
		System.exit(0);
	}

	public void executed(boolean succ, MessageReceivedEvent event) {
	}

	public String getDescription() {
		return "Kills the bot!";
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
}
