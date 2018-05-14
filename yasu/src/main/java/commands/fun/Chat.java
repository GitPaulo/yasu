package commands.fun;

import commands.Command;
import commands.CommandCategory;
import commands.CommandScope;
import core.DiscordMessage;
import java.util.HashMap;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import utilities.Cleverbot;

public class Chat implements Command {
	private static final HashMap<String, String> subcommands = new HashMap<String, String>();

	static {
		subcommands.put("<message>", "Talks with the bot!");
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
		event.getChannel().sendMessage(DiscordMessage.embedCleverbot(event.getAuthor(), "Chatting!",
				Cleverbot.getResponse(event.getAuthor(), String.join(" ", args)))).queue();
	}

	public String getDescription() {
		return "Have a conversation with the bot!";
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
