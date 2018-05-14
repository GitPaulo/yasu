package commands;

import java.util.HashMap;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public abstract interface Command {
	public abstract boolean called(String paramString, String[] paramArrayOfString,
			MessageReceivedEvent paramMessageReceivedEvent);

	public abstract void action(String paramString, String[] paramArrayOfString,
			MessageReceivedEvent paramMessageReceivedEvent);

	public abstract void executed(boolean paramBoolean, MessageReceivedEvent paramMessageReceivedEvent);

	public abstract String getDescription();

	public abstract HashMap<String, String> getSubCommands();

	public abstract CommandCategory getCategory();

	public abstract CommandScope getScope();

	public abstract boolean isListed();
}
