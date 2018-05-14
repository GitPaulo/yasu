package commands.fun;

import commands.Command;
import commands.CommandCategory;
import commands.CommandScope;
import core.DiscordMessage;
import java.io.File;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import utilities.StringSimilar;

public class Naruto implements Command {
	private static final HashMap<String, String> subcommands = new HashMap<String, String>();

	static {
		subcommands.put("katon", "A naruto jutsu category.");
		subcommands.put("suiton", "A naruto jutsu category.");
		subcommands.put("raiton", "A naruto jutsu category.");
		subcommands.put("futon", "A naruto jutsu category.");
		subcommands.put("doton", "A naruto jutsu category.");
		subcommands.put("rasengan", "A naruto jutsu category.");
		subcommands.put("chidori", "A naruto jutsu category.");
		subcommands.put("rinnegan", "A naruto jutsu category.");
		subcommands.put("byakugan", "A naruto jutsu category.");
		subcommands.put("sharingan", "A naruto jutsu category.");
	}

	public boolean called(String raw, String[] args, MessageReceivedEvent event) {
		if (args.length < 1) {
			event.getTextChannel()
					.sendMessage(
							DiscordMessage.embedProblem(event.getAuthor(), "Invalid **arguments**! Try !help naruto"))
					.queue();
			return false;
		}
		if (!subcommands.containsKey(args[0].toLowerCase())) {
			event.getTextChannel().sendMessage(DiscordMessage.embedProblem(event.getAuthor(),
					"Invalid **sub command**! Please check the list using !help naruto")).queue();
			return false;
		}
		File dir = new File("yasu_naruto");
		if (!dir.exists()) {
			event.getTextChannel().sendMessage(DiscordMessage.embedProblem(event.getAuthor(),
					"Oh shit! The **naruto folder** was not found. This command needs that!")).queue();

			return false;
		}
		return true;
	}

	public void action(String raw, String[] args, MessageReceivedEvent event) {
		String type = args[0].toLowerCase();

		String path = "yasu_naruto/" + type.trim();
		File dir = new File(path);
		File[] files = dir.listFiles();
		if ((files == null) || (files.length <= 0)) {
			event.getChannel().sendMessage(DiscordMessage.embedProblem(event.getAuthor(),
					"Could **not** find any files on the naruto folder? Check if you have uploaded it correctly to the bot file base."))
					.queue();
			return;
		}
		File toSend = null;
		String toSendName = null;
		if (args.length == 1) {
			int rindex = ThreadLocalRandom.current().nextInt(0, files.length + 1);
			toSend = files[rindex];
			toSendName = "jutsu!";
		} else {
			float highest = 0.0F;
			args[0] = "";
			String find = String.join(" ", args);
			for (File v : files) {
				String name = v.getName().replace('_', ' ');

				name = name.substring(0, name.lastIndexOf('.'));

				float cmp = StringSimilar.getDistance(name, find, 2);
				if (cmp == 1.0F) {
					toSend = v;
					toSendName = name;
					break;
				}
				if (cmp > highest) {
					highest = cmp;
					toSend = v;
					toSendName = name;
				}
			}
			if (highest < 0.3D) {
				event.getChannel().sendMessage(DiscordMessage.embedProblem(event.getAuthor(),
						"Could **not** find any jutsu named or similar to: \"" + find + "\"")).queue();
				return;
			}
		}
		if (toSend == null) {
			event.getChannel().sendMessage(
					DiscordMessage.embedProblem(event.getAuthor(), "Problem while loading file, contact developer!"))
					.queue();
			return;
		}
		event.getChannel()
				.sendFile(toSend, DiscordMessage.toSimple("**" + type.toUpperCase() + ", " + toSendName + "**"))
				.queue();
	}

	public String getDescription() {
		return "Displays a naruto jutsu :D";
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
