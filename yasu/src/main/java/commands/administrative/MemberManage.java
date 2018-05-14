package commands.administrative;

import commands.Command;
import commands.CommandCategory;
import commands.CommandScope;
import core.Client;
import core.DiscordMessage;
import java.util.HashMap;
import java.util.List;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.HierarchyException;
import net.dv8tion.jda.core.managers.GuildController;

public class MemberManage implements Command {
	private static final HashMap<String, String> subcommands = new HashMap<String, String>();

	static {
		subcommands.put("ban",
				"ban <user> <deldays> <reason> | bans a user from the server. If deldays = 0, it wont delete messages.");

		subcommands.put("unban", "unban <user> | undoes a ban!");
		subcommands.put("kick", "kick <user> reason | kicks a user from server.");
		subcommands.put("setrole", "setrole <user> <role> | Sets a user to a role.");
		subcommands.put("removerole", "removerole <user> <role> | Removes a users role");
		subcommands.put("mute", "mute <user> | Mutes a user.");
		subcommands.put("unmute", "unmute <user> | Unmutes a user.");
		subcommands.put("deafen", "deafen <user> | Deafens a user.");
		subcommands.put("undeafen", "undeaden <user> | Undeafens a user.");
		subcommands.put("prune",
				"prune <days> | Kicks all members that were inactive on the server for the last X days.");

		subcommands.put("transferownership",
				"transferownership <user> | Transfers the ownership of the guild to the user targeted");
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
		Member target = event.getGuild().getMember(Client.getUserbyString(args[1]));
		if (target == null) {
			event.getChannel()
					.sendMessage(DiscordMessage.embedProblem(event.getAuthor(),
							"Could not find targeted user or user is not member of guild!\n String used: " + args[1]))
					.queue();
			return;
		}
		try {
			args[1] = "";
			if ("ban".equalsIgnoreCase(args[0])) {
				args[0] = "";
				int delDays = 0;
				try {
					Integer.parseInt(args[2]);
				} catch (NumberFormatException e) {
					event.getChannel().sendMessage(
							DiscordMessage.embedProblem(event.getAuthor(), "Invalid days input! Must be a number."))
							.queue();
				}
				args[2] = "";
				String reason = String.join(" ", args);

				gc.ban(target, delDays, reason).queue();

				event.getChannel()
						.sendMessage(DiscordMessage.embedSimple(event.getAuthor(), "Ban",
								"**" + target.getUser().getName() + "** has been banned for " + delDays
										+ "days.\nReason: *" + reason + "*"))

						.queue();
			} else if ("kick".equalsIgnoreCase(args[0])) {
				args[0] = "";
				String reason = String.join(" ", args);
				gc.kick(target, reason).queue();
				event.getChannel()
						.sendMessage(DiscordMessage.embedSimple(event.getAuthor(), "Kick",
								"**" + target.getUser().getName() + "** has been kicked.\nReason: *" + reason + "*"))
						.queue();
			} else if ("setrole".equalsIgnoreCase(args[0])) {
				args[0] = "";
				List<Role> roles = event.getGuild().getRolesByName(args[2], true);
				if (roles.isEmpty()) {
					event.getChannel().sendMessage(DiscordMessage.embedProblem(event.getAuthor(),
							"Roles could not be found! Try again and check spelling.")).queue();
					return;
				}
				gc.addRolesToMember(target, roles).queue();
				event.getChannel()
						.sendMessage(DiscordMessage.embedSimple(event.getAuthor(), "Role(s) Added",
								"**" + target.getUser().getName() + "** has been put in the role(s): " + args[2]))
						.queue();
			} else if ("removerole".equalsIgnoreCase(args[0])) {
				args[0] = "";
				List<Role> roles = event.getGuild().getRolesByName(args[2], true);
				if (roles.isEmpty()) {
					event.getChannel().sendMessage(DiscordMessage.embedProblem(event.getAuthor(),
							"Roles could not be found! Try again and check spelling.")).queue();
					return;
				}
				gc.removeRolesFromMember(target, roles).queue();
				event.getChannel()
						.sendMessage(DiscordMessage.embedSimple(event.getAuthor(), "Role(s) Removed",
								"**" + target.getUser().getName() + "** has been put in the role(s): " + args[2]))
						.queue();
			} else if ("mute".equalsIgnoreCase(args[0])) {
				args[0] = "";
				gc.setMute(target, true).queue();
				event.getChannel().sendMessage(DiscordMessage.embedSimple(event.getAuthor(), "Mute",
						"**" + target.getUser().getName() + "** has been muted.")).queue();
			} else if ("unmute".equalsIgnoreCase(args[0])) {
				args[0] = "";
				gc.setMute(target, false).queue();
				event.getChannel().sendMessage(DiscordMessage.embedSimple(event.getAuthor(), "Mute",
						"**" + target.getUser().getName() + "** has been unmuted.")).queue();
			} else if ("deafen".equalsIgnoreCase(args[0])) {
				args[0] = "";
				gc.setDeafen(target, true).queue();
				event.getChannel().sendMessage(DiscordMessage.embedSimple(event.getAuthor(), "Deafen",
						"**" + target.getUser().getName() + "** has been deafened.")).queue();
			} else if ("undeafen".equalsIgnoreCase(args[0])) {
				args[0] = "";
				gc.setDeafen(target, false).queue();
				event.getChannel().sendMessage(DiscordMessage.embedSimple(event.getAuthor(), "Deafen",
						"**" + target.getUser().getName() + "** has been undeafened.")).queue();
			} else if ("unban".equalsIgnoreCase(args[1])) {
				args[0] = "";
				gc.unban(target.getUser()).queue();
				event.getChannel().sendMessage(DiscordMessage.embedSimple(event.getAuthor(), "Ban",
						"**" + target.getUser().getName() + "** has been unbaned.")).queue();
			} else if ("prune".equalsIgnoreCase(args[1])) {
				args[0] = "";
				int days = 0;
				try {
					days = Integer.parseInt(args[2]);
				} catch (NumberFormatException e) {
					event.getChannel().sendMessage(
							DiscordMessage.embedProblem(event.getAuthor(), "Invalid days input! Must be a number."))
							.queue();
				}
				args[2] = "";

				gc.prune(days).queue();
				event.getChannel().sendMessage(DiscordMessage.embedSimple(event.getAuthor(), "Prune",
						"**" + target.getUser().getName() + "** server has been prune for the last **" + days + "**!"))
						.queue();
			} else if ("transferownership".equalsIgnoreCase(args[1])) {
				args[0] = "";
				gc.transferOwnership(target).queue();
				event.getChannel()
						.sendMessage(DiscordMessage.embedSimple(event.getAuthor(), "Guild Ownership Transfered",
								"**" + target.getUser().getName() + "** is now the server owner!"))
						.queue();
			}
		} catch (HierarchyException e) {
			event.getChannel()
					.sendMessage(DiscordMessage.embedProblem(event.getAuthor(), new StringBuilder()
							.append("**Cannot** modify the status of this person. Hierarchy exception was thrown: __")
							.append(e.getMessage()).toString()) + "__")
					.queue();
		}
	}

	public String getDescription() {
		return "Use the bot to manage your members in the server!";
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
