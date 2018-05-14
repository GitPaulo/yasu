package commands.informative;

import commands.Command;
import commands.CommandCategory;
import commands.CommandScope;
import core.DiscordMessage;
import core.Yasu;
import java.awt.Color;
import java.util.HashMap;
import java.util.StringJoiner;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Information implements Command {
	private static final HashMap<String, String> subcommands = new HashMap<String, String>();

	public boolean called(String raw, String[] args, MessageReceivedEvent event) {
		return true;
	}

	public void action(String raw, String[] args, MessageReceivedEvent event) {
		int Channels = 0;
		int voiceChannels = 0;

		int users = 0;
		int offline = 0;
		int online = 0;
		int inactive = 0;

		JDA jda = event.getJDA();
		for (Guild g : jda.getGuilds()) {
			for (Member u : g.getMembers()) {
				switch (u.getOnlineStatus()) {
				case ONLINE:
					online++;
					break;
				case OFFLINE:
					offline++;
					break;
				case DO_NOT_DISTURB:
					inactive++;
					break;
				case IDLE:
					inactive++;
					break;
				case INVISIBLE:
					inactive++;
				default:
					break;
				}
			}
			users += g.getMembers().size();
			Channels += g.getTextChannels().size();
			voiceChannels += g.getVoiceChannels().size();
		}
		int servers = jda.getGuilds().size();
		int channels = Channels + voiceChannels;

		StringJoiner joiner = new StringJoiner("\n");

		joiner.add("? Servers _____ " + servers);
		joiner.add("?");
		joiner.add("? Channels ____ " + channels);
		joiner.add("?  - Text _____ " + Channels);
		joiner.add("?  - Voice ____ " + voiceChannels);
		joiner.add("?");
		joiner.add("? Users _______ " + users);
		joiner.add("?  - Online ___ " + online);
		joiner.add("?  - Offline __ " + offline);
		joiner.add("?  - Inactive _ " + inactive);
		joiner.add("?");
		joiner.add("? Ping ____ " + Yasu.bot.getInstance().getPing() + "ms");
		joiner.add("? Creator _____ Paul");
		joiner.add("? Commands ____ " + Yasu.bot.getCommands().size());
		joiner.add("? Library _____ JDA");
		joiner.add("? Uptime ______ " + Yasu.getUptime() / 1000L + "sec.");

		event.getChannel().sendMessage(DiscordMessage.embedMessage(event.getAuthor(),
				"Information about \"" + Yasu.bot.getInstance().getSelfUser().getName(),
				"```xl\n" + joiner.toString() + "```\n", Color.BLACK,
				"https://upload.wikimedia.org/wikipedia/commons/thumb/a/ac/VisualEditor_-_Icon_-_Help.svg/2000px-VisualEditor_-_Icon_-_Help.svg.png"))

				.queue();
	}

	public void executed(boolean succ, MessageReceivedEvent event) {
	}

	public String getDescription() {
		return null;
	}

	public HashMap<String, String> getSubCommands() {
		return subcommands;
	}

	public CommandCategory getCategory() {
		return CommandCategory.INFORMATIVE;
	}

	public CommandScope getScope() {
		return CommandScope.BOTH;
	}

	public boolean isListed() {
		return true;
	}
}
