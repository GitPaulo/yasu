package commands.fun;

import commands.Command;
import commands.CommandCategory;
import commands.CommandScope;
import java.util.HashMap;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.json.JSONObject;
import utilities.UtilityMethods;

public class Neko implements Command {
	private static final HashMap<String, String> subcommands = new HashMap<String, String>();

	public boolean called(String raw, String[] args, MessageReceivedEvent event) {
		return true;
	}

	public void action(String raw, String[] args, MessageReceivedEvent event) {
		String link = "https://nekos.life/api/neko";
		String response = UtilityMethods.requestReadAllURL(link);

		JSONObject json = new JSONObject(response);
		String neko_url = (String) json.get("neko");

		event.getChannel().sendMessage(neko_url).queue();
	}

	public void executed(boolean succ, MessageReceivedEvent event) {
	}

	public String getDescription() {
		return "Displays a neko!! :3";
	}

	public HashMap<String, String> getSubCommands() {
		return subcommands;
	}

	public CommandCategory getCategory() {
		return CommandCategory.FUN;
	}

	public CommandScope getScope() {
		return CommandScope.BOTH;
	}

	public boolean isListed() {
		return true;
	}
}
