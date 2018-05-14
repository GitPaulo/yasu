package commands.fun;

import commands.Command;
import commands.CommandCategory;
import commands.CommandScope;
import core.DiscordMessage;
import java.util.HashMap;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import utilities.GiphyHelper;

public class Emote implements Command {
	private int range = 20;
	public static final int DEFAULT_RANGE = 20;
	public static final int MAX_RANGE = 1000;
	public static final int MIN_RANGE = 1;
	private static final HashMap<String, String> subcommands = new HashMap<String, String>();

	static {
		subcommands.put("limit", "Sets the range pool of gifs to search in.");
		subcommands.put("pat", "Shows a pating gif.");
		subcommands.put("cry", "Shows a crying gif.");
		subcommands.put("slap", "Shows a slaping gif.");
		subcommands.put("punch", "Shows a punch gif.");
		subcommands.put("kick", "Shows an kick gif.");
		subcommands.put("laugh", "Shows a laughing gif.");
		subcommands.put("angry", "Shows an angry gif.");
		subcommands.put("sad", "Shows an sad gif.");
		subcommands.put("happy", "Shows an happy gif.");
		subcommands.put("hentai", "Shows an hentai gif.");
		subcommands.put("baka", "Shows an baka gif.");
		subcommands.put("nani", "Shows an nani gif.");
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
		GiphyHelper gh = null;
		switch (args[0]) {
		case "limit":
			try {
				this.range = Integer.parseInt(args[1]);
				if ((this.range > 1000) || (this.range < 1)) {
					this.range = 20;
					event.getChannel().sendMessage(DiscordMessage.embedProblem(event.getAuthor(),
							"Under or over range Limits! Max range: 1000 Min range:1")).queue();
				} else {
					event.getChannel().sendMessage("**Emote Pool range set:** " + this.range).queue();
				}
			} catch (NumberFormatException e) {
				event.getChannel().sendMessage(DiscordMessage.embedProblem(event.getAuthor(),
						"Invalid **second** argument. Must be a number!")).queue();
			}
			return;
		case "pat":
			gh = new GiphyHelper("anime pat");
			break;
		case "cry":
			gh = new GiphyHelper("anime cry");
			break;
		case "slap":
			gh = new GiphyHelper("anime slap");
			break;
		case "punch":
			gh = new GiphyHelper("anime punch");
			break;
		case "laugh":
			gh = new GiphyHelper("anime laugh");
			break;
		case "angry":
			gh = new GiphyHelper("anime angry");
			break;
		case "sad":
			gh = new GiphyHelper("anime sad");
			break;
		case "happy":
			gh = new GiphyHelper("anime happy");
			break;
		case "kick":
			gh = new GiphyHelper("anime kick");
			break;
		case "hentai":
			gh = new GiphyHelper("hentai");
			break;
		case "baka":
			gh = new GiphyHelper("baka");
			break;
		case "nani":
			gh = new GiphyHelper("nani!?");
			break;
		default:
			event.getChannel()
					.sendMessage(DiscordMessage.embedProblem(event.getAuthor(), "Invalid **first** argument!")).queue();
			return;
		}
		event.getChannel().sendMessage(gh.getImages(this.range)[new java.util.Random().nextInt(this.range - 1)])
				.queue();
	}

	public String getDescription() {
		return "Emote, displaying a gif instead!";
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
