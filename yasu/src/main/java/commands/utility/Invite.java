package commands.utility;

import commands.Command;
import commands.CommandCategory;
import commands.CommandScope;
import core.DiscordMessage;
import java.util.HashMap;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Invite implements Command {
	private static final HashMap<String, String> subcommands = new HashMap<String, String>();

	public boolean called(String raw, String[] args, MessageReceivedEvent event) {
		return true;
	}

	public void action(String raw, String[] args, MessageReceivedEvent event) {
		String url = "https://discordapp.com/oauth2/authorize?&client_id=" + event.getJDA().getSelfUser().getId()
				+ "&scope=bot&permissions=0";

		event.getChannel().sendMessage(DiscordMessage.embedSimple(event.getAuthor(), "Invite This Bot to a Server!",
				"You may invite me through this url:\n__" + url
						+ "__\n Make sure you are logged in to a discord account with permissions **on the browser**."))

				.queue();
	}

	public void executed(boolean succ, MessageReceivedEvent event) {
	}

	public String getDescription() {
		return "retuns the invite url for the bot!";
	}

	public HashMap<String, String> getSubCommands() {
		return subcommands;
	}

	public CommandCategory getCategory() {
		return CommandCategory.UTILITY;
	}

	public CommandScope getScope() {
		return CommandScope.BOTH;
	}

	public boolean isListed() {
		return true;
	}
}
