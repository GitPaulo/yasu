package commands.developer;

import commands.Command;
import commands.CommandCategory;
import commands.CommandScope;
import core.DiscordMessage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import net.dv8tion.jda.core.entities.Icon;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.managers.AccountManager;

public class BotManage implements Command {
	private static final HashMap<String, String> subcommands = new HashMap<String, String>();

	static {
		subcommands.put("setname", "Set the bot user name to a value");
		subcommands.put("seticon", "Set the bot user icon to a file name");
	}

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
		AccountManager am = new AccountManager(event.getJDA().getSelfUser());
		String type = args[1];
		args[1] = "";
		String rest = String.join(" ", args);
		if ("setname".equalsIgnoreCase(type)) {
			String before = event.getJDA().getSelfUser().getName();
			am.setName(rest).complete();
			event.getChannel().sendMessage(DiscordMessage.embedSimple(event.getAuthor(), "Bot Managing - Set Name",
					"Bot name set! Changed from " + before + " to " + rest)).queue();
		} else if ("seticon".equalsIgnoreCase(type)) {
			File iconFile = new File(rest);
			if (!iconFile.exists()) {
				event.getChannel().sendMessage(DiscordMessage.embedProblem(event.getAuthor(),
						"The file specified for the Icon was not found!")).queue();
				return;
			}
			Icon icon = null;
			try {
				icon = Icon.from(iconFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
			am.setAvatar(icon).queue();
		}
	}

	public String getDescription() {
		return "Developer command to manage bot details";
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
