package commands.utility;

import commands.Command;
import commands.CommandCategory;
import commands.CommandScope;
import core.DiscordMessage;
import core.Logger;
import core.Yasu;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Vote implements Command {
	private static final HashMap<String, String> subcommands;
	private static final HashMap<String, HashMap<String, VoteData>> VOTES = new HashMap<String, HashMap<String, VoteData>>();

	static {
		subcommands = new HashMap<String, String>();
		subcommands.put("start", "Starts a vote. Must be followed by ID of vote and options. Seperated by space.");
		subcommands.put("end", "Ends a vote. Must be followed by ID of vote.");
		subcommands.put("list all", "Lists all the current active votes.");
		subcommands.put("<id> <option>", "Votes for a selection.");
	}

	public boolean called(String raw, String[] args, MessageReceivedEvent event) {
		if (args.length < 2) {
			event.getChannel().sendMessage(DiscordMessage.embedProblem(event.getAuthor(), "Invalid #arguments!"))
					.queue();
			return false;
		}
		return true;
	}

	public void action(String raw, String[] args, MessageReceivedEvent event) {
		if (VOTES.isEmpty()) {
			throw new Error("Guilds have not been initialised for voting system!");
		}
		HashMap<String, VoteData> data = VOTES.get(event.getGuild().getId());
		if (args[0].equalsIgnoreCase("start")) {
			if (args.length < 4) {
				event.getChannel().sendMessage(DiscordMessage.embedProblem(event.getAuthor(),
						"Invalid #arguments! Correct cmd: !vote start <id> <[options, .. ]>")).queue();
				return;
			}
			if (data.containsKey(args[1])) {
				event.getChannel().sendMessage(
						DiscordMessage.embedProblem(event.getAuthor(), "There **already** exists a vote with this ID!"))
						.queue();
				return;
			}
			StringBuilder message = new StringBuilder();

			HashMap<String, Integer> options = new HashMap<String, Integer>();
			for (int i = 2; i < args.length; i++) {
				options.put(args[i], Integer.valueOf(0));
				message.append("**" + args[i] + " ** = 0\n");
			}
			Message vote_start_msg = (Message) event.getChannel()
					.sendMessage(DiscordMessage.embedMessage(event.getAuthor(),
							"Guild Wide Vote - New Vote: \"" + args[1] + "\"", message.toString(), Color.lightGray,
							"http://www.ncsbe.gov/portals/0/Images/icons/vote_icon.png"))
					.complete();

			data.put(args[1], new VoteData(vote_start_msg, options, new ArrayList<User>()));
		} else {
			Iterator<Map.Entry<String, Integer>> it;
			if (args[0].equalsIgnoreCase("end")) {
				if (!data.containsKey(args[1])) {
					event.getChannel().sendMessage(DiscordMessage.embedProblem(event.getAuthor(),
							"There does **not** exist a vote with that ID!")).queue();
					return;
				}
				HashMap<String, Integer> options = data.get(args[1]).options;
				it = options.entrySet().iterator();
				String highest_key = null;
				int highest_value = 0;
				while (it.hasNext()) {
					Map.Entry<String, Integer> pair = it.next();
					String key = pair.getKey();
					int value = pair.getValue().intValue();
					if (value > highest_value) {
						highest_value = value;
						highest_key = key;
					}
				}
				if (highest_key == null) {
					event.getChannel().sendMessage(DiscordMessage.embedProblem(event.getAuthor(),
							"Could not find the highest key! Contact developer.")).queue();
					return;
				}
				data.remove(args[1]);

				event.getChannel()
						.sendMessage(DiscordMessage.embedMessage(event.getAuthor(),
								"Guild Wide Vote - Results - Vote: \"" + args[1] + "\"",
								"**" + highest_key + "** category has won by " + highest_value + " votes!",
								Color.lightGray, "http://www.ncsbe.gov/portals/0/Images/icons/vote_icon.png"))

						.queue();
			} else if (args[0].equalsIgnoreCase("list all")) {
				String message = "#(Active votes in this guild): " + VOTES.size() + "\n";
				for (String v : data.keySet()) {
					message = message + "**ID:** " + v + "\n";
				}
				event.getChannel().sendMessage(DiscordMessage.embedMessage(event.getAuthor(), "Guild Wide Vote - List",
						message, Color.lightGray, "http://www.ncsbe.gov/portals/0/Images/icons/vote_icon.png")).queue();
			} else {
				if (!data.containsKey(args[0])) {
					event.getChannel().sendMessage(DiscordMessage.embedProblem(event.getAuthor(),
							"There does **not** exist a vote with that ID!")).queue();
					return;
				}
				HashMap<String, Integer> options = data.get(args[0]).options;
				if (!options.containsKey(args[1])) {
					event.getChannel().sendMessage(DiscordMessage.embedProblem(event.getAuthor(),
							"There does **not** exist a vote option with that ID!")).queue();
					return;
				}
				if (data.get(args[0]).hasVoted.contains(event.getAuthor())) {
					event.getChannel().sendMessage(
							DiscordMessage.embedProblem(event.getAuthor(), "You have **already** voted for this vote!"))
							.queue();
					return;
				}
				int current_value = options.get(args[1]).intValue();
				options.put(args[1], Integer.valueOf(++current_value));
				data.get(args[0]).hasVoted.add(event.getAuthor());

				event.getChannel()
						.sendMessage(DiscordMessage.embedMessage(event.getAuthor(),
								"Guild Wide Vote - New entry - " + args[1],
								"**" + event.getAuthor().getName() + "** has voted for " + args[1], Color.lightGray,
								"http://www.ncsbe.gov/portals/0/Images/icons/vote_icon.png"))

						.queue();

				String message = "";
				Iterator<Map.Entry<String, Integer>> it1 = options.entrySet().iterator();
				while (it1.hasNext()) {
					Map.Entry<String, Integer> pair = it1.next();
					message = message + "**" + pair.getKey() + " ** = " + pair.getValue() + "\n";
				}
				data.get(args[0]).message.editMessage(
						DiscordMessage.embedMessage(event.getAuthor(), "Guild Wide Vote - Vote: \"" + args[1] + "\"",
								message, Color.lightGray, "http://www.ncsbe.gov/portals/0/Images/icons/vote_icon.png"))
						.queue();
			}
		}
	}

	public String getDescription() {
		return "Guild wide voting system!";
	}

	public HashMap<String, String> getSubCommands() {
		return subcommands;
	}

	public CommandCategory getCategory() {
		return CommandCategory.UTILITY;
	}

	public CommandScope getScope() {
		return CommandScope.PUBLIC;
	}

	public boolean isListed() {
		return true;
	}

	public static class VoteData {
		private final Message message;
		private final HashMap<String, Integer> options;
		private ArrayList<User> hasVoted;

		public VoteData(Message message, HashMap<String, Integer> options, ArrayList<User> hasVoted) {
			this.message = message;
			this.options = options;
			this.hasVoted = hasVoted;
		}
	}

	public static void initVotingStructure() {
		if (!VOTES.isEmpty()) {
			throw new Error("The voting structure has already been initialised!");
		}
		Logger.info(new Object[] { "Initialising the voting system data structure!" });
		for (Guild v : Yasu.bot.getInstance().getGuilds()) {
			VOTES.put(v.getId(), new HashMap<String, VoteData>());
		}
	}

	public void executed(boolean succ, MessageReceivedEvent event) {
	}
}
