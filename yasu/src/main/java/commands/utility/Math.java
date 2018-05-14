package commands.utility;

import commands.Command;
import commands.CommandCategory;
import commands.CommandScope;
import core.DiscordMessage;
import java.util.HashMap;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Math implements Command {
	private static final HashMap<String, String> subcommands = new HashMap<String, String>();

	static {
		subcommands.put("<expression>", "A mathematical expression to be computed.");
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
		ScriptEngineManager mgr = new ScriptEngineManager();
		ScriptEngine engine = mgr.getEngineByName("JavaScript");
		try {
			event.getChannel()
					.sendMessage("Computed Result: ```"
							+ Integer.toString(((Integer) engine.eval(String.join(" ", args))).intValue()) + "```")
					.queue();
		} catch (ScriptException e) {
			event.getChannel().sendMessage(DiscordMessage.embedProblem(event.getAuthor(),
					"Problem parsing math expression! Try again and check contents")).queue();
			e.printStackTrace();
		}
	}

	public String getDescription() {
		return "Computes a mathematical expression in chat";
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
