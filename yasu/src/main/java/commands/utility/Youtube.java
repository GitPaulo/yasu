package commands.utility;

import commands.Command;
import commands.CommandCategory;
import commands.CommandScope;
import core.DiscordMessage;
import core.Yasu;
import java.awt.Color;
import java.util.HashMap;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.json.JSONArray;
import org.json.JSONObject;
import utilities.JsonUrlParse;

public class Youtube implements Command {
	private int limit = 3;
	public static final int DEFAULT_LIMIT = 3;
	public static final int MIN_LIMIT = 1;
	public static final int MAX_LIMIT = 10;
	private static final HashMap<String, String> subcommands = new HashMap<String, String>();

	static {
		subcommands.put("limit", "Sets limit for the number of search queries displayed!");
		subcommands.put("<keywords>", "A set of keywords to be parsed for search.");
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
		if (args[0].equalsIgnoreCase("limit")) {
			try {
				this.limit = Integer.parseInt(args[1]);
				if ((this.limit > 10) || (this.limit < 1)) {
					event.getChannel().sendMessage(DiscordMessage.embedProblem(event.getAuthor(),
							"Limit overflow! Max limit: 10 Min Limit: 1")).queue();
				} else {
					((Message) event.getChannel()
							.sendMessage(DiscordMessage.embedMessage(event.getAuthor(), "Youtube - Settings",
									"**Limit configuration:** Limit set!( Limit: " + this.limit + " )", Color.RED,
									"http://www.freeiconspng.com/uploads/youtube-subscribe-png-28.png"))
							.complete()).getId();
				}
			} catch (NumberFormatException e) {
				event.getChannel()
						.sendMessage(DiscordMessage.embedProblem(event.getAuthor(), "Could not read number (>.<)"))
						.queue();
			}
			return;
		}
		try {
			String query = String.join(" ", args);

			String msg_id = ((Message) event.getChannel()
					.sendMessage(DiscordMessage.embedMessage(event.getAuthor(), "Youtube - Processing",
							"**Searching through youtube...** ( Limit: " + this.limit + " )", Color.RED,
							"http://www.freeiconspng.com/uploads/youtube-subscribe-png-28.png"))
					.complete()).getId();

			query = query.replace(" ", "+");

			String url = "https://www.googleapis.com/youtube/v3/search?part=snippet&maxResults=" + this.limit
					+ "&type=video&q=" + query + "&key=" + Yasu.bot.getAPIKey("youtube_data");

			JSONObject jsonObject = new JSONObject(JsonUrlParse.readUrl(url));
			if (jsonObject.has("error")) {
				event.getChannel().sendMessage(
						DiscordMessage.embedProblem(event.getAuthor(), "**API ERROR:** " + jsonObject.get("error")))
						.queue();
				return;
			}
			JSONArray jsonArray = jsonObject.getJSONArray("items");
			event.getChannel()
					.editMessageById(msg_id,
							DiscordMessage.embedMessage(event.getAuthor(), "Youtube - Results", "Searched " + query,
									Color.RED, "http://www.freeiconspng.com/uploads/youtube-subscribe-png-28.png"))

					.queue();
			for (int i = 0; i < jsonArray.length(); i++) {
				if (i > this.limit) {
					break;
				}
				event.getChannel().sendMessage("https://www.youtube.com/watch?v="
						+ jsonArray.getJSONObject(i).getJSONObject("id").getString("videoId")).queue();
			}
		} catch (Exception e) {
			event.getChannel()
					.sendMessage(DiscordMessage.embedProblem(event.getAuthor(), "Unable to get YouTube results."))
					.queue();
			e.printStackTrace();
		}
	}

	public String getDescription() {
		return "Searches youtube for videos.";
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
