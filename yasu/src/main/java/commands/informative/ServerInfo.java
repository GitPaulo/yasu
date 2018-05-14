package commands.informative;

import commands.Command;
import commands.CommandCategory;
import commands.CommandScope;
import core.DiscordMessage;
import java.util.HashMap;
import java.util.List;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.managers.GuildManager;

public class ServerInfo implements Command {
	private static final HashMap<String, String> subcommands = new HashMap<String, String>();

	public boolean called(String raw, String[] args, MessageReceivedEvent event) {
		return true;
	}

	public void action(String raw, String[] args, MessageReceivedEvent event) {
		GuildManager gm = new GuildManager(event.getGuild());
		Guild g = gm.getGuild();

		event.getTextChannel().sendMessage(DiscordMessage.embedSimple(event.getAuthor(),
				"Guild/Server Information - " + g.getName(),
				"**Owner:** " + g.getOwner().getEffectiveName() + " ( Online: " + g.getOwner().getOnlineStatus()
						+ " )\n**Guild ID:** " + g.getId() + "\n**Region:** " + g.getRegion() + "\n**Public Channel:** "
						+ g.getDefaultChannel().getName() + "\n**Public Role:** " + g.getPublicRole().getName()
						+ "\n**AFK Channel ID:** "
						+ (g.getAfkChannel() == null ? "__No AFK Channel__" : g.getAfkChannel().getId())
						+ "\n**#VoiceChannels:** " + g.getVoiceChannels().size() + "\n**#TextChannels:** "
						+ g.getTextChannels().size() + "\n**#Users:** " + g.getMembers().size() + "\n**#Bans:** "
						+ ((List<?>) g.getBanList().complete()).size() + "\n**AudioManager Status:** "
						+ g.getAudioManager().getConnectionStatus().toString() + "\n**Verification:** "
						+ g.checkVerification() + "\n**Guild creation date:** " + g.getCreationTime().getYear() + "/"
						+ g.getCreationTime().getMonth() + "/" + g.getCreationTime().getDayOfMonth()
						+ "\n**Public role:** " + g.getPublicRole().getName() + "\n"))
				.queue();
	}

	public void executed(boolean succ, MessageReceivedEvent event) {
	}

	public String getDescription() {
		return "Display information about the current guild/server!";
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
