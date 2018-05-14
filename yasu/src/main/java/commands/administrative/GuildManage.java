package commands.administrative;

import commands.Command;
import commands.CommandCategory;
import commands.CommandScope;
import core.DiscordMessage;
import java.util.HashMap;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.managers.GuildController;
import net.dv8tion.jda.core.managers.GuildManager;

public class GuildManage implements Command {
	private static final HashMap<String, String> subcommands = new HashMap<String, String>();

	static {
		subcommands.put("newtchannel", "newtchannel <name> | creates a new text channel");
		subcommands.put("newvchannel", "newvchannel <name> | creates a new voice channel");
		subcommands.put("setname", "setname <name> | sets the name of the guild as <name>");
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
		GuildController gc = new GuildController(event.getGuild());
		GuildManager gm = new GuildManager(event.getGuild());
		if ("newtchannel".equalsIgnoreCase(args[0])) {
			args[0] = "";
			String cname = String.join(" ", args);
			gc.createTextChannel(cname).queue();
			event.getChannel()
					.sendMessage(DiscordMessage.embedSimple(event.getAuthor(), "Text Channel Created!",
							"**" + cname + "** has been created as a *text channel* by " + event.getAuthor().getName()))
					.queue();
		} else if ("newvchannel".equalsIgnoreCase(args[0])) {
			args[0] = "";
			String cname = String.join(" ", args);
			gc.createVoiceChannel(cname).queue();
			event.getChannel().sendMessage(DiscordMessage.embedSimple(event.getAuthor(), "Voice Channel Created!",
					"**" + cname + "** has been created as a *voice channel* by " + event.getAuthor().getName()))
					.queue();
		} else if ("setname".equalsIgnoreCase(args[0])) {
			args[0] = "";
			String cname = String.join(" ", args);
			gm.setName(cname).queue();
			event.getChannel().sendMessage(DiscordMessage.embedSimple(event.getAuthor(), "Guild Name Changed",
					event.getAuthor().getName() + " has changed the guild name to: **" + cname + "**")).queue();
		}
	}

	public String getDescription() {
		return "Use the bot to manage your guild!";
	}

	public HashMap<String, String> getSubCommands() {
		return subcommands;
	}

	public CommandCategory getCategory() {
		return CommandCategory.ADMINISTRATIVE;
	}

	public CommandScope getScope() {
		return CommandScope.PUBLIC;
	}

	public boolean isListed() {
		return true;
	}

	public void executed(boolean succ, MessageReceivedEvent event) {
	}
}
