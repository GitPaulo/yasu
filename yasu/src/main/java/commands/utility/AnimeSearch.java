package commands.utility;

import commands.Command;
import commands.CommandCategory;
import commands.CommandScope;
import core.DiscordMessage;
import java.awt.Color;
import java.util.HashMap;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import utilities.MyAnimeList;

public class AnimeSearch implements Command {
	private static final HashMap<String, String> subcommands = new HashMap<String, String>();

	static {
		subcommands.put("<keywords>", "Format: !anime <anime/manga> {title}");
		subcommands.put("help", "Displays specific help for commands");
	}

	public boolean called(String raw, String[] args, MessageReceivedEvent event) {
		if (args.length < 1) {
			event.getChannel()
					.sendMessage(DiscordMessage.embedProblem(event.getAuthor(), "**Error:** Invalid #arguments!"))
					.queue();
			return false;
		}
		if ((!args[0].equalsIgnoreCase("help")) && (!args[0].equalsIgnoreCase("manga"))
				&& (!args[0].equalsIgnoreCase("anime"))) {
			event.getChannel().sendMessage(DiscordMessage.embedProblem(event.getAuthor(),
					"Invalid **first** argument, either: 'manga' or 'anime' types! OR HELP command!")).queue();
			return false;
		}
		return true;
	}

	public void action(String raw, String[] args, MessageReceivedEvent event) {
		String results = null;
		String type = args[0];

		args[0] = "";
		String search = String.join(" ", args);
		if (args[0].equalsIgnoreCase("help")) {
			event.getChannel()
					.sendMessage(DiscordMessage.embedNotification(event.getAuthor(),
							"**Help for the command:** <prefix>animesearch <#HELP>/<anime/manga> <search words>"))
					.queue();
		} else {
			event.getChannel().sendTyping().queue();

			String msg_id = ((Message) event.getChannel()
					.sendMessage(DiscordMessage.embedMessage(event.getAuthor(), "Anime Search for: " + search,
							"Please wait while I search online...", Color.PINK,
							"http://www.userlogos.org/files/logos/zahnjin/myanimelist_relf_rev4.png"))
					.complete()).getId();

			results = MyAnimeList.search(search, type);
			event.getChannel()
					.editMessageById(msg_id,
							DiscordMessage.embedMessage(event.getAuthor(), "Anime Search for: " + search, results,
									Color.PINK,
									"http://www.userlogos.org/files/logos/zahnjin/myanimelist_relf_rev4.png"))
					.complete();
		}
	}

	public String getDescription() {
		return "Search MAL for anime or managa!";
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
