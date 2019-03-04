package commands;

import java.util.HashMap;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public abstract interface Command {
	public abstract boolean called(String raw, String[] args,
			MessageReceivedEvent event);

	public abstract void action(String raw, String[] args,
			MessageReceivedEvent event);

	public abstract void executed(boolean succ, MessageReceivedEvent event);

	public abstract String getDescription();

	public abstract HashMap<String, String> getSubCommands();

	public abstract CommandCategory getCategory();

	public abstract CommandScope getScope();

	public abstract boolean isListed();
}
