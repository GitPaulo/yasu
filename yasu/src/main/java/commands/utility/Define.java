package commands.utility;

import commands.Command;
import commands.CommandCategory;
import commands.CommandScope;
import core.DiscordMessage;
import java.awt.Color;
import java.io.IOException;
import java.util.HashMap;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import utilities.UrbanDictionary;

public class Define implements Command {
	private static final HashMap<String, String> subcommands = new HashMap<String, String>();

	static {
		subcommands.put("<keywords>", "Format: !define {keywords}");
	}

	public boolean called(String raw, String[] args, MessageReceivedEvent event) {
		if (args.length == 0) {
			event.getChannel().sendMessage(DiscordMessage.embedProblem(event.getAuthor(), "Invalid arguments!"))
					.queue();
			return false;
		}
		return true;
	}

	public void action(String raw, String[] args, MessageReceivedEvent event) {
		String msg_id = ((Message) event.getChannel()
				.sendMessage(DiscordMessage.embedMessage(event.getAuthor(), "Definitions - Processing",
						event.getMessage().getAuthor().getAsMention() + " Give me a second, please... ", Color.BLUE,
						"http://www.userlogos.org/files/logos/Ixodides/ud.png"))
				.complete()).getId();
		try {
			event.getChannel()
					.editMessageById(msg_id,
							DiscordMessage.embedMessage(event.getAuthor(),
									"Definitions - Definition of \"" + String.join(" ", args) + "\"",
									UrbanDictionary.define(String.join(" ", args)), Color.BLUE,
									"http://www.userlogos.org/files/logos/Ixodides/ud.png"))
					.queue();
			event.getChannel()
					.sendMessage(DiscordMessage.embedMessage(event.getAuthor(),
							"Definitions - Examples of \"" + String.join(" ", args) + "\"",
							UrbanDictionary.examples(String.join(" ", args)), Color.BLUE,
							"http://www.userlogos.org/files/logos/Ixodides/ud.png"))

					.queue();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getDescription() {
		return "Defines keywords and provides example in chat.";
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

	public void executed(boolean succ, MessageReceivedEvent event) {
	}
}
