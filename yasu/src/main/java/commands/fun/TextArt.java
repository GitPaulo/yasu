package commands.fun;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

import commands.Command;
import commands.CommandCategory;
import commands.CommandScope;
import core.DiscordMessage;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import utilities.AsciiArt;

public class TextArt implements Command {
	private static final HashMap<String, String> subcommands = new HashMap<String, String>();
	private int size = 12;
	private int type = Font.PLAIN;
	
	static {
		subcommands.put("<font> <text>", "Text is converted to font and artified.");
		subcommands.put("fonts", "Displays fonts.");
		subcommands.put("size", "Set font size.");
		subcommands.put("type", "Set font type.");
	}
	
	@Override
	public boolean called(String raw, String[] args, MessageReceivedEvent event) {
		if( args.length < 1 ) {
			event.getChannel().sendMessage(DiscordMessage.embedProblem(event.getAuthor(), "Invalid #arguments!"))
			.queue();
			return false;
		}
		return true; 
	}

	@Override
	public void action(String raw, String[] args, MessageReceivedEvent event) {
		String arg1 = args[0];
		args[0] = "";
		
		if( arg1.equalsIgnoreCase("fonts") ) {
			event.getChannel().sendMessage("**All available fonts: [Replace Space by _ i.e: Lucida_Console]**\n" + Arrays.toString(GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames())).queue();
		} else if ( arg1.equalsIgnoreCase("size") ) {
			int old_size = size;
			try {
				size = Integer.parseInt(args[1]);
			} catch ( NumberFormatException e ) {
				event.getChannel().sendMessage(DiscordMessage.embedProblem(event.getAuthor(), "Size input was not a number!")).queue();
			}
			event.getChannel().sendMessage("**Font Size** set from " + old_size + " to " + size).queue();
		} else if ( arg1.equalsIgnoreCase("type")  ) {
			if( args[1].equalsIgnoreCase("plain") ) {
				type = Font.PLAIN;
			} else if ( args[1].equalsIgnoreCase("bold") ) {
				type = Font.BOLD;
			} else if ( args[1].equalsIgnoreCase("italic") ) {
				type = Font.ITALIC;
			} else {
				event.getChannel().sendMessage(DiscordMessage.embedProblem(event.getAuthor(), "Type input was not a valid type!")).queue();
			}
		} else {
			String input = String.join(" ", args);
			arg1 = arg1.replace('_', ' ');
			try {
				String output = AsciiArt.artify(input, new Font(arg1, type, size));
				if( output.length() >= 2000 ) {
					event.getChannel().sendMessage(DiscordMessage.embedProblem(event.getAuthor(), "Text was too big for discord! Try reducing the font size. <prefix>textart font <size>")).queue();
					return;
				}
				event.getChannel().sendMessage(output).queue();
				File image = new File("artify.png");
				if(image.exists())
					event.getChannel().sendFile(image).queue();
			} catch (IOException e) {
				e.printStackTrace();
				event.getChannel().sendMessage(DiscordMessage.embedProblem(event.getAuthor(), "There was a problem parsing the text:\n" + e.getMessage())).queue();
			}
		}
		
	}

	@Override
	public void executed(boolean succ, MessageReceivedEvent event) {
	}

	@Override
	public String getDescription() {
		return "Transform text into Ascii Art!";
	}

	@Override
	public HashMap<String, String> getSubCommands() {
		return subcommands;
	}

	@Override
	public CommandCategory getCategory() {
		return CommandCategory.UTILITY;
	}

	@Override
	public CommandScope getScope() {
		return CommandScope.BOTH;
	}

	@Override
	public boolean isListed() {
		return true;
	}

}
