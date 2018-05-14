package commands;

import java.util.ArrayList;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class CommandParser {
	public CommandContainer parse(String rw, MessageReceivedEvent e) {
		ArrayList<String> split = new ArrayList<String>();
		String raw = rw;
		String beheaded = raw.substring(1);
		String[] splitBeheaded = beheaded.split(" ");
		for (String s : splitBeheaded) {
			split.add(s);
		}
		String invoke = ((String) split.get(0)).toLowerCase();
		String[] args = new String[split.size() - 1];
		split.subList(1, split.size()).toArray(args);

		return new CommandContainer(raw, beheaded, splitBeheaded, invoke, args, e);
	}

	public static class CommandContainer {
		public final String raw;
		public final String beheaded;
		public final String[] splitBeheaded;
		public final String invoke;
		public final String[] args;
		public final MessageReceivedEvent event;

		public CommandContainer(String raw, String beheaded, String[] splitBeheaded, String invoke, String[] args,
				MessageReceivedEvent event) {
			this.raw = raw;
			this.beheaded = beheaded;
			this.splitBeheaded = splitBeheaded;
			this.invoke = invoke;
			this.args = args;
			this.event = event;
		}
	}
}
