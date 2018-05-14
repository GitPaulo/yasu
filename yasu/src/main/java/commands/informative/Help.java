package commands.informative;

import commands.Command;
import commands.CommandCategory;
import commands.CommandScope;
import core.DiscordMessage;
import core.LongMessage;
import core.Yasu;
import java.awt.Color;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringJoiner;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Help implements Command {
	private final HashMap<String, String> subcommands = new HashMap<String, String>();

	public boolean called(String raw, String[] args, MessageReceivedEvent event) {
		return true;
	}

	public void action(String raw, String[] args, MessageReceivedEvent event) {
		if (args.length == 0) {
			StringJoiner joiner = new StringJoiner("\n");
			Iterator<Map.Entry<String, Command>> it = Yasu.bot.getCommands().entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<String, Command> pair = it.next();
				String key = pair.getKey();
				Command value = pair.getValue();
				if (pair.getValue().isListed()) {
					StringBuilder subcmds = new StringBuilder("\n Subcommands: ");
					if (!value.getSubCommands().isEmpty()) {
						for (Map.Entry<String, String> entry : value.getSubCommands().entrySet()) {
							String cmd = (String) entry.getKey();
							String desc = (String) entry.getValue();
							subcmds.append("\n       " + cmd + ": " + desc);
						}
					} else {
						subcmds.append(" [No sub commands]");
					}
					joiner.add(":globe_with_meridians: | " + key + "\n Description: " + value.getDescription()
							+ "\n Category: " + value.getCategory().getDisplayName() + "\n Scope: "
							+ value.getScope().name() + subcmds.toString());
				}
			}
			String body = "Type <prefix>help <command> for detailed documentation.\n\n:file_folder:COMMANDS:\n\n"
					+ joiner.toString();
			MessageEmbed embed = DiscordMessage.embedSimple(event.getAuthor(),
					":card_box: Help Documentation - " + Yasu.bot.getInstance().getSelfUser().getName(), body);

			LongMessage.storeBigMessage((Message) event.getChannel().sendMessage(embed).complete(), body);
		} else if (args.length > 0) {
			if (Yasu.bot.getCommands().containsKey(args[0].toLowerCase())) {
				HashMap<String, String> scmds = ((Command) Yasu.bot.getCommands().get(args[0].toLowerCase()))
						.getSubCommands();
				String help_message = null;
				if (scmds.size() == 0) {
					help_message = "```xl\n This command has 0 subcommands!```\n";
					return;
				}
				StringJoiner joiner = new StringJoiner("\n");
				Iterator<Map.Entry<String, String>> it = scmds.entrySet().iterator();
				while (it.hasNext()) {
					Map.Entry<String, String> pair = it.next();
					String key = pair.getKey();
					String value = pair.getValue();

					joiner.add("? " + key + ": " + value);
				}
				help_message = "```xl\n" + joiner.toString() + "\n```";

				event.getChannel().sendMessage(DiscordMessage.embedMessage(event.getAuthor(),
						"Help for \"" + args[0].toLowerCase() + "\":", help_message, Color.BLACK,
						"https://upload.wikimedia.org/wikipedia/commons/thumb/a/ac/VisualEditor_-_Icon_-_Help.svg/2000px-VisualEditor_-_Icon_-_Help.svg.png"))

						.queue();
			} else {
				event.getChannel().sendMessage(
						DiscordMessage.embedProblem(event.getAuthor(), "There exists no " + args[0] + " command!"))
						.queue();
			}
		}
	}

	public void executed(boolean succ, MessageReceivedEvent event) {
	}

	public String getDescription() {
		return "";
	}

	public HashMap<String, String> getSubCommands() {
		return this.subcommands;
	}

	public CommandCategory getCategory() {
		return CommandCategory.INFORMATIVE;
	}

	public CommandScope getScope() {
		return CommandScope.BOTH;
	}

	public boolean isListed() {
		return false;
	}
}
