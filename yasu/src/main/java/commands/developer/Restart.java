package commands.developer;

import commands.Command;
import commands.CommandCategory;
import commands.CommandScope;
import core.DiscordMessage;
import core.Yasu;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Restart implements Command {
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
		event.getChannel()
				.sendMessage(DiscordMessage.embedNotification(event.getAuthor(), "Yasu is preparing to restart..."))
				.queue();
		try {
			if (Yasu.restartApplication()) {
				event.getChannel().sendMessage(DiscordMessage.embedNotification(event.getAuthor(), "Restarting..."))
						.queue();
				System.exit(0);
			} else {
				event.getChannel().sendMessage(DiscordMessage.embedNotification(event.getAuthor(),
						"Failed to restart! Most likely could not find the .jar file!")).queue();
			}
		} catch (URISyntaxException | IOException e) {
			e.printStackTrace();
			event.getChannel().sendMessage(DiscordMessage.embedProblem(event.getAuthor(),
					"Problem while restarting - Check Console:\nTrace:" + e.getLocalizedMessage())).queue();
		}
	}

	public void executed(boolean succ, MessageReceivedEvent event) {
	}

	public String getDescription() {
		return "Restarts the bot application.";
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
