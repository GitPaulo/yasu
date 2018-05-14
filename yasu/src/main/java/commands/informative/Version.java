package commands.informative;

import commands.Command;
import commands.CommandCategory;
import commands.CommandScope;
import core.DiscordMessage;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.StringJoiner;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

public class Version implements Command {
	private static final HashMap<String, String> subcommands = new HashMap<String, String>();

	public boolean called(String raw, String[] args, MessageReceivedEvent event) {
		return true;
	}

	public void action(String raw, String[] args, MessageReceivedEvent event) {
		MavenXpp3Reader reader = new MavenXpp3Reader();
		Model model = null;
		try {
			model = reader.read(new FileReader("pom.xml"));
		} catch (IOException | XmlPullParserException e) {
			e.printStackTrace();
			event.getChannel().sendMessage(DiscordMessage.embedProblem(event.getAuthor(),
					"Problem while reading POM.XML for data! Contact developer!")).queue();
			return;
		}
		StringJoiner joiner = new StringJoiner("\n");

		joiner.add("? **BOT VERSION** _____ " + model.getVersion());
		joiner.add("? ARTIFACT ID _____ " + model.getArtifactId());
		joiner.add("? DESCRIPION _____ " + model.getDescription());
		joiner.add("? #DEPENDENCIES (" + model.getDependencies().size() + ")");
		for (Dependency v : model.getDependencies()) {
			joiner.add("\t? ID:" + v.getArtifactId() + " | Group: " + v.getGroupId());
		}
		joiner.add(
				"```MOTD: This version is supposed to be the best most stable version yet. If it's not - cry me a river. \n\n Type !help for command documentation.```");

		event.getChannel().sendMessage(
				DiscordMessage.embedSimple(event.getJDA().getSelfUser(), "VERSION - Information", joiner.toString()))
				.queue();
	}

	public void executed(boolean succ, MessageReceivedEvent event) {
	}

	public String getDescription() {
		return "Displays current bot version and build information";
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
