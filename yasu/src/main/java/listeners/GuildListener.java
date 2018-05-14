package listeners;

import core.DiscordMessage;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberNickChangeEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberRoleAddEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberRoleRemoveEvent;
import net.dv8tion.jda.core.events.guild.update.GuildUpdateAfkChannelEvent;
import net.dv8tion.jda.core.events.guild.update.GuildUpdateIconEvent;
import net.dv8tion.jda.core.events.guild.update.GuildUpdateMFALevelEvent;
import net.dv8tion.jda.core.events.guild.update.GuildUpdateNameEvent;
import net.dv8tion.jda.core.events.guild.update.GuildUpdateOwnerEvent;
import net.dv8tion.jda.core.events.guild.update.GuildUpdateRegionEvent;
import net.dv8tion.jda.core.events.guild.update.GuildUpdateSystemChannelEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class GuildListener extends ListenerAdapter {
	public void onGuildUpdateAfkChannel(GuildUpdateAfkChannelEvent event) {
		event.getGuild().getDefaultChannel().sendMessage(DiscordMessage.embedEventMessage(event.getJDA().getSelfUser(),
				"AFK Channel Updated", "The **AFK Channel** has been updated!\n")).queue();
	}

	public void onGuildUpdateSystemChannel(GuildUpdateSystemChannelEvent event) {
		event.getGuild().getDefaultChannel().sendMessage(DiscordMessage.embedEventMessage(event.getJDA().getSelfUser(),
				"System Channel Updated", "The **System Channel** has been updated!")).queue();
	}

	public void onGuildUpdateIcon(GuildUpdateIconEvent event) {
		event.getGuild().getDefaultChannel()
				.sendMessage(DiscordMessage.embedEventMessage(event.getJDA().getSelfUser(), "Guild Icon Updated!",
						"The Guild has been updated!\n**Old:** " + event.getOldIconUrl() + "\n**New:** "
								+ event.getGuild().getIconUrl()))
				.queue();
	}

	public void onGuildUpdateMFALevel(GuildUpdateMFALevelEvent event) {
		event.getGuild().getDefaultChannel()
				.sendMessage(DiscordMessage.embedEventMessage(event.getJDA().getSelfUser(), "Guild MFA Level Updated",
						"The Guild MFA Level has beed updated!\n**Old:** " + event.getOldMFALevel().name()
								+ "\n**New:** " + event.getGuild().getRequiredMFALevel().name()))
				.queue();
	}

	public void onGuildUpdateName(GuildUpdateNameEvent event) {
		event.getGuild().getDefaultChannel()
				.sendMessage(DiscordMessage.embedEventMessage(event.getJDA().getSelfUser(), "Guild Name Updated",
						"The Guild Name has been updated!\n**Old:** " + event.getOldName() + "\n**New:** "
								+ event.getGuild().getName()))
				.queue();
	}

	public void onGuildUpdateOwner(GuildUpdateOwnerEvent event) {
		event.getGuild().getDefaultChannel()
				.sendMessage(DiscordMessage.embedEventMessage(event.getJDA().getSelfUser(), "Guild Owner Updated",
						"The Guild Owner has been updated!\n**Old:** " + event.getOldOwner().getEffectiveName()
								+ "\n**New:** " + event.getGuild().getOwner().getEffectiveName()))
				.queue();
	}

	public void onGuildUpdateRegion(GuildUpdateRegionEvent event) {
		event.getGuild().getDefaultChannel()
				.sendMessage(DiscordMessage.embedEventMessage(event.getJDA().getSelfUser(), "Guild Region Updated",
						"The Guild Name has been updated!\n**Old:** " + event.getOldRegion().getName() + "\n**New:** "
								+ event.getGuild().getRegion().getName()))
				.queue();
	}

	public void onGuildMemberJoin(GuildMemberJoinEvent event) {
		event.getGuild().getDefaultChannel().sendMessage(DiscordMessage.embedEventMessage(event.getJDA().getSelfUser(),
				"Member has joined the server!",
				"**" + event.getUser().getName() + "** has joined the server!\n ID: " + event.getUser().getId()))
				.queue();
	}

	public void onGuildMemberLeave(GuildMemberLeaveEvent event) {
		event.getGuild().getDefaultChannel()
				.sendMessage(DiscordMessage.embedEventMessage(event.getJDA().getSelfUser(),
						"Member has left the server!",
						"**" + event.getUser().getName() + "** has left the server!\n ID: " + event.getUser().getId()))
				.queue();
	}

	public void onGuildMemberRoleAdd(GuildMemberRoleAddEvent event) {
		event.getGuild().getDefaultChannel()
				.sendMessage(
						DiscordMessage
								.embedEventMessage(event.getJDA().getSelfUser(), "Member promoted!",
										"**" + event.getUser().getName() + "** has been added to the role "
												+ event.getRoles().get(0) + "\n ID: " + event.getUser().getId()))
				.queue();
	}

	public void onGuildMemberRoleRemove(GuildMemberRoleRemoveEvent event) {
		event.getGuild().getDefaultChannel()
				.sendMessage(
						DiscordMessage
								.embedEventMessage(event.getJDA().getSelfUser(), "Member demoted!",
										"**" + event.getUser().getName() + "** has been removed of the role "
												+ event.getRoles().get(0) + "\n ID: " + event.getUser().getId()))
				.queue();
	}

	public void onGuildMemberNickChange(GuildMemberNickChangeEvent event) {
		event.getGuild().getDefaultChannel().sendMessage(DiscordMessage.embedEventMessage(event.getJDA().getSelfUser(),
				"Guild Nickname Updated",
				"The Nickname of **" + event.getPrevNick() + "** has been updated!\n**New:** " + event.getNewNick()))
				.queue();
	}
}
