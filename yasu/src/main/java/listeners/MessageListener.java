package listeners;

import commands.Command;
import commands.CommandParser;
import commands.CommandScope;
import core.DiscordMessage;
import core.Logger;
import core.LongMessage;
import core.Yasu;
import java.util.ArrayList;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class MessageListener extends ListenerAdapter {
	public void onMessageReceived(MessageReceivedEvent event) {
		User nissan = event.getJDA().getSelfUser();
		User author = event.getMessage().getAuthor();
		String message = event.getMessage().getContentRaw();
		if (event.getChannelType() == ChannelType.PRIVATE) {
			Logger.print(new Object[] { "[PRIVATE][" + author.getName() + "] " + message });
		} else {
			Logger.print(new Object[] { "[" + event.getGuild().getName() + "][" + author.getName() + "] " + message });
		}
		for (String x : Yasu.bot.getPrefixes()) {
			if ((message.startsWith(x)) && (!author.getId().equals(nissan.getId()))) {
				handleCommand(Yasu.bot.getCommandparser().parse(message, event));
			}
		}
	}

	public void onMessageReactionAdd(MessageReactionAddEvent event) {
		if (event.getUser().equals(Yasu.bot.getInstance().getSelfUser())) {
			return;
		}
		String msg_id = event.getReaction().getMessageId();
		ArrayList<LongMessage> storage = Yasu.bot.getStorageBigMessages();
		LongMessage message = null;
		for (LongMessage v : storage) {
			if (v.getMessage().getId().equals(msg_id)) {
				message = v;
				break;
			}
		}
		if (message == null) {
			return;
		}
		String emote_name = event.getReactionEmote().getName();
		int current_page = message.getPage();
		if (emote_name.equals("?")) {
			message.setPage(--current_page);
		} else {
			if (emote_name.equals("?")) {
				LongMessage.removeFromStorage(msg_id);

				event.getChannel().deleteMessageById(msg_id).queue();
				return;
			}
			if (emote_name.equals("?")) {
				message.setPage(++current_page);
			} else {
				event.getReaction().removeReaction(event.getUser()).complete();
				return;
			}
		}
		if (message.hasEmbeds()) {
			MessageEmbed embed = (MessageEmbed) message.getEmbed().get(0);
			EmbedBuilder eb = new EmbedBuilder();
			eb.setAuthor(embed.getAuthor().getName(), embed.getAuthor().getIconUrl(), embed.getAuthor().getIconUrl());
			eb.setColor(embed.getColor());
			eb.setDescription(message.getCurrentContent());
			eb.setTitle(embed.getTitle());
			if (embed.getThumbnail() != null) {
				eb.setThumbnail(embed.getThumbnail().getProxyUrl());
			}
			event.getChannel().editMessageById(message.getMessage().getId(), eb.build()).queue();
		} else {
			event.getChannel().editMessageById(message.getMessage().getId(), message.getCurrentContent()).queue();
		}
		event.getReaction().removeReaction(event.getUser()).complete();
	}

	private void handleCommand(CommandParser.CommandContainer cmd) {
		if (Yasu.bot.getCommands().containsKey(cmd.invoke)) {
			if (Yasu.bot.isBlackListed(cmd.event.getAuthor())) {
				cmd.event.getChannel().sendMessage(DiscordMessage.embedBlackListMessage(cmd.event.getAuthor(),
						"BLOCKED", "You are blocked from using commands!")).queue();
				return;
			}
			Command command = (Command) Yasu.bot.getCommands().get(cmd.invoke);
			if (CommandScope.matchChannelType(cmd.event.getChannelType(), command.getScope())) {
				if (!cmd.event.getChannelType().equals(ChannelType.PRIVATE)) {
					if (!command.getCategory().hasPermissions(cmd.event.getMember())) {
						cmd.event.getChannel().sendMessage(DiscordMessage.embedBlackListMessage(cmd.event.getAuthor(),
								"LACK OF PERMISSIONS",
								"You do not have the required permissions for this command type! Needed permissions: `"
										+ command.getCategory().getNeededPermissions().toString() + "`"))
								.queue();
						return;
					}
				}
				boolean safe = ((Command) Yasu.bot.getCommands().get(cmd.invoke)).called(cmd.raw, cmd.args, cmd.event);
				if (safe) {
					command.action(cmd.raw, cmd.args, cmd.event);
					command.executed(safe, cmd.event);
				} else {
					command.executed(safe, cmd.event);
				}
			} else {
				cmd.event.getChannel().sendMessage(DiscordMessage.embedProblem(cmd.event.getAuthor(),
						"This command cannot be operated under this channel type!")).queue();
			}
		}
	}
}
